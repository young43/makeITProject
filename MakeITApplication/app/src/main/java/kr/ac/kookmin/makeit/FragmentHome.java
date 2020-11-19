package kr.ac.kookmin.makeit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import static kr.ac.kookmin.makeit.MainActivity.db;

/**
 * @file FragmentHome
 * @desc 회원정보 및 찜버튼, 지원프로젝트 목록을 볼 수 있는 화면(프래그먼트)
 * @auther 김지홍(20191572), 김찬미(20191574)
 * @date 2020-11-01
 */
public class FragmentHome extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private ArrayList<ListItemProject> arrayData = new ArrayList<>();  // 진행중인(지원한) 프로젝트 목록 arraylist
    private ListViewProjectAdapter adapter;
    ListView listView;


    public FragmentHome() {
        // Required empty public constructor
    }

    // 사용자 정의 callback 함수 (Firebase 데이터가 다 조회되었는지 check하는 역할)
    public interface MyDataCallback{
        void onCallback();
    }


    // TODO: Rename and change types and number of parameters
    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        // 현재 로그인된 ID로 표시
        String userId = SaveSharedPreference.getUserName(getContext());
        TextView textUser = (TextView) rootView.findViewById(R.id.text_username);
        textUser.setText(userId);

        // 찜 목록 리스트 보여주기(클릭 이벤트 추가)
        Button btnBookmark = (Button) rootView.findViewById(R.id.btn_bookmark_list);
        btnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 찜목록 액티비티로 전환
                Intent intent = new Intent(getContext(), BookmarkActivity.class);
                startActivity(intent);
            }
        });

        // 이력서 등록화면으로 전환하기
        Button btnResume = (Button) rootView.findViewById(R.id.btn_resume);
        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ResumeActivity.class);
                startActivity(intent);
            }
        });

        // 진행중인 프로젝트 목록 보여주기
        adapter = new ListViewProjectAdapter(getContext(), android.R.layout.simple_list_item_multiple_choice, this);
        listView = (ListView) rootView.findViewById(R.id.list_myproject);
        listView.setAdapter(adapter);

        selectApplyOnFirebase();
        return rootView;
    }

    // Firebase 연동
    // apply 컬렉션 조회하고, 해당 프로젝트는 selectProjectlistOnFirebase 함수를 통해 다시 재조회함.
    public void selectApplyOnFirebase(){
        String id = SaveSharedPreference.getUserName(getContext());
        final DocumentReference docRef = db.collection("apply").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    arrayData.clear();

                    if (document.exists()) {
                        ApplyInfo apply = new ApplyInfo((HashMap) document.getData());
                        HashMap<String, Boolean> projectMap = apply.getProjectMap();

                        for (String key : projectMap.keySet()) {
                            // 파이어베이스는 NoSQL이라 join을 지원하지 않음. 따라서 두번 쿼리실행.
                            selectProjectlistOnFirebase(key, new MyDataCallback() {
                                @Override
                                public void onCallback() {  // 데이터 로드 후에 화면에 표시
                                    // ListView update
                                    adapter.clear();
                                    adapter.setAll(arrayData);
                                    updateListView();
                                }
                            });
                        }
                    }

                } else {
                    Log.d("apply", "get failed with ", task.getException());
                }
            }
        });

    }

    // Firebase 연동
    // project_list 컬렉션을 조회하고 내부 callback함수를 호출하여 Firebase 데이터 조회가 완료됨을 알림.
    public void selectProjectlistOnFirebase(String key, final MyDataCallback callback){
        // project_list 컬렉션에서 apply 컬렉션에 존재하는 프로젝트 불러오기
        db.collection("project_list").document(key)
            .get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        ListItemProject item = new ListItemProject((HashMap)document.getData()); // 지원한 프로젝트 객체 생성
                        item.setProject_id(document.getId());                                    // 해당 프로젝트 id값 project_id값으로 설정

                        arrayData.add(item); // 진행중인(지원한) 프로젝트 목록에 추가
                    }

                    callback.onCallback(); // firebase project_list 조회하고 진행중인 프로젝트 목록 적용한 뒤, ListView 업데이트
                }
            });
    }

    // adapter 갱신하고 ListView에 설정
    public void updateListView(){
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }
}
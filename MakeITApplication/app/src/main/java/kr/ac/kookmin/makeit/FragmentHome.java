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


    //////진행중인 프로젝트 목록 보여주기
    private ArrayList<ListItemProject> arrayData = new ArrayList<>();
    private ListViewProjectAdapter adapter;
    ListView listView;


    public FragmentHome() {
        // Required empty public constructor
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

        //이력서 등록화면으로 전환하기
        Button btnResume = (Button) rootView.findViewById(R.id.btn_resume);
        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ResumeActivity.class);
                startActivity(intent);
            }
        });

        //////진행중인 프로젝트 목록 보여주기
        adapter = new ListViewProjectAdapter(getContext(), android.R.layout.simple_list_item_multiple_choice, this);
        listView = (ListView) rootView.findViewById(R.id.list_myproject);
        listView.setAdapter(adapter);

        selectApplyOnFirebase();
        return rootView;
    }

    /////////
    public interface MyDataCallback{
        void onCallback();
    }

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

    public void selectProjectlistOnFirebase(String key, final MyDataCallback callback){
        db.collection("project_list").document(key)
            .get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        ListItemProject item = new ListItemProject((HashMap)document.getData());
                        item.setProject_id(document.getId());
                        // item.setSelected(true);

                        arrayData.add(item);
                        // Toast.makeText(BookmarkActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                    }

                    callback.onCallback();
                }
            });
    }


    public void updateListView(){
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }
}
package kr.ac.kookmin.makeit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import static kr.ac.kookmin.makeit.MainActivity.db;

/**
 * @file FragmentNotice
 * @desc 현재 팀원 모집중인 프로젝트 리스트를 보여주는 화면(프래그먼트)
 * @auther 윤서영(20191633)
 * @date 2020-11-01
 */
public class FragmentNotice extends Fragment {

    private ListViewProjectAdapter adapter;
    ListView listview;
    EditText editSearch;

    private ArrayList<ListItemProject> arrayData = new ArrayList<>();

    public interface MyDataCallback {
        void onCallback();
    }


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentNotice() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FragmentNotice newInstance(String param1, String param2) {
        FragmentNotice fragment = new FragmentNotice();
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
    public void startActivityForResult(Intent intent, int requestCode) {
        if(requestCode == 1){
            Toast.makeText(getContext(), "test", Toast.LENGTH_SHORT).show();
            selectQueryOnFirebase(new MyDataCallback() {
                @Override
                public void onCallback() {
                    selectBookmarkOnFirebase();
                }
            });
        }
        super.startActivityForResult(intent, requestCode);
    }

    // 프로젝트 등록 후에 파이어베이스에서 프로젝트 리스트 재 조회 -> ListView 새로고침 효과
    @Override
    public void onResume() {
        selectQueryOnFirebase(new MyDataCallback() {
            @Override
            public void onCallback() {
                selectBookmarkOnFirebase();
            }
        });
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_notice, container, false);

        // ListView 데이터 불러오기
        adapter = new ListViewProjectAdapter(getActivity(), android.R.layout.simple_list_item_multiple_choice);

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) rootView.findViewById(R.id.listv_project);
        listview.setAdapter(adapter);

        // 검색창
        editSearch = (EditText) rootView.findViewById(R.id.edit_query);
        ImageButton btnSearch = (ImageButton) rootView.findViewById(R.id.btn_query);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editSearch.getText().toString();

                // 검색결과 없음은 일단 GONE처리함.
                TextView resultSearch = (TextView) rootView.findViewById(R.id.text_result_search);
                if(resultSearch.getVisibility() == View.VISIBLE){
                    resultSearch.setVisibility(View.GONE);
                }

                // 아무것도 입력하지 않았을 때는 원래 화면을 보여줌.
                if(text.length() == 0) {
                    adapter.clear();
                    adapter.setAll(arrayData);
                    updateListView();
                    return;
                }

                // text를 모두 소문자로 변경(대소문자 신경안쓰고 검색하기 위함)
                text = text.toLowerCase();

                // 제목이나 내용 위주의 검색을 수행함.
                ArrayList<ListItemProject> tmpData = new ArrayList<>();
                for(ListItemProject item : arrayData){
                    // 검색 결과가 0일 때에 대한 예외처리
                    if(item != null){
                        if(item.getTitle().toLowerCase().contains(text) || item.getContent().toLowerCase().contains(text))
                            tmpData.add(item);
                    }

                }

                // 검색결과가 없으면, 없다고 표시해줌.
                if(tmpData.size() == 0){
                    resultSearch.setVisibility(View.VISIBLE);
                }

                // ListView update
                adapter.clear();
                adapter.setAll(tmpData);
                updateListView();
            }
        });


        // Floating버튼 이벤트 추가(add project)
        FloatingActionButton btnAdd = (FloatingActionButton) rootView.findViewById(R.id.btn_add_project);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RegisterProjectActivity.class);
                startActivity(intent);
            }
        });

        // 프로젝트 데이터를 먼저 가져온 후에 찜 표시.
        selectQueryOnFirebase(new MyDataCallback() {
            @Override
            public void onCallback() {
                selectBookmarkOnFirebase();
            }
        });


        return rootView;
    }

    public void selectQueryOnFirebase(final MyDataCallback callback){
        // Firebase 연동
        // 프로젝트 리스트를 긁어서 보여준다.
        // Collection(=DB) -> Document(=row)으로 구성되어있으며, column은 getData로 Map형태로 가져올 수 있다.

        CollectionReference collRef = db.collection("project_list");
        Query query = collRef.orderBy("upload_date", Query.Direction.DESCENDING);   // 내림차순 정렬(최신순으로 정렬한다)
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                arrayData.clear();
                if (task.isSuccessful()) {
                    // 각 row(=document)를 가져오고, getData를 통해 column 데이터를 가져오게 된다.
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ListItemProject item = new ListItemProject((HashMap)document.getData());
                        item.setProject_id(document.getId());   // 고유 document id는 primary key의 역할을 수행함.

                        // 진행 중인 프로젝트만 보여줄 수 있게 설정.
                        if(!item.isFinished())
                            arrayData.add(item);
                    }

                    // ListView update
                    adapter.clear();
                    adapter.setAll(arrayData);
                    updateListView();

                } else {
                    Log.d("notice", "get failed with ", task.getException());
                }

                callback.onCallback();
            }
        });

    }


    public void selectBookmarkOnFirebase() {

        for(int i=0; i<adapter.getCount(); i++){
            final ListItemProject item = (ListItemProject) adapter.getItem(i);
            final String project_id = item.getProject_id();
            final String id = SaveSharedPreference.getUserName(getContext());

            // Firebase 연동
            // 찜 리스트를 긁어서 보여준다.
            // Collection(=DB) -> Document(=row)으로 구성되어있으며, column은 getData로 Map형태로 가져올 수 있다.
            DocumentReference docRef = db.collection("bookmark").document(id);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            BookmarkInfo mark = new BookmarkInfo((HashMap) document.getData());
                            HashMap<String, Boolean> projectMap = mark.getProjectMap();

                            if (mark.getId().equals(id) && projectMap.containsKey(project_id)) {
                                item.setSelected(projectMap.get(project_id));     // 버튼 찜 설정
                            }
                        }
                        updateListView();

                    } else {
                        Log.d("bookmark", "get failed with ", task.getException());
                    }


                }
            });

        }

    }


    public void updateListView() {
        adapter.notifyDataSetChanged();
        listview.setAdapter(adapter);
    }

}
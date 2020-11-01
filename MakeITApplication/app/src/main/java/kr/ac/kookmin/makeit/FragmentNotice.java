package kr.ac.kookmin.makeit;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static kr.ac.kookmin.makeit.MainActivity.db;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentNotice#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentNotice extends Fragment {

    private ListViewProjectAdapter adapter;
    ListView listview;
    EditText editSearch;


    private ArrayList<ListItemProject> arrayData = new ArrayList<>();



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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_notice, container, false);

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

                // 아무것도 입력하지 않았을 때는 원래 화면을 보여줌.
                if(text.length() == 0) {
                    adapter.clear();
                    adapter.setAll(arrayData);
                    updateListView();
                    return;
                }


                // 제목이나 내용 위주의 검색을 수행함.
                ArrayList<ListItemProject> tmpData = new ArrayList<>();
                for(ListItemProject item : arrayData){
                    if(item.getTitle().contains(text) || item.getContent().contains(text))
                        tmpData.add(item);
                }

                // ListView update
                adapter.clear();
                adapter.setAll(tmpData);
                updateListView();
            }
        });


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
                        // 진행 중인 프로젝트만 보여줄 수 있게 설정.
                        if(!item.isFinished())
                            arrayData.add(item);

                    }

                    // ListView update
                    adapter.clear();
                    adapter.addAll(arrayData);
                    updateListView();

                    // Toast.makeText(getContext(), adapter.getCount()+"", Toast.LENGTH_LONG).show();
                } else {
                    Log.d("notice", "get failed with ", task.getException());
                }
            }
        });


        return rootView;
    }


    public void updateListView() {
        adapter.notifyDataSetChanged();
        listview.setAdapter(adapter);
    }
}
package kr.ac.kookmin.makeit;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import static kr.ac.kookmin.makeit.MainActivity.db;

/**
 * @file BookmarkActivity
 * @desc 찜한 프로젝트 목록을 보기위한 Activity
 * @auther 윤서영(20191633)
 * @date 2020-11-02
 */

public class BookmarkActivity extends AppCompatActivity {

    private ArrayList<ListItemProject> arrayData = new ArrayList<>();
    private ListViewBookmarkAdapter adapter;
    ListView listview;

    public interface MyDataCallback {
        void onCallback();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        // ListView 데이터 불러오기
        adapter = new ListViewBookmarkAdapter(this, android.R.layout.simple_list_item_multiple_choice);

        // 리스트뷰 참조 및 Adapter 설정
        listview = (ListView) findViewById(R.id.list_bookmark_project);
        listview.setAdapter(adapter);

        selectBookmartOnFirebase();
    }


    public void selectBookmartOnFirebase(){
        // Firebase 연동
        // 프로젝트 리스트를 긁어서 보여준다.
        // Collection(=DB) -> Document(=row)으로 구성되어있으며, column은 getData로 Map형태로 가져올 수 있다.
        String id = SaveSharedPreference.getUserName(this);
        final DocumentReference docRef = db.collection("bookmark").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    arrayData.clear();

                    if (document.exists()) {
                        BookmarkInfo mark = new BookmarkInfo((HashMap) document.getData());
                        HashMap<String, Boolean> projectMap = mark.getProjectMap();

                        for(String key : projectMap.keySet()){



                            if(projectMap.get(key)){
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
                    }

                } else {
                    Log.d("bookmark", "get failed with ", task.getException());
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
                        item.setSelected(true);

                        arrayData.add(item);
                        // Toast.makeText(BookmarkActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                    }

                    callback.onCallback();
                }
            });
    }


    public void updateListView() {
        adapter.notifyDataSetChanged();
        listview.setAdapter(adapter);
    }
}
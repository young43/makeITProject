package kr.ac.kookmin.makeit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static kr.ac.kookmin.makeit.MainActivity.db;

/**
 * @file ListViewProjectAdapter
 * @desc 프로젝트 리스트 화면(FragmentNotice)에서 ListView에 연결하기위한 Adapter클래스
 * @auther 김지홍(20191572), 윤서영(20191633)
 * @date 2020-11-01
 */

public class ListViewProjectAdapter extends ArrayAdapter {
    Fragment currentFragment;
    Button btnBookmark;

    private String id, project_id;

    private ArrayList<ListItemProject> listViewItemList = new ArrayList<ListItemProject>() ;

    public ListViewProjectAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public ListViewProjectAdapter(@NonNull Context context, int resource, Fragment fragment) {
        super(context, resource);
        currentFragment = fragment;
    }


    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listproject_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView titleTextView = (TextView) convertView.findViewById(R.id.text_project_title);
        TextView regionTextView = (TextView) convertView.findViewById(R.id.text_project_region);
        TextView contentTextView = (TextView) convertView.findViewById(R.id.text_content);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.text_date);
        TextView memberCntTextView = (TextView) convertView.findViewById(R.id.text_memberCnt);



        // 레이아웃 전체를 가져옴(clickable)
        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.list_item_layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ListItemProject listItem = listViewItemList.get(position);
                HashMap<String, Object> map = (HashMap)listItem.toMap();

                // 프로젝트 자세한 내용을 보기위해 ProjectInfoActivity 액티비티로 전환
                // map데이터 전체를 보냄.
                Intent intent = new Intent(context, ProjectInfoActivity.class);
                intent.putExtra("projectInfo", map);
                context.startActivity(intent);
            }
        });

        layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final String currentId = SaveSharedPreference.getUserName(getContext());
                final ListItemProject item = listViewItemList.get(position);
                final String pm_id = item.getPm_id();
                final String project_id = item.getProject_id();

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("프로젝트 삭제");
                builder.setMessage("프로젝트를 삭제하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                // 지원프로젝트 목록에서 삭제할 때는 단순히 apply에서만 삭제한다.
                                if(currentFragment instanceof FragmentHome){
                                    // 북마크 목록에서도 제거해줘야함. (안그러면 에러발생)
                                    Map<String, Object> updates = new HashMap<>();
                                    updates.put(project_id, FieldValue.delete());

                                    db.collection("apply").document(currentId)
                                        .update(updates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                listViewItemList.remove(item);
                                                notifyDataSetChanged();
                                                Log.d("delete", "DocumentSnapshot successfully deleted!");
                                            }
                                        });

                                } else{
                                    // 현재 로그인 사용자와 PM_ID가 같은 지 확인. (그 외 화면에서는 진짜 프로젝트 리스트를 삭제함.)
                                    if(currentId.equals(pm_id)){
                                        db.collection("project_list").document(project_id)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("delete", "DocumentSnapshot successfully deleted!");
                                                    listViewItemList.remove(item);
                                                    notifyDataSetChanged();


                                                    // 북마크 목록에서도 제거해줘야함. (안그러면 에러발생)
                                                    Map<String, Object> updates = new HashMap<>();
                                                    updates.put(project_id, FieldValue.delete());

                                                    db.collection("bookmark").document(currentId)
                                                        .update(updates)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Log.d("delete", "DocumentSnapshot successfully deleted!");
                                                            }
                                                        });
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("delete", "Error deleting document", e);
                                                }
                                            });



                                    }else{
                                        Toast.makeText(getContext(),"프로젝트 주인이 아닙니다!(삭제불가)",Toast.LENGTH_LONG).show();
                                    }

                                }
                            }
                        });
                builder.setNegativeButton("아니오", null);
                builder.show();


                return false;
            }
        });


        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final ListItemProject listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        titleTextView.setText(listViewItem.getTitle());
        regionTextView.setText(listViewItem.getRegion());
        contentTextView.setText(listViewItem.getContent());
        dateTextView.setText(listViewItem.getTimestamp().toString());
        memberCntTextView.setText(listViewItem.getMemberCnt()+"");  // Integer -> String형변환


        btnBookmark = (Button) convertView.findViewById(R.id.btn_bookmark);
        // Home화면의 경우, 찜하기 버튼 보이지 않게 설정함.
        if(currentFragment instanceof FragmentHome)
            btnBookmark.setVisibility(View.GONE);

        // 찜 db에 넣을 데이터 형성
        id = SaveSharedPreference.getUserName(getContext());

        // 찜하기 버튼 이벤트 설정
        btnBookmark.setSelected(listViewItem.isSelected());
        btnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                ListItemProject item = listViewItemList.get(position);
                project_id = item.getProject_id();

                boolean flag = !button.isSelected();
                button.setSelected(flag);
                item.setSelected(flag);

                Map<String, Object> data = new HashMap<>();
                data.put("id", id);
                data.put(project_id, flag);

                // 찜 목록 데이터 업데이트
                db.collection("bookmark").document(id)
                    .set(data, SetOptions.merge())  // merge옵션으로 update 및 추가 가능
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.e("bookmark", "Success update data on firebase");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("bookmark", "Error update data", e);
                        }
                    });
            }
        });

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }


    public void addAll(ArrayList<ListItemProject> items) {
        listViewItemList.addAll(items);
        // listViewItemList.addAll(Arrays.asList(items));
        // super.addAll(items);
    }

    public void setAll(ArrayList<ListItemProject> items){
        listViewItemList = (ArrayList<ListItemProject>)items.clone();   // 깊은 복사
    }

    public void removeItem(int position){
        Log.e("testActivity", listViewItemList.get(position).getTitle());
        listViewItemList.remove(position);
    }

}

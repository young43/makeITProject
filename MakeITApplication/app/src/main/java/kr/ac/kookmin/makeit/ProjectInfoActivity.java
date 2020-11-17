package kr.ac.kookmin.makeit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import static kr.ac.kookmin.makeit.MainActivity.db;

/**
 * @file ProjectInfoActivity
 * @desc 각 프로젝트가 클릭되었을 때 보여지는 프로젝트 info 화면(Activity)
 * @auther 김찬미(20191574), 윤서영(20191633)
 * @date 2020-11-01
 */

public class ProjectInfoActivity extends AppCompatActivity {

    HashMap<String, Object> data;
    TextView textTitle, textContent, textPhone, textEmail, textRegion, textMember, textDate;

    public interface MyDataCallback {
        void onCallback(boolean exists, ResumeInfo info);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_info);


        Intent intent = getIntent();
        data = (HashMap) intent.getSerializableExtra("projectInfo");

        // TextView 데이터 매칭
        textTitle = (TextView) findViewById(R.id.text_project_title);
        textContent = (TextView) findViewById(R.id.text_project_content);
        textPhone = (TextView) findViewById(R.id.text_project_phone);
        textEmail = (TextView) findViewById(R.id.text_project_email);
        textRegion = (TextView) findViewById(R.id.text_project_region);
        textMember = (TextView) findViewById(R.id.text_project_member);
        textDate = (TextView) findViewById(R.id.text_project_date);

        textTitle.setText((String)data.get("title"));
        textContent.setText((String)data.get("content"));
        textPhone.setText((String)data.get("phonenumber"));
        textEmail.setText((String)data.get("email"));
        textRegion.setText((String)data.get("region"));
        textMember.setText(String.valueOf(data.get("person")));  // person은 number형(=long)형이다.
        textDate.setText((String)data.get("upload_date"));

        // 스크롤 가능하게 설정
        textContent.setMovementMethod(new ScrollingMovementMethod());

        // 프로젝트 지원 버튼 이벤트 설정
        // 자신의 이력서 내용을 바탕으로 이메일 작성
        Button btnJoin = (Button) findViewById(R.id.btn_join_project);
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // 이력서 내용 조회
                selectQueryOnFirebase(new MyDataCallback() {
                    @Override
                    public void onCallback(boolean exists, ResumeInfo info) {
                        // 메일 관련 내용
                        String receiver = (String)data.get("email");
                        String title = (String)data.get("title") + " 지원합니다.";
                        String resume = "";

                        if(exists){
                            resume += "이름: " + info.getResume_name() + "\n"
                                    + "이메일: " + info.getResume_email() + "\n"
                                    + "연락처: " + info.getResume_phone() + "\n\n"
                                    + "[자기소개]" + "\n"
                                    + info.getResume_introduction() + "\n\n";

                            if(info.getResume_company1().length() > 0){
                                resume += "[경력1]" + "\n"
                                        + info.getResume_company1() + ") " + info.getResume_company_date1() + "\n\n";
                            }

                            if(info.getResume_company2().length() > 0){
                                resume += "[경력2]" + "\n"
                                        + info.getResume_company2() + ") " + info.getResume_company_date2() + "\n\n";
                            }

                            if(info.getResume_company3().length() > 0){
                                resume += "[경력3]" + "\n"
                                        + info.getResume_company3() + ") " + info.getResume_company_date3() + "\n\n";
                            }
                        }


                        // Firebase 지원 collection 데이터 추가
                        String id = SaveSharedPreference.getUserName(ProjectInfoActivity.this);  // 현재 로그인된 id 가져옴
                        String project_id = (String)data.get("project_id");
                        Map<String, Object> apply = new HashMap<>();
                        apply.put("id", id);
                        apply.put(project_id, true);

                        // 찜 목록 데이터 업데이트
                        db.collection("apply").document(id)
                            .set(apply, SetOptions.merge())  // merge옵션으로 update 및 추가 가능
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.e("apply", "Success update data on firebase");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("apply", "Error update data", e);
                                }
                            });

                        Intent email = new Intent(Intent.ACTION_SEND);
                        email.setType("plain/Text");
                        email.putExtra(Intent.EXTRA_EMAIL, new String[] { receiver });     // 받는사람 설정
                        email.putExtra(Intent.EXTRA_SUBJECT, title);
                        email.putExtra(Intent.EXTRA_TEXT, resume);
                        email.setType("message/rfc822");
                        startActivity(email);

                    }
                });



            }
        });

    }

    public void selectQueryOnFirebase(final MyDataCallback callback){
        // Firebase 연동
        // 프로젝트 리스트를 긁어서 보여준다.
        // Collection(=DB) -> Document(=row)으로 구성되어있으며, column은 getData로 Map형태로 가져올 수 있다.
        String id = SaveSharedPreference.getUserName(ProjectInfoActivity.this);

        db.collection("resume").document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if(document.getData() != null){
                                ResumeInfo info = new ResumeInfo((HashMap) document.getData());
                                callback.onCallback(true, info);
                            }else{
                                callback.onCallback(false, null);
                            }

                        }else{
                            callback.onCallback(false, null);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onCallback(false, null);
                    }
                });

    }
}
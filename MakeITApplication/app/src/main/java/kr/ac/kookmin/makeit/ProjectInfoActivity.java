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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import static kr.ac.kookmin.makeit.MainActivity.db;

/**
 * @file ProjectInfoActivity
 * @desc 각 프로젝트가 클릭되었을 때 보여지는 프로젝트 info 화면(Activity)
 * @auther 윤서영(20191633)
 * @date 2020-11-01
 */

public class ProjectInfoActivity extends AppCompatActivity {

    HashMap<String, Object> data;
    TextView textTitle, textContent, textPhone, textEmail, textRegion, textMember, textDate;

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
                // Firebase 지원 collection에 추가
                String id = SaveSharedPreference.getUserName(ProjectInfoActivity.this);
                String project_id = (String)data.get("project_id");

                Map<String, Object> data = new HashMap<>();
                data.put("id", id);
                data.put(project_id, true);

                // 찜 목록 데이터 업데이트
                db.collection("apply").document(id)
                        .set(data, SetOptions.merge())  // merge옵션으로 update 및 추가 가능
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



                // 자동으로 메일 연동
                String receiver = (String)data.get("email");
                String title = (String)data.get("title") + " 지원합니다.";
                String text = "이력서 내용";     // 이력서는 나중에 파이어베이스에서 데이터 조회하는 것으로 설정.

                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("plain/Text");
                email.putExtra(Intent.EXTRA_EMAIL, new String[] { receiver });     // 받는사람 설정
                email.putExtra(Intent.EXTRA_SUBJECT, title);
                email.putExtra(Intent.EXTRA_TEXT, text);
                email.setType("message/rfc822");
                startActivity(email);

            }
        });

    }
}
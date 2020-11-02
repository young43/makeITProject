package kr.ac.kookmin.makeit;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static kr.ac.kookmin.makeit.MainActivity.db;


/**
 * @file RegisterProjectActivity
 * @desc 프로젝트를 등록하기 위한 Activity
 * @auther 윤서영(20191633)
 * @date 2020-11-02
 */

public class RegisterProjectActivity extends AppCompatActivity {

    EditText editTitle, editContent, editPhone, editEmail;
    Spinner spinnerRegion, spinnerMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_project);

        // Spinner 아이템 설정
        // 지역(region), 인원수(member)
        spinnerRegion = (Spinner)findViewById(R.id.spinner_project_region);
        ArrayAdapter regionAdapter = ArrayAdapter.createFromResource(this, R.array.region, android.R.layout.simple_spinner_item);
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRegion.setAdapter(regionAdapter);
        spinnerRegion.setSelection(0);

        spinnerMember = (Spinner)findViewById(R.id.spinner_project_member);
        List<String> spinnerArray =  new ArrayList<String>();
        // 최대인원 15명까지
        for(int i=1; i<=15; i++)
            spinnerArray.add(String.valueOf(i));
        ArrayAdapter<String> memberAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);
        memberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMember.setAdapter(memberAdapter);
        spinnerMember.setSelection(0);

        // editTex매칭
        editTitle = (EditText) findViewById(R.id.edit_project_title);
        editContent = (EditText) findViewById(R.id.edit_project_content);
        editPhone = (EditText) findViewById(R.id.edit_project_phone);
        editEmail = (EditText) findViewById(R.id.edit_project_email);

        // 프로젝트 등록 이벤트 설정
        Button btnRegister = (Button) findViewById(R.id.btn_register_project);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pm_id = SaveSharedPreference.getUserName(RegisterProjectActivity.this);  // 현재 로그인된 id 가져옴
                String title = editTitle.getText().toString().trim();
                String content = editContent.getText().toString().trim();
                String phone = editPhone.getText().toString().trim();
                String email = editEmail.getText().toString().trim();
                String region = spinnerRegion.getSelectedItem().toString();
                long member = Long.parseLong(spinnerMember.getSelectedItem().toString());
                boolean finish = false;

                Date date = new Date();
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
                String now = format1.format(date);

                //String title, String content, String region, long memberCnt, String email, String phoneNumber, boolean isFinished, String pm_id, String timestamp
                ListItemProject project = new ListItemProject(title, content, region, member, email, phone, finish, pm_id, now);
                HashMap<String, Object> data = (HashMap)project.toMap();
                data.put("upload_date", date);  // 파이어베이스는 Date형으로 넣어주어야함

                // 파이어베이스 데이터(row) 추가
                db.collection("project_list")
                    .add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("register", "DocumentSnapshot written with ID: " + documentReference.getId());
                            Toast.makeText(RegisterProjectActivity.this, "정상적으로 등록되었습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("register", "Error adding document", e);
                        }
                    });


            }
        });


    }



}
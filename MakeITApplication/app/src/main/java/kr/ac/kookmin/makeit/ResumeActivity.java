package kr.ac.kookmin.makeit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;

import static kr.ac.kookmin.makeit.MainActivity.db;

/**
 * @file ResumeActivity
 * @desc 이력서를 등록하기 위한 Activity
 * @auther 김찬미(20191574)
 * @date 2020-11-12
 */

public class ResumeActivity extends AppCompatActivity {

    EditText editName, editEmail, editPhone, editIntroduction, editCompany1, editCompany2, editCompany3,
                editInYear1, editInYear2, editInYear3, editOutYear1, editOutYear2, editOutYear3,
                editInMonth1, editInMonth2, editInMonth3, editOutMonth1, editOutMonth2, editOutMonth3;


    public interface MyDataCallback {
        void onCallback(boolean exists, ResumeInfo info);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);


        selectQueryOnFirebase(new MyDataCallback() {
            @Override
            public void onCallback(boolean exists, ResumeInfo info) {
                // 이력서에 대한 정보가 있으면 화면에 표시해줌.
                if(exists){
                    editName.setText(info.getResume_name());
                    editEmail.setText(info.getResume_email());
                    editPhone.setText(info.getResume_phone());
                    editIntroduction.setText(info.getResume_introduction());
                    editCompany1.setText(info.getResume_company1());
                    editCompany2.setText(info.getResume_company2());
                    editCompany3.setText(info.getResume_company3());

                    // 날짜형식: 201909/202010 -> 파싱필요
                    if(info.getResume_company_date1().length() > 1 && info.getResume_company_date1().split("/")[0].length() == 6){
                        String inyear1 = info.getResume_company_date1().split("/")[0].substring(0, 4);
                        String inmonth1 = info.getResume_company_date1().split("/")[0].substring(4, 6);

                        editInYear1.setText(inyear1);
                        editInMonth1.setText(inmonth1);
                    }

                    if(info.getResume_company_date2().length() > 2 && info.getResume_company_date2().split("/")[0].length() == 6){
                        String inyear2 = info.getResume_company_date2().split("/")[0].substring(0, 4);
                        String inmonth2 = info.getResume_company_date2().split("/")[0].substring(4, 6);

                        editInYear2.setText(inyear2);
                        editInMonth2.setText(inmonth2);
                    }

                    if(info.getResume_company_date3().length() > 2 && info.getResume_company_date3().split("/")[0].length() == 6){
                        String inyear3 = info.getResume_company_date3().split("/")[0].substring(0, 4);
                        String inmonth3 = info.getResume_company_date3().split("/")[0].substring(4, 6);

                        editInYear3.setText(inyear3);
                        editInMonth3.setText(inmonth3);
                    }

                    if(info.getResume_company_date1().length() > 2 && info.getResume_company_date1().split("/")[1].length() == 6){
                        String outyear1 = info.getResume_company_date1().split("/")[1].substring(0, 4);
                        String outmonth1 = info.getResume_company_date1().split("/")[1].substring(4, 6);

                        editOutYear1.setText(outyear1);
                        editOutMonth1.setText(outmonth1);
                    }

                    if(info.getResume_company_date2().length() > 2 && info.getResume_company_date2().split("/")[1].length() == 6){
                        String outyear2 = info.getResume_company_date2().split("/")[1].substring(0, 4);
                        String outmonth2 = info.getResume_company_date2().split("/")[1].substring(4, 6);

                        editOutYear2.setText(outyear2);
                        editOutMonth2.setText(outmonth2);
                    }

                    if(info.getResume_company_date3().length() > 2 && info.getResume_company_date3().split("/")[1].length() == 6){
                        String outyear3 = info.getResume_company_date3().split("/")[1].substring(0, 4);
                        String outmonth3 = info.getResume_company_date3().split("/")[1].substring(4, 6);

                        editOutYear3.setText(outyear3);
                        editOutMonth3.setText(outmonth3);
                    }


                }

                onRestart();
            }
        });


        //editText 매칭
        editName = (EditText) findViewById(R.id.edit_resume_name);
        editEmail = (EditText) findViewById(R.id.edit_resume_email);
        editPhone = (EditText) findViewById(R.id.edit_resume_phone);
        editIntroduction = (EditText) findViewById(R.id.edit_resume_introduction);
        editCompany1 = (EditText) findViewById(R.id.resume_company_name1);
        editCompany2 = (EditText) findViewById(R.id.resume_company_name2);
        editCompany3 = (EditText) findViewById(R.id.resume_company_name3);
        editInYear1 = (EditText) findViewById(R.id.resume_in_year1);
        editInYear2 = (EditText) findViewById(R.id.resume_in_year2);
        editInYear3 = (EditText) findViewById(R.id.resume_in_year3);
        editOutYear1 = (EditText) findViewById(R.id.resume_out_year1);
        editOutYear2 = (EditText) findViewById(R.id.resume_out_year2);
        editOutYear3 = (EditText) findViewById(R.id.resume_out_year3);
        editInMonth1 = (EditText) findViewById(R.id.resume_in_month1);
        editInMonth2 = (EditText) findViewById(R.id.resume_in_month2);
        editInMonth3 = (EditText) findViewById(R.id.resume_in_month3);
        editOutMonth1 = (EditText) findViewById(R.id.resume_out_month1);
        editOutMonth2 = (EditText) findViewById(R.id.resume_out_month2);
        editOutMonth3 = (EditText) findViewById(R.id.resume_out_month3);

        //이력서 등록 이벤트
        Button btnResume = (Button) findViewById(R.id.btn_register_resume);
        btnResume.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String id = SaveSharedPreference.getUserName(ResumeActivity.this);  // 현재 로그인된 id 가져옴
                String name = editName.getText().toString().trim();
                String email = editEmail.getText().toString().trim();
                String phone = editPhone.getText().toString().trim();
                String introduction = editIntroduction.getText().toString().trim();
                String company1 = editCompany1.getText().toString().trim();
                String company2 = editCompany2.getText().toString().trim();
                String company3 = editCompany3.getText().toString().trim();
                String date1 = editInYear1.getText().toString() + editInMonth1.getText().toString() + "/" + editOutYear1.getText().toString() + editOutMonth1.getText().toString();
                String date2 = editInYear2.getText().toString() + editInMonth2.getText().toString() + "/" + editOutYear2.getText().toString() + editOutMonth2.getText().toString();
                String date3 = editInYear3.getText().toString() + editInMonth3.getText().toString() + "/" + editOutYear3.getText().toString() + editOutMonth3.getText().toString();

                // 날짜 유효성 체크
                if(date1.length() > 2 && !(date1.length() == 7 || date1.length() == 13)){
                    Toast.makeText(ResumeActivity.this, "날짜 입력 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(date2.length() > 2 && !(date2.length() == 7 || date2.length() == 13)){
                    Toast.makeText(ResumeActivity.this, "날짜 입력 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(date3.length() > 2 && !(date3.length() == 7 || date3.length() == 13)){
                    Toast.makeText(ResumeActivity.this, "날짜 입력 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                ResumeInfo resume = new ResumeInfo(id, name, email, phone, introduction, company1, company2, company3, date1, date2, date3);
                HashMap<String, Object> data = (HashMap)resume.toMap();

                // Firebase 연동 및 resume콜렉션 데이터 추가
                db.collection("resume").document(id)
                    .set(data, SetOptions.merge())  // merge옵션으로 update 및 추가 가능
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.e("resume", "Success update data on firebase");
                            Toast.makeText(ResumeActivity.this, "정상적으로 등록되었습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("resume", "Error update data", e);
                        }
                    });

            }
        });
    }


    public void selectQueryOnFirebase(final MyDataCallback callback){
        // Firebase 연동
        // 프로젝트 리스트를 긁어서 보여준다.
        // Collection(=DB) -> Document(=row)으로 구성되어있으며, column은 getData로 Map형태로 가져올 수 있다.
        String id = SaveSharedPreference.getUserName(ResumeActivity.this);

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
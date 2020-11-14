package kr.ac.kookmin.makeit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);

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
            }
        });
    }



}
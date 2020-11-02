package kr.ac.kookmin.makeit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin, btnJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 자동 로그인 기능
        // SaveSharedPreference.setUserName(this, editId.getText().toString());

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 회원조회 및 패스워드 확인작업 필요
                // 임시 id로그인
                String tmpId = "root";
                // String tmpPw = "1234";

                SaveSharedPreference.setUserName(LoginActivity.this, tmpId);   // 자동 로그인 기능

                // Main화면으로 Intent 전환
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
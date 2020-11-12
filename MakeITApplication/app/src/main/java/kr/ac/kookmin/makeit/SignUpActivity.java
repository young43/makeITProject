package kr.ac.kookmin.makeit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;

import static kr.ac.kookmin.makeit.MainActivity.db;

public class SignUpActivity extends AppCompatActivity {

    Button btn_register;
    EditText emailText, idText, passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btn_register = (Button) findViewById(R.id.btn_register);
        emailText = (EditText) findViewById(R.id.emailText);
        idText = (EditText) findViewById(R.id.idText);
        passwordText = (EditText) findViewById(R.id.passwordText);


        btn_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //회원가입 정보를 모두 입력했을 경우만 등록이 가능(정보를 모두 입력한 경우->EditText 길이가 5이상인 경우)
                if(emailText.getText().toString().length()>4 && idText.getText().toString().length()>4 && passwordText.getText().toString().length()>4){
                    // Toast.makeText(SignUpActivity.this, "회원 가입이 완료되었습니다😀", Toast.LENGTH_SHORT).show();
                    String id = idText.getText().toString().trim();
                    String pw = passwordText.getText().toString();
                    String email = emailText.getText().toString().trim();
                    String phone = "010";

                    //(String id, String passwd, String email, String phone)
                    UserInfo member = new UserInfo(id, pw, email, phone);
                    HashMap<String, Object> data = (HashMap) member.toMap();

                    // 파이어베이스 데이터(row) 추가
                    db.collection("member")
                        .add(data)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("member", "DocumentSnapshot written with ID: " + documentReference.getId());
                                Toast.makeText(SignUpActivity.this, "회원 가입이 완료되었습니다😀", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("member", "Error adding document", e);
                            }
                        });


                    //로그인 페이지로 이동
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

                //회원 가입 정보를 모두 입력하지 않은 경우
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setMessage("정보를 모두 입력해주세요🤔");
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });

    }
}
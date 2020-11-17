package kr.ac.kookmin.makeit;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

import static kr.ac.kookmin.makeit.MainActivity.db;

/**
 * @file LoginActivity
 * @desc 로그인 기능을 수행하는 Activity
 * @auther 김지홍(20191572)
 * @date 2020-11-12
 */

public class LoginActivity extends AppCompatActivity {
    int memberResult = 0;
    Button btnLogin, btnjoin;
    EditText editId, editPw;

    String loginId, loginPw;

    public interface MyDataCallback {
        void onCallback();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 자동 로그인 기능
        // SaveSharedPreference.setUserName(this, editId.getText().toString());

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnjoin = (Button) findViewById(R.id.btn_join);

        editId = (EditText) findViewById(R.id.idText);
        editPw = (EditText) findViewById(R.id.passwordText);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 임시 id로그인
                loginId = editId.getText().toString().trim();
                loginPw = editPw.getText().toString();

                // firebase에서 회원정보 조회
                selectUserInfoOnFirebase(loginId, loginPw, new MyDataCallback() {
                    @Override
                    public void onCallback() {

                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        AlertDialog alertDialog;


                        switch (memberResult){
                            case -1:
                                builder.setMessage("가입된 정보가 없습니다.");
                                alertDialog = builder.create();
                                alertDialog.show();
                                // Toast.makeText(LoginActivity.this, "가입된 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                                break;
                            case -2:
                                builder.setMessage("비밀번호가 틀렸습니다.");
                                alertDialog = builder.create();
                                alertDialog.show();
                                // Toast.makeText(LoginActivity.this, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                                break;
                            default:  // 로그인 성공
                                SaveSharedPreference.setUserName(LoginActivity.this, loginId);   // 자동 로그인 기능

                                // Main화면으로 Intent 전환
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                        }
                    }
                });


            }
        });

        btnjoin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //회원 가입 페이지로 이동
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }


    public void selectUserInfoOnFirebase(final String id, final String pw, final MyDataCallback callback){
        // memberResult(-1): 가입정보가 없음
        // memberResult(-2): 아이디는 있으나, 비밀번호가 틀림.

        // Firebase 연동
        // 회원정보 리스트를 긁어서 보여준다.
        // Collection(=DB) -> Document(=row)으로 구성되어있으며, column은 getData로 Map형태로 가져올 수 있다.
        CollectionReference collRef = db.collection("member");
        Query query = collRef.whereEqualTo("id", id);
        query.get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    memberResult = -1;
                    int length = task.getResult().size();
                    if(length > 0){
                        DocumentSnapshot data = task.getResult().getDocuments().get(0);
                        UserInfo item = new UserInfo((HashMap) data.getData());

                        if(item.getPasswd().equals(pw)) memberResult = 1;
                        else memberResult = -2;
                    }

                    callback.onCallback();
                }
            });

    }
}
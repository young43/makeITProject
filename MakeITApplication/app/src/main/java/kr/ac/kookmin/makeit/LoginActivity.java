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

    // 사용자 정의 callback 함수 (Firebase 데이터가 다 조회되었는지 check하는 역할)
    public interface MyDataCallback {
        void onCallback();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnjoin = (Button) findViewById(R.id.btn_join);

        editId = (EditText) findViewById(R.id.idText);
        editPw = (EditText) findViewById(R.id.passwordText);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 사용자가 입력한 아이디와 비밀번화 받아오기
                loginId = editId.getText().toString().trim();
                loginPw = editPw.getText().toString();

                // firebase에서 회원정보 조회
                selectUserInfoOnFirebase(loginId, loginPw, new MyDataCallback() {
                    @Override
                    public void onCallback() {

                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        AlertDialog alertDialog;


                        switch (memberResult){
                            case -1: // 회원 가입이 필요한 경우
                                builder.setMessage("가입된 정보가 없습니다.");
                                alertDialog = builder.create();
                                alertDialog.show();
                                break;
                            case -2: // 이미 존재하는 회원인 경우
                                builder.setMessage("비밀번호가 틀렸습니다.");
                                alertDialog = builder.create();
                                alertDialog.show();
                                break;
                            default:  // 로그인 성공한 경우
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

    // Firebase 연동
    // member 컬렉션을 조회한다.
    // Collection(=DB) -> Document(=row)으로 구성되어있으며, column은 getData로 Map형태로 가져올 수 있다.
    public void selectUserInfoOnFirebase(final String id, final String pw, final MyDataCallback callback){

        // memberResult(-1): 가입정보가 없음
        // memberResult(-2): 아이디는 있으나, 비밀번호가 틀림.
        CollectionReference collRef = db.collection("member");
        Query query = collRef.whereEqualTo("id", id);
        query.get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    memberResult = -1;
                    int length = task.getResult().size();
                    if(length > 0){  // 회원가입이 되어 있을 때
                        DocumentSnapshot data = task.getResult().getDocuments().get(0);
                        UserInfo item = new UserInfo((HashMap) data.getData());

                        if(item.getPasswd().equals(pw)) memberResult = 1;
                        else memberResult = -2;
                    }

                    callback.onCallback();  //firebase 조회한 다음, 알맞은 memberResult값 사용
                }
            });

    }
}
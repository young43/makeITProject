package kr.ac.kookmin.makeit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

import static kr.ac.kookmin.makeit.MainActivity.db;

public class LoginActivity extends AppCompatActivity {
    int memberResult = 0;
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
                // 임시 id로그인
                String tmpId = "seo5220";
                String tmpPw = "ysy5220";

                // firebase에서 회원정보 조회
                selectUserInfoOnFirebase(tmpId, tmpPw);
                switch (memberResult){
                    case -1:
                        Toast.makeText(LoginActivity.this, "가입된 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case -2:
                        Toast.makeText(LoginActivity.this, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    default:  // 로그인 성공
                        SaveSharedPreference.setUserName(LoginActivity.this, tmpId);   // 자동 로그인 기능

                        // Main화면으로 Intent 전환
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                }

            }
        });

    }


    public void selectUserInfoOnFirebase(final String id, final String pw){
        // memberResult(-1): 가입정보가 없음
        // memberResult(-2): 아이디는 있으나, 비밀번호가 틀림.

        // Firebase 연동
        // 회원정보 리스트를 긁어서 보여준다.
        // Collection(=DB) -> Document(=row)으로 구성되어있으며, column은 getData로 Map형태로 가져올 수 있다.
        CollectionReference collRef = db.collection("member");

        collRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                memberResult = -1;

                if (task.isSuccessful()) {
                    // 각 row(=document)를 가져오고, getData를 통해 column 데이터를 가져오게 된다.
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        UserInfo item = new UserInfo((HashMap)document.getData());

                        if(item.getId().equals(id) && item.getPasswd().equals(pw)){
                            memberResult = 1;       // 정상로그인
                            break;
                        }else if(item.getId().equals(id) && !item.getPasswd().equals(pw)){
                            memberResult = -2;      // 비밀번호가 틀림
                            break;
                        }
                    }

                } else {
                    Log.d("member", "get failed with ", task.getException());
                }
            }
        });


    }
}
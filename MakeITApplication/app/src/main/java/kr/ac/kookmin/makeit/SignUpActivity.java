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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

import static kr.ac.kookmin.makeit.MainActivity.db;

/**
 * @file SignUpActivity
 * @desc 회원가입 기능을 수행하는 Activity
 * @auther 김지홍(20191572)
 * @date 2020-11-12
 */

public class SignUpActivity extends AppCompatActivity {

    Button btn_register;
    EditText emailText, idText, passwordText;

    String id, pw, email, phone;

    // 사용자 정의 callback 함수 (Firebase 데이터가 다 조회되었는지 check하는 역할)
    public interface MyDataCallback {
        void onCallback(int result);
    }

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
                    id = idText.getText().toString().trim();
                    pw = passwordText.getText().toString();
                    email = emailText.getText().toString().trim();
                    phone = "010";

                    selectUserInfoOnFirebase(id, new MyDataCallback() {
                        @Override
                        public void onCallback(int result) {
                            // 이미 회원정보가 있을 경우 Toast 메시지 출력
                            if(result < 0){
                                Toast.makeText(SignUpActivity.this, "이미 회원정보가 존재합니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }


                            //(String id, String passwd, String email, String phone)
                            UserInfo member = new UserInfo(id, pw, email, phone);

                            // firebase에 데이터를 전달하기 위해, 데이터 map형태로 변환
                            HashMap<String, Object> data = (HashMap) member.toMap();

                            // firebase에 데이터(row) 추가
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
                    });


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


    // Firebase 연동
    // member 컬렉션을 조회한다.
    // 데이터가 다 조회되면 callback함수를 호출한다. 이때 회원정보가 없으면 0을 리턴하게 된다.
    public void selectUserInfoOnFirebase(final String id, final MyDataCallback callback){
        CollectionReference collRef = db.collection("member");
        Query query = collRef.whereEqualTo("id", id);
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    int flag = 0;
                    if(task.getResult().size() > 0)   // 이미 회원정보가 존재할 때
                        flag = -1;

                    callback.onCallback(flag); // firebase 조회한 다음, 알맞은 flag값 전달
                }
            });

    }
}
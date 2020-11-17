package kr.ac.kookmin.makeit;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * @file MainActivity
 * @desc 로그인 여부에 따라 보여지는 첫 화면을 결정하는 Activity
 * @auther 김찬미(20191574)
 * @date 2020-11-01
 */

public class MainActivity extends AppCompatActivity {

    // 파이어베이스 연동
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();

    private BottomNavigationView bottomNavigationView;

    private FragmentManager fm;
    private FragmentTransaction ft;

    private Fragment frag1;
    private Fragment frag2;
    private Fragment frag3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 기존에 로그인된 정보가 없으면 Login화면으로 이동
        if(SaveSharedPreference.getUserName(this).length() == 0) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        // bottomNavigaion View를 id를 통해서 가져옴
        bottomNavigationView = findViewById(R.id.bottomNavi);

        // 무슨 아이콘을 선택했냐에 따라서 보여주는 화면(Fragment)전환이 이루어짐.
        // setOnNavigationItemSelectedListener 이벤트 설정
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_main:
                        setFrag(0, null);
                        break;
                    case R.id.action_notice:
                        setFrag(1,null);
                        break;
                    case R.id.action_mypage:
                        setFrag(2, null);
                        break;
                }
                return true;
            }
        });

        frag1 = new FragmentHome();
        frag2 = new FragmentNotice();
        frag3 = new FragmentMypage();

        // 첫 화면은 FragmentHome으로 설정.
        // setFrag에 주는 정수에 따라서 프래그먼트 트랜지션이 이루어짐.
        setFrag(0, null); // 첫 프래그먼트 화면 지정
    }

    public void setFrag(int n, Fragment frag) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        frag1 = new FragmentHome();
        frag2 = new FragmentNotice();
        frag3 = new FragmentMypage();

        switch (n) {
            case 0: // 메인
                if(frag != null)
                    frag1 = frag;

                bottomNavigationView.getMenu().findItem(R.id.action_main).setChecked(true);
                ft.replace(R.id.Main_Frame, frag1);
                ft.commit();
                break;

            case 1: // 공고
                if(frag != null)
                    frag2 = frag;

                bottomNavigationView.getMenu().findItem(R.id.action_notice).setChecked(true);
                ft.replace(R.id.Main_Frame, frag2);
                ft.commit();
                break;

            case 2: // 마이페이지
                if(frag != null)
                    frag3 = frag;

                bottomNavigationView.getMenu().findItem(R.id.action_mypage).setChecked(true);
                ft.replace(R.id.Main_Frame, frag3);
                ft.commit();
                break;

        }

    }
}
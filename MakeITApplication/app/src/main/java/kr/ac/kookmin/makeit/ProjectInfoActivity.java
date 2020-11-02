package kr.ac.kookmin.makeit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.HashMap;

/**
 * @file ProjectInfoActivity
 * @desc 각 프로젝트가 클릭되었을 때 보여지는 프로젝트 info 화면(Activity)
 * @auther 윤서영(20191633)
 * @date 2020-11-01
 */

public class ProjectInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_info);


        Intent intent = getIntent();
        HashMap<String, Object> hashMap = (HashMap) intent.getSerializableExtra("projectInfo");

        String title = (String)hashMap.get("title");
        Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
    }
}
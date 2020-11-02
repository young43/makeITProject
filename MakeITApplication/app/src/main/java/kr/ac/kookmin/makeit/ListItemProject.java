package kr.ac.kookmin.makeit;

import android.graphics.drawable.Drawable;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



/**
 * @file ListItemProject
 * @desc 프로젝트 리스트 화면(FragmentNotice)에서 각 Item에 대한 데이터 클래스.
 * @auther 윤서영(20191633)
 * @date 2020-11-01
 */

// 현재 존재하는 프로젝트 리스트에 대한 item 클래스
// 파이어베이스 연동을 위해 dao형태로도 사용됨.
public class ListItemProject {
    private long id;    // 고유 식별자
    private String title;
    private String content;
    private String region;
    private long memberCnt;
    private String email;
    private String phoneNumber;
    private String timestamp;
    private boolean isFinished;     // 프로젝트가 끝났는 지 여부
    private String pm_id;           // 프로젝트 매니저 아이디



    public ListItemProject(){
    }


    public ListItemProject(HashMap<String, Object> map) {
        this.id = (Long)map.get("id");
        this.title = (String)map.get("title");
        this.content = (String)map.get("content");
        this.region = (String)map.get("region");
        this.memberCnt = (Long) map.get("person");
        this.email = (String)map.get("email");
        this.phoneNumber = (String)map.get("phoneNumber");
        this.isFinished = (Boolean)map.get("finish");
        this.pm_id = (String)map.get("pm_id");

        // Timestamp -> Date -> String 형변환
        Date tmpDate = ((Timestamp)map.get("upload_date")).toDate();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        this.timestamp =  format.format(tmpDate);
    }

    // 파이어베이스 연동을 위해 dao 형태로 만들어줌
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("title", title);
        result.put("content", content);
        result.put("region", region);
        result.put("memberCnt", memberCnt);
        result.put("email", email);
        result.put("phoneNumber", phoneNumber);
        result.put("timestamp", timestamp);
        result.put("finish", isFinished);
        result.put("pm_id", pm_id);

        return result;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public long getMemberCnt() {
        return memberCnt;
    }

    public void setMemberCnt(long memberCnt) {
        this.memberCnt = memberCnt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public String getPm_id() {
        return pm_id;
    }

    public void setPm_id(String pm_id) {
        this.pm_id = pm_id;
    }
}

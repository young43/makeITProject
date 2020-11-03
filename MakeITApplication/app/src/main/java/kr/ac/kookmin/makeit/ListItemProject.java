package kr.ac.kookmin.makeit;

import com.google.firebase.Timestamp;

import java.text.ParseException;
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
    private String title;
    private String content;
    private String region;
    private long memberCnt;
    private String email;
    private String phoneNumber;
    private String timestamp;
    private boolean isFinished;     // 프로젝트가 끝났는 지 여부
    private String pm_id;           // 프로젝트 매니저 아이디(=프로젝트를 등록한 사람 아이디)
    private String project_id;

    private boolean selected;       // 찜버튼

    public ListItemProject(){}

    public ListItemProject(String title, String content, String region, long memberCnt, String email, String phoneNumber, boolean isFinished, String pm_id, String timestamp){
        this.title = title;
        this.content = content;
        this.region = region;
        this.memberCnt = memberCnt;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.isFinished = isFinished;
        this.pm_id = pm_id;
        this.timestamp = timestamp;
        this.selected = false;
    }


    public ListItemProject(HashMap<String, Object> map) {
        this.title = (String)map.get("title");
        this.content = (String)map.get("content");
        this.region = (String)map.get("region");
        this.memberCnt = (Long) map.get("person");
        this.email = (String)map.get("email");
        this.phoneNumber = (String)map.get("phonenumber");
        this.isFinished = (Boolean)map.get("finish");
        this.pm_id = (String)map.get("pm_id");

        // Timestamp -> Date -> String 형변환
        Date tmpDate = ((Timestamp)map.get("upload_date")).toDate();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        this.timestamp =  format.format(tmpDate);
        this.selected = false;
    }

    // 파이어베이스 연동을 위해 dao 형태로 만들어줌
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("content", content);
        result.put("region", region);
        result.put("person", memberCnt);
        result.put("email", email);
        result.put("phonenumber", phoneNumber);
        result.put("upload_date", timestamp);
        result.put("finish", isFinished);
        result.put("pm_id", pm_id);
        result.put("project_id", project_id);

        return result;
    }

    public Date convertTimeStamp(String time) throws ParseException {
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date convertDate = transFormat.parse(time);
        return convertDate;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

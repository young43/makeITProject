package kr.ac.kookmin.makeit;

import java.util.HashMap;
import java.util.Map;

/**
 * @file ApplyInfo
 * @desc 프로젝트 지원정보 관련 클래스
 * @auther 김지홍(20191572)
 * @date 2020-11-12
 */

public class ApplyInfo {
    private String id;                           // user id
    private String project_id;                   // 지원한 프로젝트 id
    private HashMap<String, Boolean> projectMap; // 지원한 프로젝트 목록

    public ApplyInfo(){  // default생성자
        this.projectMap = new HashMap<>();
    }  //default 생성자

    public ApplyInfo(String id, String project_id){
        this.projectMap = new HashMap<>();
        this.id = id;
        this.projectMap.put(project_id, true); // 지원한 프로젝트 목록에 추가
    }

    public ApplyInfo(HashMap<String, Object> map){
        this.projectMap = new HashMap<>();
        // Set에서 id값 받아오기
        for(String key : map.keySet()){
            if(key.equals("id")){
                this.id = (String) map.get("id");
                continue;
            }
            this.projectMap.put(key, (Boolean) map.get(key)); // 지원한 프로젝트 목록에 추가
        }

    }


    public HashMap<String, Boolean> getProjectMap() {
        return projectMap;
    }

    public void setrojectMap(HashMap<String, Boolean> markMap) {
        this.projectMap = markMap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

}

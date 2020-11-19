package kr.ac.kookmin.makeit;

import java.util.HashMap;
import java.util.Map;

/**
 * @file BookmarkInfo
 * @desc 찜 목록 item 클래스
 * @auther 윤서영(20191633)
 * @date 2020-11-02
 */

public class BookmarkInfo {
    private String id;
    private String project_id;
    private boolean selected;
    private HashMap<String, Boolean> projectMap;

    public BookmarkInfo(){  // default생성자
        this.projectMap = new HashMap<>();
    }

    public BookmarkInfo(String id, String project_id, boolean flag){
        this.projectMap = new HashMap<>();
        this.id = id;
        this.projectMap.put(project_id, flag);
    }

    public BookmarkInfo(HashMap<String, Object> map){
        this.projectMap = new HashMap<>();
        for(String key : map.keySet()){
            if(key.equals("id")){
               this.id = (String) map.get("id");
               continue;
            }
            this.projectMap.put(key, (Boolean) map.get(key));
        }

//        this.id = (String) map.get("id");
//        this.project_id = (String) map.get("project_id");
//        this.selected = (boolean) map.get("selected");
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> map = new HashMap<>();

        for(String key : this.projectMap.keySet()){
            if(key.equals("id")){
                map.put("id", id);
                continue;
            }
            this.projectMap.put(key, (Boolean)this.projectMap.get(key));
        }

//        map.put("project_id", project_id);
//        map.put("selected", selected);

        return map;
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

    public boolean isSelected() {
        return selected;
    }

    public void setFlag(boolean selected) {
        this.selected = selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

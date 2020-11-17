package kr.ac.kookmin.makeit;

import java.util.HashMap;
import java.util.Map;

/**
 * @file UserInfo
 * @desc 회원정보 클래스
 * @auther 김지홍(20191572)
 * @date 2020-11-02
 */

public class UserInfo {
    private String id;
    private String passwd;
    private String email;
    private String phone;

    public UserInfo(){ }  // default생성자

    public UserInfo(String id, String passwd, String email, String phone){
        this.id = id;
        this.passwd = passwd;
        this.email = email;
        this.phone = phone;
    }

    public UserInfo(HashMap<String, Object> map){
        this.id = (String) map.get("id");
        this.passwd = (String) map.get("passwd");
        this.email = (String) map.get("email");
        this.phone = (String) map.get("phone");
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("passwd", passwd);
        map.put("email", email);
        map.put("phone", phone);

        return map;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

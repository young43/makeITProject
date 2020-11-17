package kr.ac.kookmin.makeit;

import java.util.HashMap;
import java.util.Map;

/**
 * @file ResumeInfo
 * @desc 이력서 관련 클래스
 * @auther 김찬미(20191574)
 * @date 2020-11-12
 */

public class ResumeInfo {
    private String resume_id;
    private String resume_name;
    private String resume_email;
    private String resume_phone;
    private String resume_introduction;
    private String resume_company1;
    private String resume_company2;
    private String resume_company3;
    private String resume_company_date1;
    private String resume_company_date2;
    private String resume_company_date3;



    public ResumeInfo(){ }

    public ResumeInfo(String resume_id, String resume_name, String resume_email, String resume_phone,
                      String resume_introduction, String resume_company1, String resume_company2, String resume_company3,
                        String resume_company_date1, String resume_company_date2, String resume_company_date3){
        this.resume_id = resume_id;
        this.resume_name = resume_name;
        this.resume_email = resume_email;
        this.resume_phone = resume_phone;
        this.resume_introduction = resume_introduction;
        this.resume_company1 = resume_company1;
        this.resume_company2 = resume_company2;
        this.resume_company3 = resume_company3;
        this.resume_company_date1 = resume_company_date1;
        this.resume_company_date2 = resume_company_date2;
        this.resume_company_date3 = resume_company_date3;
    }

    public ResumeInfo(HashMap<String, Object> map){
        this.resume_id = (String) map.get("resume_id");
        this.resume_name = (String) map.get("resume_name");
        this.resume_email = (String) map.get("resume_email");
        this.resume_phone = (String) map.get("resume_phone");
        this.resume_introduction = (String) map.get("resume_introduction");
        this.resume_company1 = (String) map.get("resume_company1");
        this.resume_company2 = (String) map.get("resume_company2");
        this.resume_company3 = (String) map.get("resume_company3");
        this.resume_company_date1 = (String) map.get("resume_company_date1");
        this.resume_company_date2 = (String) map.get("resume_company_date2");
        this.resume_company_date3 = (String) map.get("resume_company_date3");
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("resume_id", resume_id);
        map.put("resume_name", resume_name);
        map.put("resume_email", resume_email);
        map.put("resume_phone", resume_phone);
        map.put("resume_introduction", resume_introduction);
        map.put("resume_company1", resume_company1);
        map.put("resume_company2", resume_company2);
        map.put("resume_company3", resume_company3);
        map.put("resume_company_date1", resume_company_date1);
        map.put("resume_company_date2", resume_company_date2);
        map.put("resume_company_date3", resume_company_date3);

        return map;
    }


    public String getResume_id() { return resume_id; }

    public void setResume_id(String resume_id) { this.resume_id = resume_id; }

    public String getResume_name() { return resume_name; }

    public void setResume_name(String resume_name) { this.resume_name = resume_name; }

    public String getResume_email() { return resume_email; }

    public void setResume_email(String resume_email) { this.resume_email = resume_email; }

    public String getResume_phone() { return resume_phone; }

    public void setResume_phone(String resume_phone) { this.resume_phone = resume_phone; }

    public String getResume_introduction() { return resume_introduction; }

    public void setResume_introduction(String resume_introduction) { this.resume_introduction = resume_introduction; }

    public String getResume_company1() { return resume_company1; }

    public void setResume_company1(String resume_company1) { this.resume_company1 = resume_company1; }

    public String getResume_company2() { return resume_company2; }

    public void setResume_company2(String resume_company2) { this.resume_company2 = resume_company2; }

    public String getResume_company3() { return resume_company3; }

    public void setResume_company3(String resume_company3) { this.resume_company3 = resume_company3; }

    public String getResume_company_date1() { return resume_company_date1; }

    public void setResume_company_date1(String resume_company_date1) { this.resume_company_date1 = resume_company_date1; }

    public String getResume_company_date2() { return resume_company_date2; }

    public void setResume_company_date2(String resume_company_date2) { this.resume_company_date2 = resume_company_date2; }

    public String getResume_company_date3() { return resume_company_date3; }

    public void setResume_company_date3(String resume_company_date3) { this.resume_company_date3 = resume_company_date3; }
}

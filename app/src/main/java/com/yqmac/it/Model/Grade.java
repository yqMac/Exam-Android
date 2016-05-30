package com.yqmac.it.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yqmac on 2016/5/27 0027.
 */
public class Grade {
    private String examName;
    private String begainTime;
    private String endTime;
    private String grade;
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Grade(){

    }

    public Grade(JSONObject json){

        try {
            this.examName = json.getString("examName");
            this.begainTime = json.getString("begainTime");
            this.endTime = json.getString("endTime");
            this.grade = json.getString("grade");
            this.status = json.getInt("status");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Grade(String examName, String begainTime, String endTime, String grade) {
        this.examName = examName;
        this.begainTime = begainTime;
        this.endTime = endTime;
        this.grade = grade;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getBegainTime() {
        return begainTime;
    }

    public void setBegainTime(String begainTime) {
        this.begainTime = begainTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}

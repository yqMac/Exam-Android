package com.yqmac.it.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yqmac on 2016/5/24 0024.
 */
public class Exam {

    public int id;
    public String name;
    public String begainTime;
    public String endTime;
    public String detail;
    public int status;

    public Exam(){}

    public Exam(JSONObject obj ) {

        try {
            this.id = obj.getInt("id");
            this.name = obj.getString("name");
            this.begainTime = obj.getString("begainTime");
            this.endTime = obj.getString("endTime");
            this.detail = obj.getString("detail");
            this.status = obj.getInt("status");

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



}

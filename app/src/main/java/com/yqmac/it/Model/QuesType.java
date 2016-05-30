package com.yqmac.it.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yqmac on 2016/5/25 0025.
 */
public class QuesType {

    private  int id;
    private String name;


    public QuesType() {
    }

    public QuesType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public QuesType(JSONObject obj){
        try {
            this.id = obj.getInt("id");
            this.name = obj.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

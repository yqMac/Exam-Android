package com.yqmac.it.Model;

import android.util.Log;
import com.yqmac.it.util.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yqmac on 2016/5/27 0027.
 */
public class Ques {

    private int id;
    private int examId;

    private String content;
    private String answer;


    private String optionStr;
    private int optionNum;
    private String[] options;

    private String userAnswer;
    private List<Integer> userAnswerIndex = new ArrayList<>();

    private int typeId;
    private String typeName;

    public Ques() {

    }

    public Ques(JSONObject json) {
        try {
            this.id = json.getInt("id");
            this.content = json.getString("content");
            this.optionNum = json.getInt("optionNum");
            this.optionStr = json.getString("optionStr");
            if (optionStr.length() > 0) {
                System.out.println(StringUtil.string2Options(optionStr));
                this.options = StringUtil.string2OptionsArr(optionStr);
            }
            this.answer = json.getString("answer");
            this.typeId = json.getInt("typeId");
            this.typeName = json.getString("typeName");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getAns() {
        String s = "";
        if (userAnswer == null || userAnswer.length() == 0) {

            for (Integer integer : userAnswerIndex) {
                s += integer + ",";
            }
            Log.v("myTAG", s.length()+"");
            if (userAnswerIndex.size()>0)
                s = s.substring(0, s.length() - 1);
        } else s = userAnswer;
        return s;
    }

    public JSONObject getJson() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", this.id);
            obj.put("typeId", this.typeId);
            obj.put("ans", getAns());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;

    }

    public List<Integer> getUserAnswerIndex() {
        return userAnswerIndex;
    }

    public void setUserAnswerIndex(List<Integer> userAnswerIndex) {
        this.userAnswerIndex = userAnswerIndex;
    }

    public String getOptionStr() {
        return optionStr;
    }

    public void setOptionStr(String optionStr) {
        this.optionStr = optionStr;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getOptionNum() {
        return optionNum;
    }

    public void setOptionNum(int optionNum) {
        this.optionNum = optionNum;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}

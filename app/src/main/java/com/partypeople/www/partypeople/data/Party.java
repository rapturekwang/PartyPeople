package com.partypeople.www.partypeople.data;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;
import com.partypeople.www.partypeople.utils.JSONParsing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwang on 15. 11. 8..
 */
public class Party implements Serializable{
    public boolean isBookmark() {
        return bookmark;
    }

    public void setBookmark(boolean bookmark) {
        this.bookmark = bookmark;
    }

    public boolean bookmark;

    public String id;
    public String name;
    public String description;
    public boolean has_photo;
    public String photo;
    public File imageFile;
    public Bitmap image;
    public String created_at;
    public String deleted_at;
    public String start_at;
    public String end_at;
    public String pay_end_at;
    public int[] themes;
    public String location;
    @SerializedName("private")
    public boolean privated;
    public String password = "0000";
    public double expect_pay;
    public List<PayMethod> pay_method;
    public String bank;
    public double account;
    public double phone;
//    public JSONObject owner;
//    public List<> likes;
    public List<Member> members;
    public int member_count;
    public boolean auto_approval;
    public String enrollment_code;
    public boolean featured;

    public String getJsonString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("name", name);
            jsonObject.accumulate("description", description);
            jsonObject.accumulate("photo", photo);
            jsonObject.accumulate("end_at", end_at);
//            jsonObject.accumulate("theme", getTheme());
            jsonObject.accumulate("location", location);
            jsonObject.accumulate("private", privated);
            jsonObject.accumulate("password", password);
            jsonObject.accumulate("expect_pay", expect_pay);
            jsonObject.accumulate("pay_method", pay_method);
            jsonObject.accumulate("bank", bank);
            jsonObject.accumulate("account", account);
            jsonObject.accumulate("phone", phone);
        } catch (Exception e) {

        }
        return jsonObject.toString();
    }
}

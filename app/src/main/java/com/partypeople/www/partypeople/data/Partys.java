package com.partypeople.www.partypeople.data;

import com.google.gson.annotations.SerializedName;
import com.partypeople.www.partypeople.utils.JSONParsing;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kwang on 15. 11. 8..
 */
public class Partys implements JSONParsing {
    public String _id;
    public String name;
    public String photo;
    public String theme;
    public String date;
    public String location;
    public String description;
    @SerializedName("private")
    public boolean privated;
    public String password;
    public int expect_pay;
    public String bank;
    public String account;
    public boolean active;
    public int __v;
    public String pay_method;
    public String created_at;

    @Override
    public void parsing(JSONObject jobject) throws JSONException {
        _id = jobject.getString("_id");
        name = jobject.getString("name");
        photo = jobject.getString("photo");
        theme = jobject.getString("theme");
        date = jobject.getString("date");
        location = jobject.getString("location");
        description = jobject.getString("description");
        privated = jobject.getBoolean("private");
        password = jobject.getString("password");
        expect_pay = jobject.getInt("expect_pay");
        bank = jobject.getString("bank");
        account = jobject.getString("account");
        active = jobject.getBoolean("active");
        __v = jobject.getInt("__v");
        pay_method = jobject.getString("pay_method");
        created_at = jobject.getString("created_at");
    }
}

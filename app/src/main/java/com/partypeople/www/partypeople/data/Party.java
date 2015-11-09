package com.partypeople.www.partypeople.data;

import com.google.gson.annotations.SerializedName;
import com.partypeople.www.partypeople.utils.JSONParsing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwang on 15. 11. 8..
 */
public class Party implements JSONParsing {
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
    public int account;
    public boolean active;
    public int __v;
    public List<PayMethod> pay_method;
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
        account = jobject.getInt("account");
        active = jobject.getBoolean("active");
        __v = jobject.getInt("__v");
//        JSONObject jpaymethod = jobject.getJSONObject("pay_method");
        pay_method = new ArrayList<PayMethod>();
        JSONArray array = jobject.getJSONArray("pay_method");
        for(int i=0; i<array.length(); i++) {
            JSONObject jarea = array.getJSONObject(i);
            PayMethod s = new PayMethod();
            s.parsing(jarea);
            pay_method.add(s);
        }
        created_at = jobject.getString("created_at");
    }
}

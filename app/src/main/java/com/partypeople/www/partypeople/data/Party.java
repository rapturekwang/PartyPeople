package com.partypeople.www.partypeople.data;

import com.google.gson.annotations.SerializedName;
import com.partypeople.www.partypeople.utils.JSONParsing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwang on 15. 11. 8..
 */
public class Party implements Serializable{
    public boolean bookmark;

    public String id;
    public String name;
    public String photo;
//    public String theme;
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

    public boolean isBookmark() {
        return bookmark;
    }

    public void setBookmark(boolean bookmark) {
        this.bookmark = bookmark;
    }

    public int __v;
    public List<PayMethod> pay_method;
    public String created_at;

//    public String get_id() {
//        return id;
//    }
//
//    public void set_id(String _id) {
//        this.id = _id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getPhoto() {
//        return photo;
//    }
//
//    public void setPhoto(String photo) {
//        this.photo = photo;
//    }
//
//    public String getTheme() {
//        return theme;
//    }
//
//    public void setTheme(String theme) {
//        this.theme = theme;
//    }
//
//    public String getDate() {
//        return date;
//    }
//
//    public void setDate(String date) {
//        this.date = date;
//    }
//
//    public String getLocation() {
//        return location;
//    }
//
//    public void setLocation(String location) {
//        this.location = location;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public boolean isPrivated() {
//        return privated;
//    }
//
//    public void setPrivated(boolean privated) {
//        this.privated = privated;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public int getExpect_pay() {
//        return expect_pay;
//    }
//
//    public void setExpect_pay(int expect_pay) {
//        this.expect_pay = expect_pay;
//    }
//
//    public String getBank() {
//        return bank;
//    }
//
//    public void setBank(String bank) {
//        this.bank = bank;
//    }
//
//    public int getAccount() {
//        return account;
//    }
//
//    public void setAccount(int account) {
//        this.account = account;
//    }
//
//    public boolean isActive() {
//        return active;
//    }
//
//    public void setActive(boolean active) {
//        this.active = active;
//    }
//
//    public int get__v() {
//        return __v;
//    }
//
//    public void set__v(int __v) {
//        this.__v = __v;
//    }
//
//    public List<PayMethod> getPay_method() {
//        return pay_method;
//    }
//
//    public void setPay_method(List<PayMethod> pay_method) {
//        this.pay_method = pay_method;
//    }
//
//    public String getCreated_at() {
//        return created_at;
//    }
//
//    public void setCreated_at(String created_at) {
//        this.created_at = created_at;
//    }

//    @Override
//    public void parsing(JSONObject jobject) throws JSONException {
//        id = jobject.getString("_id");
//        name = jobject.getString("name");
//        photo = jobject.getString("photo");
//        theme = jobject.getString("navi_theme");
//        date = jobject.getString("date");
//        location = jobject.getString("location");
//        description = jobject.getString("description");
//        privated = jobject.getBoolean("private");
//        password = jobject.getString("password");
//        expect_pay = jobject.getInt("expect_pay");
//        bank = jobject.getString("bank");
//        account = jobject.getInt("account");
//        active = jobject.getBoolean("active");
//        __v = jobject.getInt("__v");
////        JSONObject jpaymethod = jobject.getJSONObject("pay_method");
//        pay_method = new ArrayList<PayMethod>();
//        JSONArray array = jobject.getJSONArray("pay_method");
//        for(int i=0; i<array.length(); i++) {
//            JSONObject jarea = array.getJSONObject(i);
//            PayMethod s = new PayMethod();
//            s.parsing(jarea);
//            pay_method.add(s);
//        }
//        created_at = jobject.getString("created_at");
//    }
//
//    public String getJsonString() {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.accumulate("name", getName());
//            jsonObject.accumulate("photo", getPhoto());
//            jsonObject.accumulate("theme", getTheme());
//            jsonObject.accumulate("date", getDate());
//            jsonObject.accumulate("location", getLocation());
//            jsonObject.accumulate("description", getDescription());
//            jsonObject.accumulate("private", isPrivated());
//            jsonObject.accumulate("password", getPassword());
//            jsonObject.accumulate("expect_pay", getExpect_pay());
//            jsonObject.accumulate("bank", getBank());
//            jsonObject.accumulate("account", getAccount());
//            jsonObject.accumulate("active", isActive());
//            jsonObject.accumulate("pay_method", getPay_method());
//            jsonObject.accumulate("created_at", getCreated_at());
//        } catch (Exception e) {
//
//        }
//        return jsonObject.toString();
//    }
}

package com.partypeople.www.partypeople.data;

import com.partypeople.www.partypeople.utils.JSONParsing;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Tacademy on 2015-11-09.
 */
public class PayMethod implements Serializable{
    public String title;
    public int price;
    public String id;
    public String photo;

    public void add(String title, int price) {
        this.title = title;
        this.price = price;
    }

//    @Override
//    public void parsing(JSONObject jobject) throws JSONException {
//        title = jobject.getString("title");
//        price = jobject.getInt("price");
//        id = jobject.getString("_id");
//    }
}

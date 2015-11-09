package com.partypeople.www.partypeople.data;

import com.partypeople.www.partypeople.utils.JSONParsing;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tacademy on 2015-11-09.
 */
public class PayMethod implements JSONParsing{
    public String title;
    public int price;
    public String _id;

    public void add(String title, int price) {
        this.title = title;
        this.price = price;
    }

    @Override
    public void parsing(JSONObject jobject) throws JSONException {
        title = jobject.getString("title");
        price = jobject.getInt("price");
        _id = jobject.getString("_id");
    }
}

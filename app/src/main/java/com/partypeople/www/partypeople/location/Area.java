package com.partypeople.www.partypeople.location;

import com.partypeople.www.partypeople.utils.JSONParsing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-11-06.
 */
public class Area implements JSONParsing{
//    public List<String> upperDistName;
    //List<String> middleDistName;
    public String upperDistName;

    @Override
    public void parsing(JSONObject jobject) throws JSONException {
//        upperDistName = new ArrayList<String>();
//        JSONArray array = jobject.getJSONArray("upperDistName");
//        for(int i = 0; i<array.length(); i++) {
//            JSONObject jupperdistname = array.getJSONObject(i);
//            upperDistName.add(jupperdistname.toString());
//        }
        upperDistName = jobject.getString("upperDistName");
//        middleDistName = new ArrayList<String>();
//        array = jobject.getJSONArray("middleDistName");
//        for(int i = 0; i<array.length(); i++) {
//            JSONObject jmiddledistname = array.getJSONObject(i);
//            middleDistName.add(jmiddledistname.toString());
//        }
    }
}

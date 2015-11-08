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

    public String upperDistName;
    public String middleDistName;
    public int upperDistCode;

    @Override
    public void parsing(JSONObject jobject) throws JSONException {
        upperDistName = jobject.getString("upperDistName");
        middleDistName = jobject.getString("middleDistName");
        upperDistCode = jobject.getInt("upperDistCode");
    }
}

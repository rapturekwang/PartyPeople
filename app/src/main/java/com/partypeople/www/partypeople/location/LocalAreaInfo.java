package com.partypeople.www.partypeople.location;

import com.partypeople.www.partypeople.utils.JSONParsing;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tacademy on 2015-11-06.
 */
public class LocalAreaInfo implements JSONParsing{
    public Areas areas;

    @Override
    public void parsing(JSONObject jobject) throws JSONException {
        JSONObject jareas = jobject.getJSONObject("areas");
        areas.parsing(jareas);
    }
}

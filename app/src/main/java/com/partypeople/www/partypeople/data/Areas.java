package com.partypeople.www.partypeople.data;

import com.partypeople.www.partypeople.utils.JSONParsing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-11-06.
 */
public class Areas implements JSONParsing{
    public List<Area> area;

    @Override
    public void parsing(JSONObject jobject) throws JSONException {
        area = new ArrayList<Area>();
        JSONArray array = jobject.getJSONArray("area");
        for(int i=0; i<array.length(); i++) {
            JSONObject jarea = array.getJSONObject(i);
            Area s = new Area();
            s.parsing(jarea);
            area.add(s);
        }
    }
}

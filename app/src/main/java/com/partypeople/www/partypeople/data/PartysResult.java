package com.partypeople.www.partypeople.data;

import com.partypeople.www.partypeople.location.Area;
import com.partypeople.www.partypeople.utils.JSONParsing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-11-09.
 */
public class PartysResult implements JSONParsing {
    public List<Partys> partysList;

    @Override
    public void parsing(JSONObject jobject) throws JSONException {
        partysList = new ArrayList<Partys>();
        JSONArray array = jobject.getJSONArray("partys");
        for(int i=0; i<array.length(); i++) {
            JSONObject jpartys = array.getJSONObject(i);
            Partys s = new Partys();
            s.parsing(jpartys);
            partysList.add(s);
        }
    }
}

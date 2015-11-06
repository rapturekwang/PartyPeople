package com.partypeople.www.partypeople.location;

import com.partypeople.www.partypeople.utils.JSONParsing;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tacademy on 2015-11-06.
 */
public class LocalInfoResult implements JSONParsing{
    public LocalAreaInfo localAreaInfo;

    @Override
    public void parsing(JSONObject jobject) throws JSONException {
        localAreaInfo = new LocalAreaInfo();
        JSONObject jlocalareainfo = jobject.getJSONObject("localAreaInfo");
        localAreaInfo.parsing(jlocalareainfo);
    }
}

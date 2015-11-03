package com.partypeople.www.partypeople.manager;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.partypeople.www.partypeople.utils.MyApplication;

/**
 * Created by dongja94 on 2015-10-28.
 */
public class PropertyManager {
    private static PropertyManager instance;
    public static PropertyManager getInstance() {
        if (instance == null) {
            instance = new PropertyManager();
        }
        return instance;
    }

    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    private PropertyManager() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        mEditor = mPrefs.edit();
    }

    private static final String FIELD_FACEBOOK_ID = "facebookId";

    public void setFacebookId(String id) {
        mEditor.putString(FIELD_FACEBOOK_ID, id);
        mEditor.commit();
    }

    public void deleteId() {
        mEditor.remove(FIELD_FACEBOOK_ID);
    }

    public String getFaceBookId() {
        return mPrefs.getString(FIELD_FACEBOOK_ID, "");
    }
}

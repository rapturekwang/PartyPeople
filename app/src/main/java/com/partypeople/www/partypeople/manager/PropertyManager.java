package com.partypeople.www.partypeople.manager;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.partypeople.www.partypeople.data.User;
import com.partypeople.www.partypeople.utils.MyApplication;

import java.util.List;

/**
 * Created by dongja94 on 2015-10-28.
 */
public class PropertyManager {
    User user;
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
    private static final String FIELD_ID = "id";
    private static final String FIELD_TOKEN = "token";
    private static final String FIELD_EMAIL = "email";
    private static final String FIELD_LOCATION = "location";
    private static final String FIELD_THEME = "navi_theme";

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

    public void setId(String id) {
        mEditor.putString(FIELD_ID, id);
        mEditor.commit();
    }

    public void setToken(String token) {
        mEditor.putString(FIELD_TOKEN, token);
        mEditor.commit();
    }

    public String getToken() {
        String token = mPrefs.getString(FIELD_TOKEN, "");
        return token;
    }

    public void setEmail(String email) {
        mEditor.putString(FIELD_EMAIL, email);
        mEditor.commit();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        setToken(user.token);
        setId(user.data.id);
        setEmail(user.data.email);
    }

    public void setLocation(String location) {
        mEditor.putString(FIELD_LOCATION, location);
        mEditor.commit();
    }

    public String getLocation() {
        return mPrefs.getString(FIELD_LOCATION, "");
    }

    public void setTheme(String theme) {
        mEditor.putString(FIELD_THEME, theme);
        mEditor.commit();
    }

    public String getTheme() {
        return mPrefs.getString(FIELD_THEME, "");
    }

    public boolean isLogin() {
        String token;
        token = mPrefs.getString(FIELD_TOKEN, "");
        Log.d("PropertyManager", token);
        return token.equals("") ? false: true;
    }

    public void logout() {
        mEditor.clear();
        mEditor.commit();
    }

    private static final String REG_ID = "regToken";

    public void setRegistrationToken(String regId) {
        mEditor.putString(REG_ID, regId);
        mEditor.commit();
    }

    public String getRegistrationToken() {
        return mPrefs.getString(REG_ID, "");
    }
}

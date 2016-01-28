package com.partypeople.www.partypeople.manager;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.facebook.login.LoginManager;
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.data.User;
import com.partypeople.www.partypeople.utils.Constants;
import com.partypeople.www.partypeople.utils.MyApplication;

import java.util.List;

/**
 * Created by dongja94 on 2015-10-28.
 */
public class PropertyManager {
    User user;
    Party party;
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
    private static final String FIELD_LOGIN_METHOD = "loginMethod";

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

    public void setLoginMethod(int method) {
        mEditor.putInt(FIELD_LOGIN_METHOD, method);
        mEditor.commit();
    }

    public int getLoginMethod() {
        int loginMethod = mPrefs.getInt(FIELD_LOGIN_METHOD, -1);
        return loginMethod;
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
        setId(user.id);
        setEmail(user.email);
    }

    public boolean isLogin() {
        String token;
        token = mPrefs.getString(FIELD_TOKEN, "");
        return token.equals("") ? false: true;
    }

    public void logout() {
        String regId = getRegistrationToken();
        if(getLoginMethod()==Constants.LOGIN_WITH_FACEBOOK) {
            LoginManager.getInstance().logOut();
        }
        mEditor.clear();
        mEditor.commit();
        setRegistrationToken(regId);
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

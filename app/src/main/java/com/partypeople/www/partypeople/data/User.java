package com.partypeople.www.partypeople.data;

import com.partypeople.www.partypeople.manager.PropertyManager;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Tacademy on 2015-11-11.
 */
public class User implements Serializable{
    public String id;
    public String email;
    public String name;
    public String realname;
    public boolean auth;
    public String bank_name;
    public double bank_account;
    public List<Follow> following;
    public List<Follow> follower;
    public String address;
    public String favorite_address;
    public double tel;
    public int[] themes;
    public String role;
    public boolean has_photo;
    public String photo;
    public File imageFile;
    public String created_at;
    public List<Party> groups;
    public List<Party> likes;
    public String provider;
    public String android_token;
    public boolean[] push;

    public User() {
        User propertyUser = PropertyManager.getInstance().getUser();

        if(propertyUser!=null) {
            auth = propertyUser.auth;
            has_photo = propertyUser.has_photo;
            push = propertyUser.push;
            tel = propertyUser.tel;
            bank_account = propertyUser.bank_account;
        }
    }
}

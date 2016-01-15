package com.partypeople.www.partypeople.data;

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
    public double phone;
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
}

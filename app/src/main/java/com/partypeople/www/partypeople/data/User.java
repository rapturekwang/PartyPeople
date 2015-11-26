package com.partypeople.www.partypeople.data;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Tacademy on 2015-11-11.
 */
public class User implements Serializable{
    public String _id;
    public String id;
    public String email;
    public String name;
//    public List<Follow> follows;
    public String address;
    public String favorite_address;
    public double tel;
    public int[] theme;
//    public Role role;
    public boolean has_photo;
    public String photo;
    public File imageFile;
    public String created_at;
    //public Group group;
}

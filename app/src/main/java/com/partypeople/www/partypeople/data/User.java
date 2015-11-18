package com.partypeople.www.partypeople.data;

import java.io.Serializable;

/**
 * Created by Tacademy on 2015-11-11.
 */
public class User implements Serializable{
    public String id;
    public String token;
    public String description;
    public boolean has_photo;
    public String photo;
    public String created_at;
    public boolean email_validated;
    //public Group group;
    public Data data;
}

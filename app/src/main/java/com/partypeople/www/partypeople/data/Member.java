package com.partypeople.www.partypeople.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Tacademy on 2015-11-27.
 */
public class Member implements Serializable{
    public String id;
    public String email;
    public String name;
    public boolean has_photo;
    public String photo;
    public int rating;
    public String role;
}

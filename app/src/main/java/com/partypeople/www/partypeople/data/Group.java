package com.partypeople.www.partypeople.data;

import java.io.Serializable;

/**
 * Created by Tacademy on 2015-11-11.
 */
public class Group implements Serializable{
    public String id;
    public String name;
    public String description;
    public String has_photo;
    public int member_count;
    public Role role;
}

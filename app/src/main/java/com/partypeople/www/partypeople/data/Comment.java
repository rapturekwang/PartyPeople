package com.partypeople.www.partypeople.data;

import java.io.Serializable;

/**
 * Created by kwang on 15. 12. 29..
 */
public class Comment implements Serializable {
    public String group;
    public User from;
    public String comment;
    public boolean has_photo;
    public String photo;
    public String created_at;
    public String id;
    public boolean participant;
}

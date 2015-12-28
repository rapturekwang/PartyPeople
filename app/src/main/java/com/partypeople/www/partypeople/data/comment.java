package com.partypeople.www.partypeople.data;

import java.io.Serializable;

/**
 * Created by kwang on 15. 12. 28..
 */
public class comment implements Serializable{
    public String group;
    public String from;
    public String comment;
    public boolean has_photo;
    public String photo;
    public String created_at;
}

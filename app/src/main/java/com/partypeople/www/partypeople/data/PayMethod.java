package com.partypeople.www.partypeople.data;

import java.io.Serializable;

/**
 * Created by Tacademy on 2015-11-09.
 */
public class PayMethod implements Serializable {
    public String title;
    public int price;
    public String id;
    public String photo;

    public void add(String title, int price) {
        this.title = title;
        this.price = price;
    }
}
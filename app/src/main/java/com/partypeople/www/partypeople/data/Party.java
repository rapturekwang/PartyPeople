package com.partypeople.www.partypeople.data;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwang on 15. 11. 8..
 */
public class Party implements Serializable{
    public boolean isBookmark() {
        return bookmark;
    }

    public void setBookmark(boolean bookmark) {
        this.bookmark = bookmark;
    }

    public boolean bookmark = false;

    public String id;
    public String name;
    public String description;
    public boolean has_photo;
    public String photo;
    public File imageFile;
    public Bitmap image;
    public String created_at;
    public String deleted_at;
    public String start_at;
//    public String end_at;
//    public String pay_end_at;
    public String amount_end_at;
    public int[] themes;
    public String location;
    @SerializedName("private")
    public boolean privated;
    public String password = "0000";
//    public double expect_pay;
//    public List<PayMethod> pay_method;
    public double amount_expect;
    public List<PayMethod> amount_method;
    public double amount_total;
    public User owner;
    public List<Like> likes;
    public List<Member> members;
    public int member_count;
    public boolean auto_approval;
    public String enrollment_code;
    public boolean featured;
    public List<Comment> comments;
    public ArrayList<String> photos;
    public ArrayList<File> imageFiles;
    public boolean[] has_photos;
    public boolean amount_custom;

    public String role;
}

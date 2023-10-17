package com.theflash.hpw.bookshare;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Follow {

    public String bk;
    public String req;
    public String sh;

    public Follow() {
    }
    public Follow(String books , String requested, String Shared)
    {
       bk=books;
       req=requested;
       sh=Shared;
    }
}

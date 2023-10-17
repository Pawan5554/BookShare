package com.theflash.hpw.bookshare;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class addmydetail {

    public  String book;
    public  String name;
    public   String url;
    public String number;

    public addmydetail()
    {}

    public  addmydetail(String bookname,String bookurl,String n,String na)
    {
        book=bookname;
        url=bookurl;
        number=n;
        name=na;
    }
}

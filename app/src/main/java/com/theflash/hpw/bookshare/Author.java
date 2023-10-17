package com.theflash.hpw.bookshare;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Author {

    public  String book;
    public  String type;
    public   String url;
    public  String author;
    public  String date;
    public String number;

    public Author() {
    }

    public Author(String bookname,String bookurl,String auth,String  ty,String dat,String n)
    {
        book=bookname;
        url=bookurl;
        author=auth;
        type=ty;
        date=dat;
        number=n;
    }
}

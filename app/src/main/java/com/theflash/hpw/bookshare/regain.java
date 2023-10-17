package com.theflash.hpw.bookshare;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class regain {

    public  String Gender;
    public   String DOB;
    public  String state;
    public  String city;
    public String profilename;
    public String imageurl;
    public String bio;
    public String country;

    public regain() {
    }
    public regain(String gender , String dob, String c, String s, String p,String url,String bb,String con)
    {

        Gender=gender;
        DOB=dob;
        city=c;
        state=s;
        profilename=p;
        imageurl=url;
        bio=bb;
        country=con;

    }

}

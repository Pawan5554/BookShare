package com.theflash.hpw.bookshare;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class AddFollowers {
    public  String number;

    public  AddFollowers()
    {}
    public AddFollowers(String num)
    {
        number=num;
    }

}

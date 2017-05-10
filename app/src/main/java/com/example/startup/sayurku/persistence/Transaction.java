package com.example.startup.sayurku.persistence;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Startup on 4/18/17.
 */

public class Transaction {

    public String date;
    public String info;
    public int amount;
    public String type;

    public Transaction()
    {
        date="";
        info="";
        amount=0;
        type="";
    }

    public String getDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date2= null;
        try {
            date2= simpleDateFormat.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MMMM/dd/yy, HH:mm aa");
        return simpleDateFormat2.format(date2);

    }
}

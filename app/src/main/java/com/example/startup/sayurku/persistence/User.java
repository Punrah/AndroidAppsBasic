package com.example.startup.sayurku.persistence;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Settings.Secure;

/**
 * Created by Startup on 1/27/17.
 */

public class User implements Parcelable {
    public  String name;
    public  String email;
    public  String phone;
    public  String id_customer;
    public  String password;
    public String deviceId;

    public User()
    {
        name="";
        email ="";
        phone="";
        id_customer ="";
        password="";

    }

    public static String getDeviceId(Context context)
    {
        return Secure.getString(context.getContentResolver(),
                Secure.ANDROID_ID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(id_customer);

    }
    // Method to recreate a Question from a Parcel
    public static Creator<User> CREATOR = new Creator<User>() {

        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }

    };

    public User (Parcel parcel) {

        name=parcel.readString();
        email =parcel.readString();
        phone=parcel.readString();
        id_customer =parcel.readString();
    }




}

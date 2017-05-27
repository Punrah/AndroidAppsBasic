package com.example.startup.sayurku.persistence;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Settings.Secure;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Startup on 1/27/17.
 */

public class User implements Parcelable {
    public  String name;
    public  String company;
    public String address;
    public LatLng latLng;
    public  String email;
    public  String phone;
    public  String id_customer;
    public  String password;

    public User()
    {
        name="";
        company="";
        address="";
        latLng=new LatLng(0,0);
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
        dest.writeString(company);
        dest.writeString(address);
        dest.writeParcelable(latLng,flags);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(id_customer);
        dest.writeString(password);

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

        this.name=parcel.readString();
        this. company = parcel.readString();
        this.latLng = (LatLng) parcel.readParcelable(LatLng.class.getClassLoader());
        this.email =parcel.readString();
        this.phone=parcel.readString();
        this.id_customer =parcel.readString();
        this.password=parcel.readString();
    }




}

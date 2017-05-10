package com.example.startup.sayurku.persistence;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Startup on 2/1/17.
 */

public class Driver implements Parcelable {
    public String name;
    public String phone;
    public String plat;
    public String idDriver;
    public LatLng driverLocation;
    public String photo;
    public int rating;

    public Driver()
    {
        name="";
        phone="";
        plat="";
        idDriver="";
        driverLocation = new LatLng(0,0);
        photo="";
        rating=0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(plat);
        dest.writeString(idDriver);

        dest.writeParcelable(driverLocation,flags);
        dest.writeInt(rating);

    }
    // Method to recreate a Question from a Parcel
    public static Creator<Driver> CREATOR = new Creator<Driver>() {

        @Override
        public Driver createFromParcel(Parcel source) {
            return new Driver(source);
        }

        @Override
        public Driver[] newArray(int size) {
            return new Driver[size];
        }

    };

    public Driver (Parcel parcel) {

        name=parcel.readString();
        phone=parcel.readString();
        plat=parcel.readString();
        idDriver=parcel.readString();
        driverLocation = (LatLng) parcel.readParcelable(LatLng.class.getClassLoader());
        rating=parcel.readInt();
    }
}

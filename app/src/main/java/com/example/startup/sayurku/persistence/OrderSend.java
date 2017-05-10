package com.example.startup.sayurku.persistence;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Startup on 1/31/17.
 */

public class OrderSend extends Order implements Parcelable {

    public String senderName;
    public String senderPhone;
    public String receiverName;
    public String receiverPhone;
    public String description;


    public OrderSend()
    {
        senderName="";
        senderPhone="";
        receiverName="";
        receiverPhone="";
        description="";
    }
    public String getStatusString()
    {
        String value="";
        if(status.contentEquals("Accept"))
        {
            value="Driver Found";
        }
        else if(status.contentEquals("OTW"))
        {
            value="Picking up package";
        }
        else if(status.contentEquals("start"))
        {
            value="On the way with your package";
        }
        else if(status.contentEquals("Complete"))
        {
            value="Completed";
        }
        else if(status.contentEquals("Cancel"))
        {
            value="Canceled";
        }
        return value;
    }




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(user,flags);
        dest.writeString(id_order);
        dest.writeString(orderDate);
        dest.writeInt(price);
        dest.writeInt(priceCar);
        dest.writeInt(priceBike);
        dest.writeDouble(distance);
        dest.writeParcelable(pickupPosition,flags);
        dest.writeParcelable(dropoofPosition,flags);
        dest.writeString(pickupAddress);
        dest.writeString(dropoffAddress);
        dest.writeParcelable(driver,flags);
        dest.writeString(pickupNote);
        dest.writeString(dropoffNote);
        dest.writeString(status);
        dest.writeInt(orderType);
        dest.writeString(vehicle);
        dest.writeString(payment_type);
        dest.writeString(senderName);
        dest.writeString(senderPhone);
        dest.writeString(receiverName);
        dest.writeString(receiverPhone);
        dest.writeString(description);
        dest.writeInt(rating);


    }

    public OrderSend (Parcel parcel) {

        this.user = (User) parcel.readParcelable(User.class.getClassLoader());
        this.id_order=parcel.readString();
        this.orderDate=parcel.readString();
        this.price=parcel.readInt();
        this.priceCar=parcel.readInt();
        this.priceBike=parcel.readInt();
        this.distance=parcel.readDouble();
        this.pickupPosition = (LatLng) parcel.readParcelable(LatLng.class.getClassLoader());
        this.dropoofPosition=(LatLng) parcel.readParcelable(LatLng.class.getClassLoader());
        this.pickupAddress=parcel.readString();
        this.dropoffAddress=parcel.readString();
        this.driver = (Driver) parcel.readParcelable(Driver.class.getClassLoader());
        this.pickupNote=parcel.readString();
        this.dropoffNote=parcel.readString();
        this.status=parcel.readString();
        this.orderType=parcel.readInt();
        this.vehicle=parcel.readString();
        this.payment_type=parcel.readString();
        this.senderName=parcel.readString();
        this.senderPhone=parcel.readString();
        this.receiverName=parcel.readString();
        this.receiverPhone=parcel.readString();
        this.description=parcel.readString();
        this.rating=parcel.readInt();

    }

    // Method to recreate a Question from a Parcel
    public static Creator<OrderSend> CREATOR = new Creator<OrderSend>() {

        @Override
        public OrderSend createFromParcel(Parcel source) {
            return  new OrderSend(source);
        }

        @Override
        public OrderSend[] newArray(int size) {
            return new OrderSend[size];
        }

    };


}

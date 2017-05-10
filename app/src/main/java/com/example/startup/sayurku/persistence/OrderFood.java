package com.example.startup.sayurku.persistence;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Startup on 1/31/17.
 */

public class OrderFood extends Order implements Parcelable{

    public List<Menu> menu;
    public List<Item> item;
    public int totalPrice;


    public OrderFood()
    {
        menu = new ArrayList<Menu>();
        item = new ArrayList<Item>();
        totalPrice=0;
    }

    public List<Item> getSelectedItem()
    {
        List<Item> selectedItem = new ArrayList<Item>();
        for(int i=0;i<menu.size();i++)
        {
            selectedItem.addAll(menu.get(i).getSelectedItem());
        }
        return selectedItem;
    }

    public int getRecapPrice()
    {
        int price=0;
        for(int i=0;i<menu.size();i++)
        {
            price=price+menu.get(i).getMenuPrice();
        }
        return price;
    }

    public int getRecapQty()
    {
        int qty=0;
        for(int i=0;i<menu.size();i++)
        {
            qty=qty+menu.get(i).getMenuQty();
        }
        return qty;
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
            value="Picking up food";
        }
        else if(status.contentEquals("start"))
        {
            value="On the way with your food";
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
        dest.writeList(menu);
        dest.writeList(item);
        dest.writeInt(totalPrice);
        dest.writeInt(rating);


    }

    public OrderFood (Parcel parcel) {

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
        this.menu = parcel.readArrayList(Menu.class.getClassLoader());
        this.item = parcel.readArrayList(Menu.class.getClassLoader());
        this.totalPrice=parcel.readInt();
        this.rating=parcel.readInt();

    }

    // Method to recreate a Question from a Parcel
    public static Creator<OrderFood> CREATOR = new Creator<OrderFood>() {

        @Override
        public OrderFood createFromParcel(Parcel source) {
            return  new OrderFood(source);
        }

        @Override
        public OrderFood[] newArray(int size) {
            return new OrderFood[size];
        }

    };




}

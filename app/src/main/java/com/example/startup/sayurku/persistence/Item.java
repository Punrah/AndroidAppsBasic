package com.example.startup.sayurku.persistence;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Startup on 2/10/17.
 */

public class Item implements Parcelable {

    public String idItem;
    public String name;
    public String restaurantName;
    public String description;
    public int price;
    public String photo;
    public int qty;
    public String notes;
    public Item()
    {
        idItem="";
        name="";
        restaurantName="";
        photo="";
        price=0;
        description="";
        qty=0;
        notes="";
    }


    public void plusOne()
    {
        qty++;
    }
    public void minOne()
    {
        if(qty>0) {
            qty--;
        }
    }

    public int getItemPrice() {
        return qty*price;
    }

    public String getPriceString()
    {
        return String.valueOf(price);
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        public Item createFromParcel(Parcel source) {
            Item item = new Item();
            item.idItem = source.readString();
            item.name = source.readString();
            item.restaurantName = source.readString();
            item.description=source.readString();
            item.photo=source.readString();
            item.price=source.readInt();
            item.qty=source.readInt();
            item.notes=source.readString();

            return item;
        }
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public int describeContents() {
        return 0;
    }


    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(idItem);
        parcel.writeString(name);
        parcel.writeString(restaurantName);
        parcel.writeString(description);
        parcel.writeString(photo);
        parcel.writeInt(price);
        parcel.writeInt(qty);
        parcel.writeString(notes);
    }

    public int getItemQty() {
        return qty;
    }
}

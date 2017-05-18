package com.example.startup.sayurku.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.startup.sayurku.helper.OrderSQLiteHandler;
import com.example.startup.sayurku.helper.SessionManager;
import com.example.startup.sayurku.main.MainActivity;
import com.google.gson.Gson;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Startup on 1/31/17.
 */

public class Order  implements Parcelable{

    public User user;
    public String id_order;
    public String orderDate;
    public int price;
    public List<Item> item;
    public int totalPrice;

    public Order(Context context)
    {


        item = new ArrayList<Item>();
        user = new User();
        id_order="";
        orderDate="";
        price=0;
        totalPrice=0;
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
        dest.writeList(item);
        dest.writeInt(totalPrice);


    }

    public Order(Parcel parcel) {

        this.user = (User) parcel.readParcelable(User.class.getClassLoader());
        this.id_order=parcel.readString();
        this.orderDate=parcel.readString();
        this.price=parcel.readInt();
        this.item = parcel.readArrayList(Item.class.getClassLoader());
        this.totalPrice=parcel.readInt();

    }

    // Method to recreate a Question from a Parcel
    public static Creator<Order> CREATOR = new Creator<Order>() {

        @Override
        public Order createFromParcel(Parcel source) {
            return  new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }

    };




}

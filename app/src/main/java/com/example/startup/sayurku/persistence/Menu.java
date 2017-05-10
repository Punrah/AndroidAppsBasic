package com.example.startup.sayurku.persistence;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Startup on 3/2/17.
 */

public class Menu implements Parcelable {

    public String idMenu;
    public String name;
    public List<Item> item;



    public Menu()
    {
        idMenu="";
        name="";
        item= new ArrayList<Item>();
    }


    public int getMenuPrice()
    {
        int price=0;
        for(int i=0;i<item.size();i++)
        {
            price=price+item.get(i).getItemPrice();
        }
        return price;
    }


    public List<Item> getSelectedItem()
    {
        List<Item> selectedItem= new ArrayList<Item>();
        for(int i=0;i<item.size();i++)
        {
            if(item.get(i).qty>0)
            {
                selectedItem.add(item.get(i));
            }
        }
        return selectedItem;
    }

    public Menu (Parcel parcel) {
        this.idMenu = parcel.readString();
        this.name = parcel.readString();
        this.item = parcel.readArrayList(Item.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Required method to write to Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idMenu);
        dest.writeString(name);
        dest.writeList(item);
    }

    // Method to recreate a Question from a Parcel
    public static Creator<Menu> CREATOR = new Creator<Menu>() {

        @Override
        public Menu createFromParcel(Parcel source) {
            return new Menu(source);
        }

        @Override
        public Menu[] newArray(int size) {
            return new Menu[size];
        }

    };

    public int getMenuQty() {
        int qty=0;
        for(int i=0;i<item.size();i++)
        {
            qty=qty+item.get(i).getItemQty();
        }
        return qty;
    }
}

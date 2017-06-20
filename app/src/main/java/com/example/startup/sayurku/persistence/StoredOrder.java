package com.example.startup.sayurku.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.startup.sayurku.helper.OrderSQLiteHandler;
import com.google.gson.Gson;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Startup on 1/31/17.
 */

public class StoredOrder{

    // LogCat tag
    private static String TAG = StoredOrder.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    OrderSQLiteHandler orderSQLiteHandler;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "order_sayurku";

    public StoredOrder(Context context)
    {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static boolean containsId(List<Item> list, String id) {
        for (Item object : list) {
            if (object.name.contentEquals( id)) {
                return true;
            }
        }
        return false;
    }

    public List<Item> getItem()
    {
        Gson gson = new Gson();
        String json = pref.getString(PREF_NAME, "");
        if(json.equals(""))
        {
            return new ArrayList<Item>();
        }
        else
        {
            Order order=gson.fromJson(json,Order.class);
            return order.item;
        }




    }

    public String getItemJson()
    {
        Gson gson = new Gson();
        String json = pref.getString(PREF_NAME, "");
        if(json.equals(""))
        {
            return gson.toJson(new ArrayList<Item>());
        }
        else
        {
            Order order=gson.fromJson(json,Order.class);
            return gson.toJson(order.item);
        }
    }

    public boolean addItem(Item item)
    {
        item.qty=1;
        boolean val;
        Gson gson = new Gson();
        String json = pref.getString(PREF_NAME, "");
        Order order = gson.fromJson(json,Order.class);
        if(json.equals(""))
        {
                order = new Order(_context);
                order.item.add(item);;
                val=true;
        }
        else
        {

            if(containsId(order.item,item.name))
            {
                val=false;
            }
            else
            {
                order.item.add(item);;
                val=true;
            }

        }
        String json2 = gson.toJson(order);
        editor.putString(PREF_NAME, json2);
        editor.commit();


        return val;

    }

    public void deleteItem()
    {
        boolean val;
        Gson gson = new Gson();
        String json = pref.getString(PREF_NAME, "");
        Order order = gson.fromJson(json,Order.class);

        order = new Order(_context);

        String json2 = gson.toJson(order);
        editor.putString(PREF_NAME, json2);
        editor.commit();
    }

    public void deleteItem(int i)
    {
        boolean val;
        Gson gson = new Gson();
        String json = pref.getString(PREF_NAME, "");
        Order order = gson.fromJson(json,Order.class);

        order.item.remove(i);

        String json2 = gson.toJson(order);
        editor.putString(PREF_NAME, json2);
        editor.commit();
    }



    public String getRacapPrice()
    {
        Gson gson = new Gson();
        String json = pref.getString(PREF_NAME, "");
        Order order = gson.fromJson(json,Order.class);


        int price=0;
        for(int i=0;i<order.item.size();i++)
        {

            price=price+order.item.get(i).getItemPrice();
        }
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return formatter.format(price);
    }

    public int getRacapQty() {
        Gson gson = new Gson();
        String json = pref.getString(PREF_NAME, "");
        Order order = gson.fromJson(json,Order.class);

        int qty=0;
        for(int i=0;i<order.item.size();i++)
        {
            qty=qty+order.item.get(i).getItemQty();
        }

        String json2 = gson.toJson(order);
        editor.putString(PREF_NAME, json2);
        editor.commit();
        return qty;
    }

    public void plusOne(int i)
    {
        Gson gson = new Gson();
        String json = pref.getString(PREF_NAME, "");
        Order order = gson.fromJson(json,Order.class);

        order.item.get(i).plusOne();

        String json2 = gson.toJson(order);
        editor.putString(PREF_NAME, json2);
        editor.commit();
    }
    public void minOne(int i)
    {
        Gson gson = new Gson();
        String json = pref.getString(PREF_NAME, "");
        Order order = gson.fromJson(json,Order.class);

        order.item.get(i).minOne();

        String json2 = gson.toJson(order);
        editor.putString(PREF_NAME, json2);
        editor.commit();

    }





}

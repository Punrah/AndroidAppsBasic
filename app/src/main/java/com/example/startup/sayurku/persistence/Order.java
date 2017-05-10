package com.example.startup.sayurku.persistence;





import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Startup on 2/1/17.
 */

public class Order  {

    public User user;
    public String id_order;
    public String orderDate;
    public int price;
    public int priceCar;
    public int priceBike;
    public double distance;
    public LatLng pickupPosition;
    public LatLng dropoofPosition;
    public String pickupAddress;
    public String dropoffAddress;
    public Driver driver;
    public String pickupNote;
    public String dropoffNote;
    public String status;
    public int orderType;
    public String vehicle;
    public String payment_type;
    public int rating;

    public Order()
    {
        user = new User();
        id_order="";
        orderDate="";
        price=0;
        priceCar=0;
        priceBike=0;
        distance=0;
        pickupPosition = new LatLng(0,0);
        dropoofPosition=new LatLng(0,0);
        pickupAddress="";
        dropoffAddress="";
        driver = new Driver();
        pickupNote="";
        dropoffNote="";
        status="";
        orderType=0;
        vehicle="";
        payment_type="";
        rating=0;

    }



    public void setPrice(String priceString)
    {
        price=Integer.parseInt(priceString);
    }
    public String getPriceString() {
        return String.valueOf(price);
    }
    public String getDistanceString() {
        return String.valueOf(distance);
    }
    public String getPickupLatString() {
        return String.valueOf(pickupPosition.latitude);
    }

    public String getPickupLngString() {
        return String.valueOf(pickupPosition.longitude);
    }

    public String getDropoffLatString() {
        return String.valueOf(dropoofPosition.latitude);
    }

    public String getDropoffLngString() {
        return String.valueOf(dropoofPosition.longitude);
    }

    public String getPickupNoteString()
    {

            return pickupNote;

    }
    public String getDropoffNoteString()
    {

            return dropoffNote;

    }


    public void setPickupPlace(Place place)
    {
        pickupPosition=place.getLatLng();
        pickupAddress=place.getAddress().toString();
    }

    public void setDropoffPlace(Place place)
    {
        dropoofPosition=place.getLatLng();
        dropoffAddress=place.getAddress().toString();
    }

    public String getDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date= null;
        try {
            date = simpleDateFormat.parse(orderDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MMMM/dd/yy, HH:mm aa");
        return simpleDateFormat2.format(date);

    }

    public String getPrice()
    {
        DecimalFormat df = new DecimalFormat("#.00"); // Set your desired format here.
        return "R "+ df.format(price/100.0);
    }

    public String getOrderNo()
    {
        return "ORDER NO "+id_order;
    }


}

package com.example.startup.sayurku.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;

import com.example.startup.sayurku.persistence.Item;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Startup on 3/24/17.
 */

public  class Formater extends AppCompatActivity {

    public static String getPrice(String price)
    {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return formatter.format(Double.parseDouble(price));

    }

    public static String getDistance(String distance) {
        String value="";
        if(Double.parseDouble(distance)<1)
        {
            double p=Double.parseDouble(distance);
            DecimalFormat df = new DecimalFormat("#.0"); // Set your desired format here.
            value= "0"+df.format(p)+"KM";
        }
        else {
            double p=Double.parseDouble(distance);
            DecimalFormat df = new DecimalFormat("#.0"); // Set your desired format here.
            value=df.format(p)+"KM";

        }
        return value;

    }

        private static final Pattern EUROPEAN_DIALING_PLAN = Pattern.compile("^\\+|(00)|(0)");



        public static String phoneNumber(String number,String countryCode) {

            Phonenumber.PhoneNumber phoneNumber = null;
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            String finalNumber = null;
            String isoCode = phoneNumberUtil.getRegionCodeForCountryCode(Integer.parseInt(countryCode));
            boolean isValid = false;
            PhoneNumberUtil.PhoneNumberType isMobile = null;
            try {
                phoneNumber = phoneNumberUtil.parse(number, isoCode);
                isValid = phoneNumberUtil.isValidNumber(phoneNumber);
                isMobile = phoneNumberUtil.getNumberType(phoneNumber);

            } catch (NumberParseException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }


            if (isValid
                    && (PhoneNumberUtil.PhoneNumberType.MOBILE == isMobile || PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE == isMobile)) {
                finalNumber = phoneNumberUtil.format(phoneNumber,
                        PhoneNumberUtil.PhoneNumberFormat.E164).substring(1);
            }

            return finalNumber;
        }

        static AlertDialog.Builder alert;
        public static void viewDialog(Context context,String smsg)
        {
            alert = new AlertDialog.Builder(context);
            alert.setMessage(smsg);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            alert.show();
        }

        public static void viewDialog(Context context,String smsg,String titleMsg)
        {
            alert = new AlertDialog.Builder(context);
            alert.setTitle(titleMsg);
            alert.setMessage(smsg);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            alert.show();
        }

    public static boolean isValidEmailAddress(String email) {

        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher((CharSequence) email);
        return matcher.matches();

    }


    public static class OrderByPrice implements Comparator<Item> {

        public int compare(Item obj1, Item obj2) {
            return ((Integer)obj1.price).compareTo(obj2.price);
        }


    }

    public static class OrderByName implements Comparator<Item> {

        public int compare(Item obj1, Item obj2) {
            return obj1.name.compareTo(obj2.name);
        }

    }



}

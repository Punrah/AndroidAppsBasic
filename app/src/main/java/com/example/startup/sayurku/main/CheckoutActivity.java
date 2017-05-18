package com.example.startup.sayurku.main;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.startup.sayurku.AsyncTask.ImageAsyncTask;
import com.example.startup.sayurku.AsyncTask.MyAsyncTask;
import com.example.startup.sayurku.R;
import com.example.startup.sayurku.app.AppConfig;
import com.example.startup.sayurku.persistence.Item;
import com.example.startup.sayurku.persistence.Order;
import com.example.startup.sayurku.persistence.StoredOrder;
import com.example.startup.sayurku.persistence.User;
import com.example.startup.sayurku.persistence.UserGlobal;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    ImageView back;
    List<TextView> textViewsQty;
    List<LinearLayout> linearLayoutsCounter;
    List<TextView> textViewsTotalPrice;
    List <LinearLayout> convertViews;
    private LinearLayout linearLayoutList;
    StoredOrder order;
    List<Item> items;
    TextView sumPrice;
    TextView checkout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Checkout");

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        linearLayoutList = (LinearLayout) findViewById(R.id.list_item);
        sumPrice = (TextView) findViewById(R.id.sum_price);
        checkout = (TextView) findViewById(R.id.checkout);


        order = new StoredOrder(CheckoutActivity.this);
        fetchItemCheckout();

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.deleteItem();
                Toast.makeText(CheckoutActivity.this, "deleted", Toast.LENGTH_SHORT).show();
                finish();
            }
        });



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchItemCheckout()
    {


            textViewsQty = new ArrayList<TextView>();
            linearLayoutsCounter = new ArrayList<LinearLayout>();
           textViewsTotalPrice = new ArrayList<TextView>();
            linearLayoutList.removeAllViews();

            if (order.getItem().size() > 0) {

                for (int i = 0; i < order.getItem().size(); i++) {
                    LayoutInflater inflater = getLayoutInflater();
                    final LinearLayout convertView = (LinearLayout) inflater.inflate(R.layout.list_item_checkout, linearLayoutList, false);
                    TextView name = (TextView) convertView.findViewById(R.id.item_name);
                    TextView price = (TextView) convertView.findViewById(R.id.price);
                    TextView description = (TextView) convertView.findViewById(R.id.item_description);
                    ImageView imageView = (ImageView) convertView.findViewById(R.id.img_item);
                    final ImageView delete =(ImageView) convertView.findViewById(R.id.delete);
                    final ImageView minus = (ImageView) convertView.findViewById(R.id.minus);
                    final ImageView plus = (ImageView) convertView.findViewById(R.id.plus);
                    textViewsQty.add((TextView) convertView.findViewById(R.id.qty));
                    linearLayoutsCounter.add((LinearLayout)convertView.findViewById(R.id.counter));
                    final int j = i;
                    textViewsTotalPrice.add((TextView) convertView.findViewById(R.id.total_price));
                    name.setText(String.valueOf(order.getItem().get(i).name));
                    price.setText(order.getItem().get(i).getPrice());
                    description.setText(String.valueOf(order.getItem().get(i).description));
                    imageView.setTag(order.getItem().get(i).photo);
                    new ImageAsyncTask().execute(imageView);
                    textViewsQty.get(i).setText(String.valueOf(order.getItem().get(i).getQty()));
                    textViewsTotalPrice.get(i).setText(order.getItem().get(i).getItemPriceString());

                    minus.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            minusQty(j);
                        }

                    });

                    plus.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            plusQty(j);
                        }
                    });

                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteItem(j);
                            fetchItemCheckout();
                        }
                    });
                    linearLayoutList.addView(convertView);

                }
            }
            setRecapPrice();


        }

    public void minusQty(int i) {

        order.minOne(i);
        textViewsQty.get(i).setText(String.valueOf(order.getItem().get(i).getQty()));
        textViewsTotalPrice.get(i).setText(order.getItem().get(i).getItemPriceString());
        setRecapPrice();
    }

    public void plusQty(int i) {
        order.plusOne(i);
        textViewsQty.get(i).setText(String.valueOf(order.getItem().get(i).getQty()));
        textViewsTotalPrice.get(i).setText(order.getItem().get(i).getItemPriceString());
        setRecapPrice();
    }

    public void deleteItem(int i)
    {
        order.deleteItem(i);

    }



    private void setRecapPrice()
    {
sumPrice.setText(order.getRacapPrice());
    }



}

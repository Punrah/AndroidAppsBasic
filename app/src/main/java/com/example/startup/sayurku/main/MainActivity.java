package com.example.startup.sayurku.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.startup.sayurku.AsyncTask.ImageAsyncTask;
import com.example.startup.sayurku.AsyncTask.MyAsyncTask;
import com.example.startup.sayurku.R;
import com.example.startup.sayurku.app.AppConfig;
import com.example.startup.sayurku.app.Formater;
import com.example.startup.sayurku.helper.OrderSQLiteHandler;
import com.example.startup.sayurku.persistence.Item;
import com.example.startup.sayurku.persistence.Order;
import com.example.startup.sayurku.persistence.StoredOrder;
import com.example.startup.sayurku.persistence.User;
import com.example.startup.sayurku.persistence.UserGlobal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Menu menu;
    private LinearLayout linearLayoutList;
    private LinearLayout linearLayoutGridGenap;
    private LinearLayout linearLayoutGridGanjil;
    private List<Item> listItem=new ArrayList<Item>();


    AlertDialog levelDialog;

    final CharSequence[] sort = {" Termurah "," Termahal "," A-Z "," Z-A "};
    final CharSequence[] view = {" Grid "," List "};

    int orderChecked=2;
    int viewChecked=0;



    LinearLayout linearLayoutOrder;
    LinearLayout linearLayoutView;

    TextView textViewCategory;
    RelativeLayout checkout;
    TextView orderQty;
    StoredOrder order;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        textViewCategory = (TextView) toolbar.findViewById(R.id.category);
        checkout = (RelativeLayout) toolbar.findViewById(R.id.checkout);



        order= new StoredOrder(MainActivity.this);
        orderQty = (TextView) toolbar.findViewById(R.id.order_qty);
        setCart();









        linearLayoutList = (LinearLayout) findViewById(R.id.list_item);
        linearLayoutGridGanjil= (LinearLayout) findViewById(R.id.grid_item_ganjil);
        linearLayoutGridGenap = (LinearLayout) findViewById(R.id.grid_item_genap);

        linearLayoutOrder = (LinearLayout) findViewById(R.id.order);

        linearLayoutView = (LinearLayout) findViewById(R.id.view);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        menu = navigationView.getMenu();
        new fetchCategory().execute();



        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        final TextView name = (TextView)header.findViewById(R.id.name);
        final TextView email = (TextView)header.findViewById(R.id.email);

// Initialize Database

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            mDatabase = FirebaseDatabase.getInstance().getReference()
                    .child("users").child(user.getUid());
            ValueEventListener postListener = new ValueEventListener() {


                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    User user = dataSnapshot.getValue(User.class);
                    // [START_EXCLUDE]
                    name.setText(user.name);
                    email.setText(user.email);
                    // [END_EXCLUDE]
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    // [START_EXCLUDE]
                    Toast.makeText(MainActivity.this, "Failed to load post.",
                            Toast.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }
            };
            mDatabase.addListenerForSingleValueEvent(postListener);


        }


        linearLayoutOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setSingleChoiceItems(sort, orderChecked, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {


                        switch(item)
                        {
                            case 0:
                                orderChecked=item;
                                fetch();
                                break;
                            case 1:
                                orderChecked=item;
                                fetch();
                                break;
                            case 2:
                                orderChecked=item;
                                fetch();
                                break;
                            case 3:
                                orderChecked=item;
                                fetch();
                                break;

                        }
                        levelDialog.dismiss();
                    }
                });
                levelDialog = builder.create();
                levelDialog.show();
            }
        });

        linearLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setSingleChoiceItems(view, viewChecked, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {


                        switch(item)
                        {
                            case 0:

                                viewChecked=item;
                                fetch();
                                break;
                            case 1:

                                viewChecked=item;
                                fetch();
                                break;

                        }
                        levelDialog.dismiss();
                    }
                });


                levelDialog = builder.create();
                levelDialog.show();
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CheckoutActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        setCart();
    }

    private void setCart()
    {

        orderQty.setText(String.valueOf(order.getItem().size()));
    }



    private void orderItem()
    {
        Formater.OrderByPrice priceOrderer = new Formater.OrderByPrice();
        Formater.OrderByName nameOrderer = new Formater.OrderByName();

        if(orderChecked==0)
        {
            Collections.sort(listItem,priceOrderer);
        }
        else if(orderChecked==1)
        {
            Collections.sort(listItem,priceOrderer);
            Collections.reverse(listItem);
        }
        else if(orderChecked==2)
        {
            Collections.sort(listItem,nameOrderer);
        }
        else if(orderChecked==3)
        {
            Collections.sort(listItem,nameOrderer);
            Collections.reverse(listItem);
        }

    }


    private void fetch()
    {
        if(viewChecked==0)
        {
            fetchGrid();
        }
        else
        {
            fetchList();
        }
    }
    private void fetchList()
    {
        orderItem();
        linearLayoutList.removeAllViews();
        linearLayoutGridGenap.removeAllViews();
        linearLayoutGridGanjil.removeAllViews();

        if (listItem.size() > 0) {

            for (int i = 0; i < listItem.size(); i++) {
                final int j=i;
                LayoutInflater inflater = getLayoutInflater();
                final LinearLayout convertView = (LinearLayout) inflater.inflate(R.layout.list_item, linearLayoutList, false);
                TextView name = (TextView) convertView.findViewById(R.id.item_name);
                TextView price = (TextView) convertView.findViewById(R.id.price);
                TextView description = (TextView) convertView.findViewById(R.id.item_description);
                ImageView imageView = (ImageView) convertView.findViewById(R.id.img_item);

                name.setText(String.valueOf(listItem.get(i).name));
                price.setText(listItem.get(i).getPrice());
                description.setText(String.valueOf(listItem.get(i).description));
                imageView.setTag(listItem.get(i).photo);
                new ImageAsyncTask().execute(imageView);

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Start an alpha animation for clicked item
                        Animation animation1 = new AlphaAnimation(0.3f, 5.0f);
                        animation1.setDuration(800);
                        convertView.startAnimation(animation1);
                        viewDetailItem(listItem.get(j));
                    }
                });


                linearLayoutList.addView(convertView);

            }
        }
    }

    private void fetchGrid()
    {

        orderItem();
        linearLayoutList.removeAllViews();
        linearLayoutGridGenap.removeAllViews();
        linearLayoutGridGanjil.removeAllViews();

        if (listItem.size() > 0) {

            for (int i = 0; i < listItem.size(); i++) {
                final int j=i;
                if(i%2==0) {

                    LayoutInflater inflater = getLayoutInflater();
                    final LinearLayout convertView = (LinearLayout) inflater.inflate(R.layout.grid_item, linearLayoutGridGanjil, false);
                    TextView name = (TextView) convertView.findViewById(R.id.item_name);
                    TextView price = (TextView) convertView.findViewById(R.id.price);
                    ImageView imageView = (ImageView) convertView.findViewById(R.id.img_item);

                    name.setText(String.valueOf(listItem.get(i).name));
                    price.setText(listItem.get(i).getPrice());
                    imageView.setTag(listItem.get(i).photo);
                    new ImageAsyncTask().execute(imageView);
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Start an alpha animation for clicked item
                            Animation animation1 = new AlphaAnimation(0.3f, 5.0f);
                            animation1.setDuration(800);
                            convertView.startAnimation(animation1);
                            viewDetailItem(listItem.get(j));
                        }
                    });

                    linearLayoutGridGanjil.addView(convertView);
                }
                else
                {
                    LayoutInflater inflater = getLayoutInflater();
                    final LinearLayout convertView = (LinearLayout) inflater.inflate(R.layout.grid_item, linearLayoutGridGenap, false);
                    TextView name = (TextView) convertView.findViewById(R.id.item_name);
                    TextView price = (TextView) convertView.findViewById(R.id.price);
                    ImageView imageView = (ImageView) convertView.findViewById(R.id.img_item);

                    name.setText(String.valueOf(listItem.get(i).name));
                    price.setText(listItem.get(i).getPrice());
                    imageView.setTag(listItem.get(i).photo);
                    new ImageAsyncTask().execute(imageView);

                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Start an alpha animation for clicked item
                            Animation animation1 = new AlphaAnimation(0.3f, 5.0f);
                            animation1.setDuration(800);
                            convertView.startAnimation(animation1);
                            viewDetailItem(listItem.get(j));

                        }
                    });

                    linearLayoutGridGenap.addView(convertView);

                }

            }
        }
    }


    private void viewDetailItem(Item item)
    {
        Intent intent = new Intent(MainActivity.this,ItemActivity.class);
        intent.putExtra("item",item);
        startActivity(intent);
    }

    private class fetchCategory extends MyAsyncTask{


        List <String> category = new ArrayList<String>();


        @Override
        public Context getContext() {
            return MainActivity.this;
        }

        @Override
        public void setSuccessPostExecute() {
            for(int i=0;i<category.size();i++)
            {
                menu.add(R.id.category, i, Menu.NONE, category.get(i));
            }
            textViewCategory.setText(category.get(0));
            new fetchItem().execute();


        }

        @Override
        public void setFailPostExecute() {

        }

        public void postData() {
            String url = AppConfig.URL_CATEGORY;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("email", UserGlobal.getUser(MainActivity.this).email));
                nameValuePairs.add(new BasicNameValuePair("password", UserGlobal.getUser(MainActivity.this).password));
                nameValuePairs.add(new BasicNameValuePair("device_id", User.getDeviceId(MainActivity.this)));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                String jsonStr = EntityUtils.toString(entity, "UTF-8");

                if (jsonStr != null) {
                    try {

                        JSONObject jsonObj = new JSONObject(jsonStr);
                        JSONArray jsonArray = jsonObj.getJSONArray("category");
                        if(jsonArray.length()>0)
                        {
                            isSucces=true;
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                category.add(jsonArray.getString(i));
                            }
                        }
                        else
                        {
                            badServerAlert();
                        }

                    } catch (final JSONException e) {
                        badServerAlert();
                    }
                } else {
                    badServerAlert();
                }
            } catch (IOException e) {
                badInternetAlert();
            }
        }


    }


    private class fetchItem extends MyAsyncTask{




        @Override
        public Context getContext() {
            return MainActivity.this;
        }

        @Override
        public void setSuccessPostExecute() {

fetch();

        }

        @Override
        public void setFailPostExecute() {

        }

        public void postData() {
            listItem = new ArrayList<>();
            String url = AppConfig.URL_ITEM;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("email", UserGlobal.getUser(MainActivity.this).email));
                nameValuePairs.add(new BasicNameValuePair("password", UserGlobal.getUser(MainActivity.this).password));
                nameValuePairs.add(new BasicNameValuePair("device_id", User.getDeviceId(MainActivity.this)));
                //nameValuePairs.add(new BasicNameValuePair("device_id", User.getDeviceId(MainActivity.this)));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                String jsonStr = EntityUtils.toString(entity, "UTF-8");

                if (jsonStr != null) {
                    try {

                        JSONArray jsonArray = new JSONArray(jsonStr);
                        if(jsonArray.length()>0)
                        {
                            isSucces=true;
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonItem = jsonArray.getJSONObject(i);
                                Item item = new Item();
                                item.name = jsonItem.getString("nama");
                                item.description = jsonItem.getString("deskripsi");
                                item.price = jsonItem.getInt("harga");
                                item.metric = jsonItem.getString("satuan");
                                listItem.add(item);
                            }
                        }
                        else
                        {
                            badServerAlert();
                        }

                    } catch (final JSONException e) {
                        badServerAlert();
                    }
                } else {
                    badServerAlert();
                }
            } catch (IOException e) {
                badInternetAlert();
            }
        }


    }







    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
        if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this,
                    LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        String title = item.getTitle().toString();

        textViewCategory.setText(title);
        new fetchItem().execute();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}

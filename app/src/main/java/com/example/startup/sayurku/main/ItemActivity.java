package com.example.startup.sayurku.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.startup.sayurku.AsyncTask.ImageAsyncTask;
import com.example.startup.sayurku.R;
import com.example.startup.sayurku.persistence.Item;
import com.example.startup.sayurku.persistence.Order;
import com.example.startup.sayurku.persistence.StoredOrder;

public class ItemActivity extends AppCompatActivity {

    ImageView back;
    RelativeLayout checkout;
    Item item;

    TextView name;
    TextView description;
    TextView price;
    ImageView image;
    TextView addToCart;

    TextView orderQty;
    StoredOrder order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        checkout = (RelativeLayout) toolbar.findViewById(R.id.checkout);

        order = new StoredOrder(ItemActivity.this);


        orderQty = (TextView) toolbar.findViewById(R.id.order_qty);
        setCart();

        back = (ImageView) toolbar.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        name=(TextView) findViewById(R.id.item_name);
        description=(TextView) findViewById(R.id.item_description);
        price=(TextView) findViewById(R.id.price);
        image=(ImageView) findViewById(R.id.img_item);
        addToCart=(TextView) findViewById(R.id.add_to_cart);


        item=getIntent().getParcelableExtra("item");


        name.setText(item.name);
        description.setText(item.description);
        price.setText(item.getPrice());
        image.setTag(item.photo);
        new ImageAsyncTask().execute(image);


        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemActivity.this,CheckoutActivity.class);
                startActivity(intent);
            }
        });


        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(order.addItem(item)) {
                    Toast.makeText(ItemActivity.this, "sukses", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Toast.makeText(ItemActivity.this, "added", Toast.LENGTH_SHORT).show();
                }

            }
        });








    }

    private void setCart()
    {
        orderQty.setText(String.valueOf(order.getItem().size()));
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

}

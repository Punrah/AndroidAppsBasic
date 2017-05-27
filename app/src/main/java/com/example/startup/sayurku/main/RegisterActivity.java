/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
package com.example.startup.sayurku.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.startup.sayurku.AsyncTask.MyAsyncTask;
import com.example.startup.sayurku.R;
import com.example.startup.sayurku.app.AppConfig;
import com.example.startup.sayurku.app.Formater;
import com.example.startup.sayurku.helper.SessionManager;
import com.example.startup.sayurku.helper.UserSQLiteHandler;
import com.example.startup.sayurku.persistence.User;
import com.example.startup.sayurku.persistence.UserGlobal;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 123 ;

    private Button btnRegister;
    private TextView btnLinkToLogin;
    private EditText inputFullName;
    private EditText companyName;
    private EditText address;
    private TextView openmap;
    private EditText inputEmail;
    private EditText inputPhone;
    private EditText inputPassword;
    private EditText inputConfirmPassword;
    private LatLng placeAddress ;
    private SessionManager session;
    private UserSQLiteHandler db;

    TextView termOfService;
    TextView privacyPolicy;

    private TextView countryCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        inputFullName = (EditText) findViewById(R.id.name);
        companyName = (EditText) findViewById(R.id.company);
        address = (EditText) findViewById(R.id.address );
        openmap = (TextView) findViewById(R.id.open_map);
placeAddress = new LatLng(0,0);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPhone = (EditText) findViewById(R.id.phone);
        inputPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (TextView) findViewById(R.id.btnLinkToLoginScreen);
        countryCode = (TextView) findViewById(R.id.country_code);
        countryCode.setText("+"+GetCountryZipCode().toString()+" ");

        termOfService= (TextView) findViewById(R.id.term_of_service);
        privacyPolicy = (TextView) findViewById(R.id.privacy_policy);








        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new UserSQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }

        openmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                    startActivityForResult(builder.build(RegisterActivity.this), PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }

            }
        });



        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getText().toString().trim();
                String company = companyName.getText().toString().trim();
                String add = address.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String phone = inputPhone.getText().toString().trim();
                String formatedPhone = Formater.phoneNumber(inputPhone.getText().toString().trim(),GetCountryZipCode());
                String password = inputPassword.getText().toString().trim();

                if(name.isEmpty())
                {
                    inputFullName.setError(getString(R.string.empty_name_pop_up));
                }
                if(add.isEmpty())
                {
                    address.setError(getString(R.string.empty_address_pop_up));
                }
                else if(email.isEmpty())
                {
                    inputEmail.setError(getString(R.string.empty_email_pop_up));
                }
                else if(!Formater.isValidEmailAddress(email))
                {
                    inputEmail.setError(getString(R.string.email_format_pop_up));
                }
                else if(phone.isEmpty())
                {
                    inputPhone.setError(getString(R.string.empty_phone_pop_up));
                }
                else if(phone.length()<8)
                {
                    inputPhone.setError(getString(R.string.phone_format_pop_up));
                }
                else if(password.isEmpty())
                {
                    inputPassword.setError(getString(R.string.empty_password_pop_up));
                }
                else if(password.length()<8)
                {
                    inputPassword.setError(getString(R.string.minimum_characters_error_messages));
                }
                else if(!password.matches("[A-Za-z0-9]+"))
                {
                    inputPassword.setError(getString(R.string.must_be_alphanumerics_error_messages));
                }
                else
                {
                    User user = new User();
                    user.name=name;
                    user.company=company;
                    user.address=add;
                    user.latLng=placeAddress;
                    user.email=email;
                    user.phone=phone;
                    user.password=password;
                new registerUser(user).execute();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start an alpha animation for clicked item
                Animation animation1 = new AlphaAnimation(0.3f, 5.0f);
                animation1.setDuration(800);
                privacyPolicy.startAnimation(animation1);
                Intent i = new Intent(RegisterActivity.this,WebActivity.class);
                i.putExtra("title", "Ketentuan Layanan");
                i.putExtra("action","term_of_service");
                startActivity(i);

            }
        });
        termOfService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start an alpha animation for clicked item
                Animation animation1 = new AlphaAnimation(0.3f, 5.0f);
                animation1.setDuration(800);
                termOfService.startAnimation(animation1);
                Intent i = new Intent(RegisterActivity.this,WebActivity.class);
                i.putExtra("title", "Kebijakan Privasi");
                i.putExtra("action","privacy_policy");
                startActivity(i);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                placeAddress = place.getLatLng();
                address.setText(place.getAddress());


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.

                address.setText(status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }


    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private class registerUser extends MyAsyncTask {

        User user;

        public registerUser(User user) {
            this.user=user;
        }


            @Override
            public Context getContext () {
                return RegisterActivity.this;
            }



            @Override
            public void setSuccessPostExecute() {
                // Launch login activity
                Intent intent = new Intent(
                        RegisterActivity.this,
                        LoginActivity.class);
            }

        @Override
        public void setFailPostExecute() {

        }

        public void postData() {
            String url = AppConfig.URL_REGISTER;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                Gson gson = new Gson();
                nameValuePairs.add(new BasicNameValuePair("json", gson.toJson(user)));


                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                String jsonStr = EntityUtils.toString(entity, "UTF-8");

                if (jsonStr != null) {
                    try {
                        JSONObject obj = new JSONObject(jsonStr);
                        status = obj.getString("status");
                        if (status.contentEquals("1")) {
                            isSucces = true;
                        }
                        else if(status.contentEquals("2"))
                        {
                            badServerAlert();
                        }
                        else if(status.contentEquals("3"))
                        {
                            msgTitle="";
                            msg=getString(R.string.phone_number_taken_error_message);
                            alertType=DIALOG;

                        }
                        else if(status.contentEquals("4"))
                        {
                            msgTitle="";
                            msg=getString(R.string.email_taken_error_message);
                            alertType=DIALOG;
                        }
                        else {
                            msgTitle="";
                            msg=obj.getString("msg");
                            alertType=DIALOG;
                        }

                    } catch (final JSONException e) {
                        msgTitle="";
                        msg=e.getMessage();
                        alertType=DIALOG;
                    }
                } else {

                    badServerAlert();
                }


            } catch (IOException e) {

                badInternetAlert();


            }
        }


    }

    public String GetCountryZipCode(){
        String CountryID="";
        String CountryZipCode="";

        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID= manager.getSimCountryIso().toUpperCase();
        String[] rl= getResources().getStringArray(R.array.CountryCodes);
        for(int i=0;i<rl.length;i++){
            String[] g=rl[i].split(",");
            if(g[1].trim().equals(CountryID.trim())){
                CountryZipCode=g[0];
                break;
            }
        }
        return CountryZipCode;
    }


}

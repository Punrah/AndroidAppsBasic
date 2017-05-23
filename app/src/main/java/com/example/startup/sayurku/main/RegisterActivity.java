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
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.startup.sayurku.AsyncTask.MyAsyncTask;
import com.example.startup.sayurku.R;
import com.example.startup.sayurku.app.AppConfig;
import com.example.startup.sayurku.app.Formater;
import com.example.startup.sayurku.helper.SessionManager;
import com.example.startup.sayurku.helper.UserSQLiteHandler;
import com.example.startup.sayurku.persistence.User;
import com.example.startup.sayurku.persistence.UserGlobal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private TextView btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPhone;
    private EditText inputPassword;
    private EditText inputConfirmPassword;
    private SessionManager session;
    private UserSQLiteHandler db;

    TextView termOfService;
    TextView privacyPolicy;

    private TextView countryCode;

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        inputFullName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPhone = (EditText) findViewById(R.id.phone);
        inputPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (TextView) findViewById(R.id.btnLinkToLoginScreen);
        countryCode = (TextView) findViewById(R.id.country_code);
        countryCode.setText("+"+GetCountryZipCode().toString()+" ");

        termOfService= (TextView) findViewById(R.id.term_of_service);
        privacyPolicy = (TextView) findViewById(R.id.privacy_policy);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String phone = inputPhone.getText().toString().trim();
                String formatedPhone = Formater.phoneNumber(inputPhone.getText().toString().trim(),GetCountryZipCode());
                String password = inputPassword.getText().toString().trim();
                createAccount(email,password);

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
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        //showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            User user = new User();
                            user.id_customer=firebaseUser.getUid();
                            user.email=firebaseUser.getEmail();
                            user.name=inputFullName.getText().toString().trim();
                            user.phone=inputPhone.getText().toString().trim();
                            mDatabase.child("users").child(user.id_customer).setValue(user);
                            Intent intent = new Intent(RegisterActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private boolean validateForm() {
        boolean valid = false;




        String name = inputFullName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String phone = inputPhone.getText().toString().trim();
        String formatedPhone = Formater.phoneNumber(inputPhone.getText().toString().trim(),GetCountryZipCode());
        String password = inputPassword.getText().toString().trim();

        if(name.isEmpty())
        {
            Formater.viewDialog(RegisterActivity.this,getString(R.string.empty_name_pop_up));
        }
        else if(email.isEmpty())
        {
            Formater.viewDialog(RegisterActivity.this,getString(R.string.empty_email_pop_up));
        }
        else if(!Formater.isValidEmailAddress(email))
        {
            Formater.viewDialog(RegisterActivity.this,getString(R.string.email_format_pop_up));
        }
        else if(phone.isEmpty())
        {
            Formater.viewDialog(RegisterActivity.this,getString(R.string.empty_phone_pop_up));
        }
        else if(phone.length()<8)
        {
            Formater.viewDialog(RegisterActivity.this,getString(R.string.phone_format_pop_up));
        }
        else if(password.isEmpty())
        {
            Formater.viewDialog(RegisterActivity.this,getString(R.string.empty_password_pop_up));
        }
        else if(password.length()<8)
        {
            Formater.viewDialog(RegisterActivity.this,getString(R.string.minimum_characters_error_messages));
        }
        else if(!password.matches("[A-Za-z0-9]+"))
        {
            Formater.viewDialog(RegisterActivity.this,getString(R.string.must_be_alphanumerics_error_messages));
        }
        else
        {
            valid=true;
        }

        return valid;
    }



    private class registerUser extends MyAsyncTask {

        String name;
        String email;
        String phone;
        String password;
        String confirmPassword;
        String idCustomer;

        public registerUser(String name,  String email,  String phone,
                             String password) {
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.password = password;
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
                        VerifyActivity.class);
                intent.putExtra("id_customer",idCustomer);
                intent.putExtra("name", UserGlobal.getUser(getApplicationContext()).name);
                intent.putExtra("email",UserGlobal.getUser(getApplicationContext()).email);
                intent.putExtra("phone",UserGlobal.getUser(getApplicationContext()).phone);
                startActivity(intent);
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

                nameValuePairs.add(new BasicNameValuePair("name", name));
                nameValuePairs.add(new BasicNameValuePair("email", email));
                nameValuePairs.add(new BasicNameValuePair("phone", phone));
                nameValuePairs.add(new BasicNameValuePair("pass1", password));
                nameValuePairs.add(new BasicNameValuePair("pass2", password));


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
                            idCustomer = obj.getString("id_customer");
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

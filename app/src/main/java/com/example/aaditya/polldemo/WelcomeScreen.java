package com.example.aaditya.polldemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WelcomeScreen extends Activity {

    boolean doubleBackToExitPressedOnce = false;
    TextView appname, detail1, detail2, detail3;
    Button signin;
    TextView register, forget;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        appname = (TextView) findViewById(R.id.appname);
        detail1 = (TextView) findViewById(R.id.detail1);
        detail2 = (TextView) findViewById(R.id.detail2);
        detail3 = (TextView) findViewById(R.id.detail3);
        signin = (Button) findViewById(R.id.main_login);
        register = (TextView) findViewById(R.id.main_register);
        forget = (TextView) findViewById(R.id.main_forget);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/comicbold.ttf");
        Typeface custom_font1 = Typeface.createFromAsset(getAssets(), "fonts/Sansation-Regular.ttf");
        appname.setTypeface(custom_font);
        detail1.setTypeface(custom_font1);
        detail2.setTypeface(custom_font1);
        detail3.setTypeface(custom_font1);
        signin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent i = new Intent(WelcomeScreen.this, Signin.class);
                startActivity(i);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent i = new Intent(WelcomeScreen.this, Register.class);
                startActivity(i);
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent i = new Intent(WelcomeScreen.this, ForgetPassword.class);
                startActivity(i);
            }
        });
        if(isNetworkAvailable(this)==false)
        {
            Toast.makeText(this, "Please connect to Internet", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            // Clear your session, remove preferences, etc.
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,0);
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}



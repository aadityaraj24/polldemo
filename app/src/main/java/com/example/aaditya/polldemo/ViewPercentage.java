package com.example.aaditya.polldemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ViewPercentage extends AppCompatActivity {
    String Que,Opt1,Opt2;
    TextView TNo,TYes,Tshow;
    int Integeropt1,Integeropt2;
    Button yes,no;

    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_percentage);
        Que = getIntent().getStringExtra("que");
        Opt1=getIntent().getStringExtra("opt1");
        Opt2=getIntent().getStringExtra("opt2");
        TYes=(TextView)findViewById(R.id.TYes);
        Tshow=(TextView)findViewById(R.id.Tshow);

        TNo=(TextView)findViewById(R.id.TNo);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/comicbold.ttf");
        Typeface custom_font1 = Typeface.createFromAsset(getAssets(), "fonts/Sansation-Regular.ttf");
        TYes.setTypeface(custom_font);
        Tshow.setTypeface(custom_font1);
        TNo.setTypeface(custom_font);

        Integeropt1=Integer.parseInt(Opt1);
        Integeropt2=Integer.parseInt(Opt2);
       int total= Integeropt1+Integeropt2;
        float yes=(float)Integeropt1/total;
        float no=(float)Integeropt2/total;
        TYes.setText(""+(yes*100)+" %");
        TNo.setText(""+(no*100)+" %");





    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}

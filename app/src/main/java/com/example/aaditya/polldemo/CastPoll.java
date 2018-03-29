package com.example.aaditya.polldemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Handler;
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

public class CastPoll extends AppCompatActivity {
String Que,Opt1,Opt2;
    TextView Tque,Thanku;
    int Integeropt1,Integeropt2;
    Button yes,no;
    boolean doubleBackToExitPressedOnce = false;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast_poll);
        Que = getIntent().getStringExtra("que");
        Opt1=getIntent().getStringExtra("opt1");
        Opt2=getIntent().getStringExtra("opt2");
        Thanku=(TextView)findViewById(R.id.Thanku);
        Tque=(TextView)findViewById(R.id.Tque);
        yes=(Button)findViewById(R.id.Byes);
        no=(Button)findViewById(R.id.Bno);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/comicbold.ttf");
        Typeface custom_font1 = Typeface.createFromAsset(getAssets(), "fonts/Sansation-Regular.ttf");
        Tque.setTypeface(custom_font);
        Tque.setText(""+Que);
        Integeropt1=Integer.parseInt(Opt1);
        Integeropt2=Integer.parseInt(Opt2);





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

    public void yes(View v) {
        Integeropt1++;
        Opt1=Integer.toString(Integeropt1);
        if (isNetworkAvailable(this) == false) {
            Toast.makeText(this, "Please connect to Internet", Toast.LENGTH_SHORT).show();
        } else
        {
            Thanku.setVisibility(View.VISIBLE);
            Tque.setVisibility(View.INVISIBLE);
            yes.setVisibility(View.INVISIBLE);
            no.setVisibility(View.GONE);
            Thanku.setText("Thanku!!");
            BackGround b = new BackGround();
            b.execute(Que,Opt1,Opt2);
        }
    }
    public void no(View v) {
        Integeropt2++;
        Thanku.setVisibility(View.VISIBLE);
        Tque.setVisibility(View.INVISIBLE);
        yes.setVisibility(View.INVISIBLE);
        no.setVisibility(View.GONE);

        Tque.setText(""+Integeropt2);
        Opt2=Integer.toString(Integeropt2);
        if (isNetworkAvailable(this) == false) {
            Toast.makeText(this, "Please connect to Internet", Toast.LENGTH_SHORT).show();
        } else {
            Thanku.setText("Thanku!!");
            BackGround b = new BackGround();
            b.execute(Que,Opt1,Opt2);
        }
    }
    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String que = params[0];
            String opt1 = params[1];
            String opt2 = params[2];

            String data="";
            int tmp;


            try {
                URL url = new URL("http://aadee.000webhostapp.com/update_poll.php");
                String urlParams = "que="+que+"&opt1="+opt1+"&opt2="+opt2;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();
                InputStream is = httpURLConnection.getInputStream();
                while((tmp=is.read())!=-1){
                    data+= (char)tmp;
                }
                is.close();
                httpURLConnection.disconnect();

                return data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {


            //Toast.makeText(SecondFragment.this,"Data saved",Toast.LENGTH_SHORT).show();



        }
    }
    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}

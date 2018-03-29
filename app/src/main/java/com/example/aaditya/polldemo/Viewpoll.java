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
import android.widget.EditText;
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

public class Viewpoll extends AppCompatActivity {

    EditText id;
    TextView test;
    Button Bsubmit;
    String Id;
    ProgressDialog progress;
    String QUE=null,OPT1=null,OPT2=null;
    Context ctx=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpoll);
        id = (EditText) findViewById(R.id.Esurvey);
        Bsubmit = (Button) findViewById(R.id.Bsubmit);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/comicbold.ttf");
        Typeface custom_font1 = Typeface.createFromAsset(getAssets(), "fonts/Sansation-Regular.ttf");
        id.setTypeface(custom_font);
    }

    public void submit(View v) {
        Id = id.getText().toString();
        if (isNetworkAvailable(this) == false) {
            Toast.makeText(this, "Please connect to Internet", Toast.LENGTH_SHORT).show();
        } else {
            progress = new ProgressDialog(this);
            progress.setMessage("Logging In");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setProgress(0);
            progress.show();
            final int totalProgressTime = 100;
            final Thread t = new Thread() {
                @Override
                public void run() {
                    int jumpTime = 0;

                    while (jumpTime < totalProgressTime) {
                        try {
                            sleep(200);
                            jumpTime += 5;
                            progress.setProgress(jumpTime);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            };
            t.start();
            BackGround b = new BackGround();
            b.execute(Id);
        }
    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://aadee.000webhostapp.com/cast_poll.php");
                String urlParams = "id=" + id;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }
                is.close();
                httpURLConnection.disconnect();

                return data;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: Error " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception:Error " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (progress.isShowing())
                progress.dismiss();
            String err = null;
            //  progress1.setVisibility(View.GONE);
            try {
                JSONObject root = new JSONObject(s);
                JSONObject user_data = root.getJSONObject("user_data");
                QUE = user_data.getString("que");
                OPT1=user_data.getString("opt1");
                OPT2=user_data.getString("opt2");


            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: " + e.getMessage();
            }
            Intent i = new Intent(ctx, ViewPercentage.class);
            i.putExtra("que", QUE);
            i.putExtra("opt1", OPT1);
            i.putExtra("opt2", OPT2);
            //i.putExtra("err", err);
            //  if(NAME.equals(Name) && PASSWORD.equals(Password))
            startActivity(i);
        }

    }
    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}

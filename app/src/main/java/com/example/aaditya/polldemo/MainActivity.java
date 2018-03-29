package com.example.aaditya.polldemo;

import android.app.Activity;
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

public class MainActivity extends Activity {
    EditText que;
    TextView Surveyid;
    String Que,Test;
    String ID="aadi",QUE=null;
    Context ctx=this;
    Button createpoll,castpoll,viewpoll;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        que=(EditText)findViewById(R.id.queET);
        Surveyid=(TextView)findViewById(R.id.surveyid);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/comicbold.ttf");
        Typeface custom_font1 = Typeface.createFromAsset(getAssets(), "fonts/Sansation-Regular.ttf");
        que.setTypeface(custom_font);
        createpoll=(Button)findViewById((R.id.create)) ;

     //   final String Email="aadi@gmail.com";
       // Question = getArguments().getString("email");


    }
    public void register_register(View v){
        Que = que.getText().toString();
       // Test = test.getText().toString();
        if(isNetworkAvailable(this)==false)
        {
            Toast.makeText(this, "Please connect to Internet", Toast.LENGTH_SHORT).show();
        }
        else {
            progress = new ProgressDialog(this);
            progress.setMessage("Registering");
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
            b.execute(Que);
        }

    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            //String email = params[0];
            String que = params[0];
           // String test= params[1];
            String data="";
            int tmp;


            try {
                URL url = new URL("http://aadee.000webhostapp.com/create_poll.php");
                String urlParams = "que="+que;
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
            if (progress.isShowing())
                progress.dismiss();
           /* if(s.equals("")){
                s="Data saved successfully.";

            }
            */
            String err = null;
            //  progress1.setVisibility(View.GONE);
            try {
                JSONObject root = new JSONObject(s);
                JSONObject user_data = root.getJSONObject("user_data");
                ID= user_data.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: " + e.getMessage();
            }
            Toast.makeText(MainActivity.this,"Your survey id is:"+ID,Toast.LENGTH_SHORT).show();
            Surveyid.setText("Your survey id is:"+ID);
        }
    }
    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}

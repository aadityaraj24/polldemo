package com.example.aaditya.polldemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Register extends Activity {

    EditText name, password, email,answers;
    String Name, Password, Email,Answers, Questions;
    Context ctx=this;
    Spinner questions;
    ProgressDialog progress;
    ArrayAdapter<CharSequence> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = (EditText) findViewById(R.id.register_name);
        password = (EditText) findViewById(R.id.register_password);
        email = (EditText) findViewById(R.id.register_email);
        answers = (EditText) findViewById(R.id.register_securityanswers);
        questions = (Spinner) findViewById(R.id.register_securityquestions);
        adapter = ArrayAdapter.createFromResource(this, R.array.questions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        questions.setAdapter(adapter);
        questions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {
                Questions =(String) questions.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    public void register_register(View v){
        Name = name.getText().toString();
        Password = password.getText().toString();
        Email = email.getText().toString();
        Answers=answers.getText().toString();
        if(Name.equals("") || Email.equals("")|| Password.equals("")||Answers.equals(""))
            Toast.makeText(Register.this, "Enter all the details", Toast.LENGTH_SHORT).show();
        else if(isNetworkAvailable(this)==false)
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
            b.execute(Name, Password, Email, Answers, Questions);
        }

    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String password = params[1];
            String email = params[2];
            String answers= params[3];
            String questions= params[4];
            String data="";
            int tmp;

            try {
                URL url = new URL("http://aadee.000webhostapp.com/register.php");
                String urlParams = "name="+name+"&password="+password+"&email="+email+"&answers="+answers+"&questions="+questions;

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
            if(s.equals("")){
                s="Data saved successfully.";
            }
            Toast.makeText(ctx,"Data saved successfully.", Toast.LENGTH_LONG).show();
            Intent i = new Intent(Register.this, Signin.class);
            startActivity(i);
        }
    }
    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}

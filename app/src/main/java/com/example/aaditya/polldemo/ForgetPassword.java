package com.example.aaditya.polldemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.Properties;



public class ForgetPassword extends Activity {
    EditText email, answers;
    TextView questions;
    String Email, Answers;
    ProgressDialog pdialog;
    Button forward;
    Context ctx;
    String NAME = null, PASSWORD = null, EMAIL = null , ANSWERS=null, QUESTIONS=null;
    Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        email = (EditText) findViewById(R.id.forget_email);
        forward= (Button)findViewById(R.id.forward);
        answers = (EditText) findViewById(R.id.forget_answers);
        questions=(TextView) findViewById(R.id.forget_questions);
        //  password = (EditText) findViewById(R.id.forget_password);


        forward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (Answers.equals(ANSWERS)) {
                    Intent i = new Intent(ForgetPassword.this, ChangePassword.class);
                    i.putExtra("name", NAME);
                    i.putExtra("password", PASSWORD);
                    i.putExtra("email", EMAIL);

                    startActivity(i);
                }
                else {
                    if (Answers.equals(""))
                        Toast.makeText(ForgetPassword.this, "Please enter your answer", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(ForgetPassword.this, "You dont remember your security answer.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }







    public void forget_submit(View v)
    {
        Email = email.getText().toString();
        //forward.setVisibility(View.VISIBLE);
        //Answers = answers.getText().toString();
        BackGround b = new BackGround();
        b.execute(Email);
    }




    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String email = params[0];
            // String password = params[1];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://aadee.000webhostapp.com/forget.php");
                String urlParams = "email=" + email;

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
            String err = null;
            try {
                JSONObject root = new JSONObject(s);
                JSONObject user_data = root.getJSONObject("user_data");
                EMAIL = user_data.getString("email");
                NAME=user_data.getString("name");

                ANSWERS = user_data.getString("answers");
                QUESTIONS=user_data.getString("questions");
                //EMAIL = user_data.getString("email");
            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: " + e.getMessage();
            }
            if(EMAIL.equals(Email)) {
                questions.setVisibility(View.VISIBLE);
                questions.setText(QUESTIONS);
                answers.setVisibility(View.VISIBLE);
                Answers = answers.getText().toString();
                if (Answers.equals(ANSWERS)) {
                    Intent i = new Intent(ForgetPassword.this, ChangePassword.class);
                    i.putExtra("name", NAME);
                    i.putExtra("password", PASSWORD);
                    i.putExtra("email", EMAIL);
                    i.putExtra("err", err);
                    startActivity(i);
                }
                else  {
                    if (Answers.equals(""))
                        Toast.makeText(ForgetPassword.this, "Please enter your answer", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(ForgetPassword.this, "You dont remember your security answer.", Toast.LENGTH_SHORT).show();
                }
            }

            else
                Toast.makeText(ForgetPassword.this,"Data not found",Toast.LENGTH_SHORT).show();

            //Toast.makeText(ForgetPassword.this,ANSWERS,Toast.LENGTH_SHORT).show();


        /*    Intent i = new Intent(ctx, Home.class);
            i.putExtra("name", NAME);
            i.putExtra("password", PASSWORD);
            i.putExtra("email", EMAIL);
            i.putExtra("err", err);
            if(NAME.equals(Name) && PASSWORD.equals(Password))
                Toast.makeText(ForgetPassword.this,"Match",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(ForgetPassword.this,"No match",Toast.LENGTH_SHORT).show();
*/
        }
    }

}

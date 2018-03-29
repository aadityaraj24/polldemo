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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class Signin extends Activity {
    EditText name, password;
    String Name, Password;
    TextView appname;
    Context ctx = this;
    String NAME = null, PASSWORD = null, EMAIL = null;
    RadioGroup radioGroup;
    ProgressDialog progress;

    Button title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

//Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//set content view AFTER ABOVE sequence (to avoid crash)
        //  this.setContentView(R.layout.your_layout_name_here);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        name = (EditText) findViewById(R.id.name);
        // final ProgressBar progress1 = (ProgressBar)findViewById(R.id.progressBar2);
        // progress1.setVisibility(View.VISIBLE);
        password = (EditText) findViewById(R.id.password);
        title=(Button)findViewById(R.id.title);
        RadioButton newaccount=(RadioButton)findViewById(R.id.radio0);
        RadioButton existingaccount=(RadioButton)findViewById(R.id.radio1);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
        radioGroup.check(R.id.radio1);

        title.setTransformationMethod(null);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/comicbold.ttf");

        title.setTypeface(custom_font);
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio0:
                if (checked) {

                    Intent i = new Intent(ctx, Register.class);
                    startActivity(i);
                    radioGroup.check(R.id.radio1);
                    break;

                }


        }
    }

    public void main_register(View v) {
        startActivity(new Intent(this, Register.class));
    }
    public void main_forget(View v) {
        startActivity(new Intent(this, ForgetPassword.class));
    }

    public void main_login(View v) {
        Name = name.getText().toString();
        Password = password.getText().toString();
        if(isNetworkAvailable(this)==false)
        {
            Toast.makeText(this, "Please connect to Internet", Toast.LENGTH_SHORT).show();
        }
        else {
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
            b.execute(Name, Password);
        }
    }




    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String password = params[1];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://aadee.000webhostapp.com/login.php");
                String urlParams = "name=" + name + "&password=" + password;

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
                NAME = user_data.getString("name");
                PASSWORD = user_data.getString("password");
                EMAIL = user_data.getString("email");
            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: " + e.getMessage();
            }

            if(Name.equals("") || Password.equals("") )
                Toast.makeText(Signin.this,"Enter somme credential",Toast.LENGTH_SHORT).show();
            else if(NAME != null && !Name.isEmpty()) {
                if (NAME.equals(Name) && PASSWORD.equals(Password)) {
                    Intent i = new Intent(ctx, Home.class);
                    i.putExtra("name", NAME);
                    i.putExtra("password", PASSWORD);
                    i.putExtra("email", EMAIL);
                    i.putExtra("err", err);
                    //  if(NAME.equals(Name) && PASSWORD.equals(Password))
                    startActivity(i);
                }
                else if (Name.equals(NAME) && !Password.equals(PASSWORD))
                    Toast.makeText(Signin.this, "wrong password", Toast.LENGTH_SHORT).show();

            }else
                Toast.makeText(Signin.this,"user name not exist",Toast.LENGTH_SHORT).show();

              /*  if(NAME!=null && !NAME.isEmpty()) {
                    Intent i = new Intent(ctx, Home.class);
                    i.putExtra("name", NAME);
                    i.putExtra("password", PASSWORD);
                    i.putExtra("email", EMAIL);
                    i.putExtra("err", err);
                    //  if(NAME.equals(Name) && PASSWORD.equals(Password))
                    startActivity(i);
                }
               else
                   Toast.makeText(MainActivity.this,"wrong password",Toast.LENGTH_SHORT).show();
*/

        }
    }
    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}

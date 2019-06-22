package com.example.android.demo;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;


public class LoginActivity extends AppCompatActivity {


    private EditText username;
    private EditText password;
    private Button sign_in;
    private TextView sign_up;
    private static String response = null;
    Handler handler = new Handler();

    private static final Pattern username_PATTERN =
            Pattern.compile("^" +
                    "[A-Za-z]+(?:[ _][A-Za-z0-9]+)*$");

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "([a-zA-Z0-9]+)" +      //any letter
                    "*$");

    AsyncT asyncT = null;
    RelativeLayout Main_layout ;
    Toolbar toolbar;

    // tests variable
    static String evalue ;
    static boolean active = false;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        Main_layout = (RelativeLayout) findViewById(R.id.Main_layout);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        sign_in = (Button) findViewById(R.id.sign_in);
        sign_up = (TextView) findViewById(R.id.link_signup);

        sign_in.setEnabled(false);

        Validate_Inputs_And_Enable_Buttons();
    }

    public void Validate_Inputs_And_Enable_Buttons() {

        Main_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                if (validatePassword() && validateUsername()) {
                    sign_in.setEnabled(true);
                }
                return false;
            }
        });
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
    /* When focus is lost check that the text field
    * has valid values.
    */
                if (!hasFocus) {
                    validateUsername();
                }
            }
        });
        username.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                evalue = "username";
                Log.d("test touch " , String.valueOf(true));
                return false;
            }
        });
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    Log.d( "test " , String.valueOf(imm.isActive()));
                    if (validatePassword() && validateUsername()) {
                        hideKeyboard();
                        sign_in.setEnabled(true);
                    }
                    return true;
                }

                else if(actionId == EditorInfo.IME_ACTION_PREVIOUS){
                    InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    Log.d( "test " , String.valueOf(imm.isActive()));
                    if (validatePassword() && validateUsername()) {
                        hideKeyboard();
                        sign_in.setEnabled(true);
                    }
                    return true;
                }
                return false;
            }

        });
        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                evalue = "password";
                return false;
            }
        });
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private boolean validateUsername() {
        String usernameInput = username.getText().toString().trim();

        if (usernameInput.isEmpty()) {
            username.setError("Field can't be empty");
            return false;
        } else if (!username_PATTERN.matcher(usernameInput).matches()) {
            username.setError("Please enter a valid user name");
            return false;
        } else {
            username.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = password.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            password.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            password.setError("Password too weak");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    public void sign_in(View v) {

        sign_in.setEnabled(false);
        sign_up.setEnabled(false);

       /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
         */
        Log.d("confirmInput : ", "true");

        if (confirmInputData()) {
            confirm_response();
            //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        sign_in.setEnabled(true);
        sign_up.setEnabled(true);
    }

    public boolean confirmInputData() {

        callAsync();
        if (asyncT != null && asyncT.isCancelled())
            Log.d("thread ", "cancel final");
        Log.d("confirmInputData : ", "true");
        return true;

    }

    public void confirm_response() {
        Log.d("Validate_Response : ", "true");

        if (response != null) {
            Log.d(" final 2 before check: ", response);
            if (!response.contains("error"))
            {
                Log.d("Server response", "response hasn't contain error message");
            }
            else {
                String response22[];
                response22 = ((response.substring(1, response.length() - 2)).split(","));

                String response_error = response22[0].split(":")[1];
                if (response_error.contains("nothing")) {
                    if (!response.contains("token"))
                        Log.d("Server response", "response hasn't contain token message");
                    else if (!response.contains("user_found"))
                        Log.d("Server response", "response hasn't contain user message");
                    else {
                        String response_token = response22[1].split(":")[1];
                        String response_user = response22[2].split(":")[1];
                        if (response_user.contains("0"))
                            Toast.makeText(this, "invalid user name or password ", Toast.LENGTH_SHORT).show();
                        else {
                            Session_Token.setToken(response_token);
                            Intent intent = new Intent(this, Profile.class);
                            intent.putExtra("Username", username.getText().toString());
                            startActivity(intent);
                        }
                    }
                } else {
                    if (!response.contains("error_message"))
                        Log.d("Server response", "response hasn't contain error message");
                    else if (!response.contains("successful"))
                        Log.d("Server response", "response hasn't successful message");
                    String error_message = response22[1].split(":")[1];
                    Log.d("error message", error_message);
                    Toast.makeText(this, (error_message), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            if (Get_Connection_Status.getAPIConnection()) {
                Log.d("Server ", "getAPIConnection() true .");
                Toast.makeText(this, ("Server :- didn't recieve response ."), Toast.LENGTH_LONG).show();
            } else if (!Get_Connection_Status.getInternetConnection()) {
                Log.d("Connection ", "didn't establish ,please check your internet .");
                Toast.makeText(this, ("please check your internet connection and try again ."), Toast.LENGTH_LONG).show();
                Get_Connection_Status.setAPIConnection(!Get_Connection_Status.getAPIConnection());
                Get_Connection_Status.setInternetConnection(!Get_Connection_Status.getInternetConnection());
            } else {
                Log.d("Connection ", "didn't establish ");
                Toast.makeText(this, ("Connection didn't establish ."), Toast.LENGTH_LONG).show();
                Get_Connection_Status.setAPIConnection(!Get_Connection_Status.getAPIConnection());
            }
        }
    }

    public void callAsync() {
        asyncT = new AsyncT();
        try {
            response = asyncT.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        asyncT.cancel(true);
        Log.d("callAsync ", "true");
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            Log.d("online ", " not ");
        }
        return false;
    }

    class AsyncT extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("thread ", "begin");
        }

        @Override
        protected String doInBackground(Void... params) {
            if (!isOnline()) {
                Log.d("thread ", "begin");
                Get_Connection_Status.setInternetConnection(false);
                Get_Connection_Status.setAPIConnection(!Get_Connection_Status.getAPIConnection());
                Log.d("thread ", "begin");
                return null;
            }
            String data = null;
            try {
                URL url = new URL(Get_Connection_Status.getUrl_api() + "api/login"); //Enter URL here

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
                httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setConnectTimeout(4000);
                httpURLConnection.connect();
                Log.d("thread ", "back");

                JSONObject jsonObject = formatInputAsJson();

                OutputStream os = httpURLConnection.getOutputStream();
                os.write(jsonObject.toString().getBytes("UTF-8"));
                os.close();

                Log.d("thread ", "back");

                StringBuilder sb = new StringBuilder();
                int HttpResult = httpURLConnection.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    data = sb.toString();
                } else {
                    System.out.println(httpURLConnection.getResponseMessage());

                }
                httpURLConnection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {

                Get_Connection_Status.setAPIConnection(!Get_Connection_Status.getAPIConnection());
            }
            return data;
        }


    }

    private JSONObject formatInputAsJson() {

        final JSONObject root = new JSONObject();
        try {
            root.put("username", username.getText().toString());
            root.put("password", password.getText().toString());
            return root;
        } catch (JSONException e) {
            Log.d("JWP", "Can not format json");
        }
        return null;
    }

    public void sign_up(View v) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Login Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }
}


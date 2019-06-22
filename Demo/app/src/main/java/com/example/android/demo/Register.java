package com.example.android.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import static com.example.android.demo.R.id.Main_layout;
import static com.example.android.demo.R.id.sign_in;

public class Register extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText confirm_password;
    private EditText first_name;
    private EditText last_name;
    private EditText age;
    private EditText email;
    private EditText phone;


    Toolbar toolbar ;
    RelativeLayout Main_layout ;
    ArrayList<Boolean> validation;
    Handler handler = new Handler();
    private static String response=null;
    Register.AsyncT asyncT = null;
    private Button sign_up;

    private static final Pattern username_PATTERN =
            Pattern.compile("^"+
                    "[A-Za-z0-9]+(?:[ _][A-Za-z0-9]+)*$" );

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "([a-zA-Z0-9]+)" +      //any letter
                    "$");

    private static final Pattern firstname_PATTERN =
            Pattern.compile(
                    "^[a-zA-Z]{5,}$" );

    private static final Pattern lastname_PATTERN =
            Pattern.compile(
                    "^[a-zA-Z]{5,}$" );

    private static final Pattern age_PATTERN =
            Pattern.compile(
                    "^([1-9][0-9])$"
                     );

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(
                    "[a-zA-Z_0-9]+@gmail.com|[a-zA-Z_0-9]+@yahoo.com"
            );

    private static final Pattern phone_PATTERN =
            Pattern.compile(
                    "^011[0-9]{8}$"
                    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_register);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        Main_layout = (RelativeLayout) findViewById(R.id.Main_layout);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        confirm_password = (EditText) findViewById(R.id.confirm_password);
        first_name = (EditText) findViewById(R.id.first_name);
        last_name = (EditText) findViewById(R.id.last_name);
        email = (EditText) findViewById(R.id.email);
        age = (EditText) findViewById(R.id.age);
        phone = (EditText) findViewById(R.id.phone);

        sign_up = (Button) findViewById(R.id.sign_up);
        validation = new ArrayList<>();
        for(int i=0;i<6;i++)
            validation.add(false);
        /*
        age = (EditText) findViewById(R.id.age);
        phone = (EditText) findViewById(R.id.phone);
         */
        Validation();

    }

    private boolean validate(final String validation_string , Pattern pattern , final EditText editText){
        String Input = editText.getText().toString().trim();

        if (Input.isEmpty()) {
            editText.setError("Field can't be empty");
            return false;
        } else if (!pattern.matcher(Input).matches()) {
            editText.setError("Please enter a valid " + validation_string);
            return false;
        } else {
            editText.setError(null);
            return true;
        }
    }

    public void Validation() {

        Main_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                if(!validation.contains(false))
                {
                    sign_up.setEnabled(true);
                }
                return false;
            }
        });

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validation.set(0,validateUsername());
                }
            }
        });
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validation.set(1,validateEmail());
                }
            }
        });
        first_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validation.set(2,validatefirstname());
                }
            }
        });
        last_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validation.set(3,validatelastname());
                }
            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validation.set(4,validatePassword());
                }
            }
        });
        confirm_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    validation.set(5,confirm_password());
                   // Log.d("sdfsdf" , String.valueOf(validation.size()));
                    if(!validation.contains(false))
                    {
                        //Log.d("sdfsdf" , "sdfdsfadadasdasdasdadad");
                        hideKeyboard();
                        sign_up.setEnabled(true);
                    }
                    return true;
                }
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

    private boolean validateUsername() { return validate("user name",username_PATTERN,username); }

    private boolean validateEmail() {
        return validate("email",EMAIL_PATTERN,email);
    }

    private boolean validatePassword() {
        return validate("password",PASSWORD_PATTERN,password);
    }

    private boolean confirm_password(){
        String Input = confirm_password.getText().toString().trim();
        if (Input.equals(password.getText().toString())){
            return true;
        }
        Toast.makeText(this, "does not match with password field", Toast.LENGTH_LONG).show();
        return false;
    }

    private boolean validatefirstname() {
        return validate("first name",firstname_PATTERN,first_name);
    }

    private boolean validatelastname() {
        return validate("last name",lastname_PATTERN,last_name);
    }

    private boolean validatephone() {
        return validate("phone",phone_PATTERN,phone);
    }

    private boolean validateage() {
        return validate("age",age_PATTERN,age);
    }

    public void confirmInput(View v) {
        Log.d("confirmInput : " , "true");
        if(confirmInputData() )
            confirm_response();
    }

    public boolean confirmInputData() {

        if (!validateUsername() |  !validatePassword() | !validatefirstname() | !validatelastname()
                 | !validateEmail() | !confirm_password() | !validateage() | !validatephone()) {
            return false ;
        }
        sign_up.setEnabled(false);
        Log.d("confirmInputData : " , "true");

        callAsync();
        if (asyncT !=null && asyncT.isCancelled())
            Log.d("thread " , "cancel final" );
        Log.d("confirmInputData : " , "true");
        return true ;
    }

    public void confirm_response() {

        sign_up.setEnabled(true);
        Log.d("Validate_Response : " , "true");

        if (response !=null )
        {
            if (response.equals("Username is exists"))
                Toast.makeText(this, "Username is exists", Toast.LENGTH_LONG).show();
            else {
                try {
                    
                    Log.d(" final 2 : ", response);
                    String response22[];
                    response22 = ((response.substring(1, response.length() - 2)).split(","));
                    String response_error = response22[0].split(":")[1];
                    String response_error_message = response22[1].split(":")[1];
                    String response_successful = response22[2].split(":")[1];
                    if (response_error.contains("0")) {

                        if (response_error_message.contains("None")) {
                            if (response_successful.contains("1")) {
                                Intent intent = new Intent(this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Log.d("Server ", "successful : 0");
                            }
                        } else {
                            Log.d("Server error_message", response_error_message);
                        }
                    } else {
                        Log.d("Server error : ", response_error);
                        Log.d("Server error_message", response_error_message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("response error " , "invalid structure .");
                    }
            }
        }
        else{
            if (Get_Connection_Status.getAPIConnection()) {
                Log.d("Server :", "didn't recieve response .");
                Toast.makeText(this, ( "Server :- didn't recieve response ." ) , Toast.LENGTH_LONG).show();
            }
            else{
                Log.d("Connection " , "didn't establish ,please check your connection .");
                Toast.makeText(this, ( "Connection didn't establish \nplease check your connection ." ) , Toast.LENGTH_LONG).show();
                Get_Connection_Status.setAPIConnection( !Get_Connection_Status.getAPIConnection() );
            }
        }


    }

    public void callAsync() {

        asyncT = new AsyncT();
        try {
            response=asyncT.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        asyncT.cancel(true);
        Log.d("callAsync " , "true" );
    }

    class AsyncT extends AsyncTask<Void,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("thread " , "begin");
        }

        @Override
        protected String doInBackground(Void... params) {

            String data=null;
            try {
                URL url = new URL(Get_Connection_Status.getUrl_api()+"api/createaccount"); //Enter URL here

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
                httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setConnectTimeout(4000);
                httpURLConnection.connect();

                JSONObject jsonObject= formatInputAsJson();

                OutputStream os = httpURLConnection.getOutputStream();
                os.write(jsonObject.toString().getBytes("UTF-8"));
                os.close();

                Log.d("thread " , "back");

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
                Get_Connection_Status.setAPIConnection( !Get_Connection_Status.getAPIConnection() );
            }
            return data;
        }


    }

    private JSONObject formatInputAsJson(){

        final JSONObject root = new JSONObject();
        try {
            root.put("username" , username.getText().toString());
            root.put("password" , password.getText().toString() );
            root.put("first_name" , first_name.getText().toString() );
            root.put("last_name" , last_name.getText().toString() );
            root.put("age" , email.getText().toString() );
            root.put("email" , age.getText().toString() );
            root.put("phone" , phone.getText().toString() );
            return root;
        } catch (JSONException e) {
            Log.d("JWP","Can not format json");
        }
        return null;
    }
}

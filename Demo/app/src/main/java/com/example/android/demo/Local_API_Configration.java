package com.example.android.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class Local_API_Configration extends AppCompatActivity {

    private EditText api_url;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_configration);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        api_url = (EditText) findViewById(R.id.api_url);

    }

    public void Save(View v) {
        String url =api_url.getText().toString().trim();

        if (!url.isEmpty()){
            if(!url.contains("https://"))
                url = "http://" + url + ":5000/";
            Get_Connection_Status.setUrl_api(url);
        }
        Intent intent= new Intent(this,LoginActivity.class);
        startActivity(intent);

    }

}

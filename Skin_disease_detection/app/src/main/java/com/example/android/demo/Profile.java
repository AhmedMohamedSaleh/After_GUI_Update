package com.example.android.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Profile extends AppCompatActivity {

    Button signout_button, upload_button,view_images_button;
    TextView username;
    Toolbar toolbar;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        upload_button = (Button) findViewById(R.id.upload_button);
        signout_button = (Button) findViewById(R.id.signout_button);
        view_images_button = (Button) findViewById(R.id.view_images_button);
        username = (TextView)findViewById(R.id.username);
        username.setText(getIntent().getStringExtra("Username"));

        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchUploadActivity();
            }
        });
        signout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchLoginActivity();
            }
        });
        view_images_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchUserImagesActivity();
            }
        });
    }
    private void launchUploadActivity() {

        Intent intent = new Intent(this, Upload.class);
        startActivity(intent);
    }
    private void launchLoginActivity() {

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    private void launchUserImagesActivity() {

        Intent intent = new Intent(this, User_Photos.class);
        startActivity(intent);
    }
}

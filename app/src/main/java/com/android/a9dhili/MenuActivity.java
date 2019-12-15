package com.android.a9dhili;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }
    public void goToWelcome(View v )
    {
        Intent intent = new Intent(MenuActivity.this,HomeActivity.class);
        startActivity(intent);
    }
}

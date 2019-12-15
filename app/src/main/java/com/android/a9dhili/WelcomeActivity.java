package com.android.a9dhili;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }
    public void goToSignUp(View v )
    {
        Intent intent = new Intent(WelcomeActivity.this,SignUpActivity.class);
        startActivity(intent);
    }

     public void goToSignIn(View v)
     {

         Intent intent = new Intent(WelcomeActivity.this,SignInActivity.class);
         startActivity(intent);
     }
}

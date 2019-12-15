package com.android.a9dhili;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ChoixActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix);
    }

    public void goToFeed(View v)
    {
        Intent intent = new Intent(ChoixActivity.this,HomeActivity.class);
        startActivity(intent);

    }
    public void goToProfil(View view)
    {
        Log.d("test page courseur","hello i m here") ;
        Intent intent = new Intent(ChoixActivity.this,ProfilActivity.class);
        startActivity(intent);
        finish();
    }
}

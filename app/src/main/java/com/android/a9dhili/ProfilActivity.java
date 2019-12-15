package com.android.a9dhili;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.a9dhili.Models.User;

public class ProfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        TextView tv_nameProfil = (TextView)findViewById(R.id.tv_nameProfil);
        User user  = ((myApp) getApplication()).getUser();
        tv_nameProfil.setText(String.format("%s %s", user.getPrenom(), user.getNom()));

    }
    public void mesCourses(View v)
    {
        Intent intent = new Intent(ProfilActivity.this,MesCoursesActivity.class);
        startActivity(intent);
    }
    public void courseToDo(View v)
    {
        Intent intent = new Intent(ProfilActivity.this,CoursesToDoActivity.class);
        startActivity(intent);
    }
    public void modifier(View v)
    {
        Intent intent = new Intent(ProfilActivity.this,UpdateAdrActivity.class);
        startActivity(intent);
    }
}

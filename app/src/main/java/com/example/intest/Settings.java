package com.example.intest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class Settings extends AppCompatActivity {
    private SharedPreferences userinfo;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        userinfo=getSharedPreferences("userinfos", MODE_PRIVATE);
        editor=userinfo.edit();
    }
    public void amEmployer(View view)
    {
      editor.putString("StudentOrEmployer","Employer");
      editor.apply();
    }
    public void amStudent(View view)
    {
        editor.putString("StudentOrEmployer","Student");
        editor.apply();
    }
    public void  ApplySettings(View view)
    {
        startActivity(new Intent(Settings.this,DashbordUser.class));
    }
}

package com.example.intest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class StudentOrEmployer extends AppCompatActivity {
    private SharedPreferences userinfo;
    private SharedPreferences.Editor myEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_or_employer);
        userinfo=getSharedPreferences("userinfos", MODE_PRIVATE);
        myEditor=userinfo.edit();

    }
    public void amEmployer(View view)
    {
        myEditor.putString("StudentOrEmployer","Employer");
        myEditor.apply();
        startActivity(new Intent(StudentOrEmployer.this,WelcomeScreen.class));
    }
    public void amStudent(View view)
    {
        myEditor.putString("StudentOrEmployer","Student");
        myEditor.apply();
        startActivity(new Intent(StudentOrEmployer.this,WelcomeScreen.class));
    }

}

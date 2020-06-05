package com.example.qa_hub;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent sintent= new Intent(this,MainActivity.class);
        sintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(sintent);
    }
}

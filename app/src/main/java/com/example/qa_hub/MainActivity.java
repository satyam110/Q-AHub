package com.example.qa_hub;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    private FirebaseAuth mAuth;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_answer:
                    mTextMessage.setText(R.string.title_answer);
                    return true;
                case R.id.navigation_search:
                    mTextMessage.setText(R.string.title_search);
                    return true;
                case R.id.navigation_profile:
                    mTextMessage.setText(R.string.title_profile);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
//        Toast.makeText(MainActivity.this,"FireBase Connection Success",Toast.LENGTH_LONG).show();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        BottomNavigationView bottomNavigationView=(BottomNavigationView) findViewById(R.id.nav_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){


                    case R.id.navigation_home:
                        Intent intent1=new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent1);
                        break;


                    case R.id.navigation_answer:
                        Intent intent2=new Intent(MainActivity.this, AskQuestion.class);
                        startActivity(intent2);
                        break;

                    case R.id.navigation_search:
                        Intent intent3=new Intent(MainActivity.this, SearchQuestions.class);
                        startActivity(intent3);
                        break;

                    case R.id.navigation_profile:
                        Intent intent4=new Intent(MainActivity.this,AnsRegister.class);
                        startActivity(intent4);
                        break;

                }

                return false;
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, AnswrerLogin.class));
        }
        else{
            mTextMessage.setText(mAuth.getCurrentUser().getEmail());

        }
    }

}

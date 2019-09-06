package com.example.qa_hub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.lang.String;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AskQuestion extends AppCompatActivity {

    EditText askQue;
    Button btnAsk;
    DatabaseReference reff;
    Questions questions;

    private TextView mTextMessage;

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
        setContentView(R.layout.activity_ask_question);
        askQue=(EditText)findViewById(R.id.askQue);
        btnAsk=(Button)findViewById(R.id.btnAsk);
        questions=new Questions();
        reff= FirebaseDatabase.getInstance().getReference().child("Questions");
        btnAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String que = askQue.getText().toString();
                questions.setAskQue(que);

                if(!TextUtils.isEmpty(que)) {
                    reff.push().setValue(questions); //pushing value to the firebase
                    Toast.makeText(AskQuestion.this, "Your question posted now! ", Toast.LENGTH_LONG).show();
                    askQue.setText(null);
                }else{
                    Toast.makeText(AskQuestion.this, "Please type your question and then hit ask ", Toast.LENGTH_LONG).show();
                }
            }
        });
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        BottomNavigationView bottomNavigationView=(BottomNavigationView) findViewById(R.id.nav_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){


                    case R.id.navigation_home:
                        Intent intent1=new Intent(AskQuestion.this, MainActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                        break;


                    case R.id.navigation_answer:
                        Intent intent2=new Intent(AskQuestion.this, AskQuestion.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                        break;

                    case R.id.navigation_search:
                        Intent intent3=new Intent(AskQuestion.this, SearchQuestions.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent3);
                        break;

                    case R.id.navigation_profile:
                        Intent intent4=new Intent(AskQuestion.this, AnsRegister.class);
                        intent4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent4);
                        break;

                }

                return false;
            }
        });

    }
}

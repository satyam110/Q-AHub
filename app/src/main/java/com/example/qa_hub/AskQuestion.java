package com.example.qa_hub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.lang.String;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AskQuestion extends AppCompatActivity {

    EditText askQue;
    Button btnAsk;
    DatabaseReference reff;
    Questions questions;

    private TextView bnTextMessage;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    bnTextMessage.setText(R.string.bn_home);
                    return true;
                case R.id.navigation_answer:
                    bnTextMessage.setText(R.string.bn_ask);
                    return true;
                case R.id.navigation_profile:
                    bnTextMessage.setText(R.string.bn_profile);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);
        askQue=findViewById(R.id.askQue);
        btnAsk=findViewById(R.id.btnAsk);
        questions=new Questions();
        reff= FirebaseDatabase.getInstance().getReference().child("Questions");
        btnAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ans ="";
                String que = askQue.getText().toString();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
                Calendar calendar = Calendar.getInstance();
                String timestamp = simpleDateFormat.format(calendar.getTime());
                if(!TextUtils.isEmpty(que)) {
                    String key = reff.push().getKey();
                    questions = new Questions(que,timestamp,key,ans);
                    reff.child(key).setValue(questions).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(AskQuestion.this, "Your question posted now! ", Toast.LENGTH_LONG).show();
                                askQue.setText(null);
                                startActivity(new Intent(AskQuestion.this,MainActivity.class));
                                finish();
                            }else {
                                Toast.makeText(AskQuestion.this, "Something went wrong or check your net connectivity ", Toast.LENGTH_LONG).show();
                            }
                        }
                    });//pushing value to the firebase

                }else{
                    Toast.makeText(AskQuestion.this, "Please type your question and then hit ask ", Toast.LENGTH_LONG).show();
                }
            }
        });
        BottomNavigationView navView = findViewById(R.id.nav_view);
        bnTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Menu menu = navView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

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


                    case R.id.navigation_profile:
                        Intent intent4=new Intent(AskQuestion.this, UserDashboard.class);
                        intent4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent4);
                        break;

                }

                return false;
            }
        });

    }
}

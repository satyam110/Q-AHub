package com.example.qa_hub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity implements AnswerInterface {
    private TextView mTextMessage,bnTextMessage;
    private FirebaseAuth mAuth;
    RecyclerView recyclerAllQues;
    private ArrayList<Questions> allQuesList = new ArrayList<>();
    private DatabaseReference reff,qreff,areff ;
    ProgressDialog progressDialog;

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
    LinearLayoutManager linearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mTextMessage= findViewById(R.id.userDetail);
        recyclerAllQues= findViewById(R.id.recycler_all_questions);
//        Toast.makeText(MainActivity.this,"FireBase Connection Success",Toast.LENGTH_LONG).show();
        reff = FirebaseDatabase.getInstance().getReference("Questions").child("");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Questions..");
      //  ButterKnife.bind(this);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        bnTextMessage = findViewById(R.id.message);
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

                    case R.id.navigation_profile:
                        Intent intent4=new Intent(MainActivity.this,AnsRegister.class);
                        startActivity(intent4);
                        break;

                }
                return false;
            }
        });

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerAllQues.setLayoutManager(linearLayoutManager);
        //readAllQues();
    }
    @Override
    protected void onResume(){
        super.onResume();
        readAllQues();
    }
    public void readAllQues(){

         allQuesList.clear();
         progressDialog.show();
         reff.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                     Questions questions = snapshot.getValue(Questions.class);
                     allQuesList.add(questions);
                 }
                 progressDialog.dismiss();

                 QuestionsAdapter questionsAdapter = new QuestionsAdapter(MainActivity.this,allQuesList);
                 recyclerAllQues.setAdapter(questionsAdapter);
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });
    }
    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, AnswrerLogin.class));
        }else{
         // mTextMessage.setText(mAuth.getCurrentUser().getEmail());
//            if(mAuth.getCurrentUser().getEmail() != null){
//                mTextMessage.setText("Session is active");
//            }
        }

    }

    @Override
    public void answerQuestion(Questions questions) {
        qreff = reff.child(questions.getQuesId());
        areff = qreff.child("ans");
        areff.setValue(questions.getAns()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Answer Submitted!", Toast.LENGTH_LONG).show();
                    readAllQues();
                }
                else{
                    Toast.makeText(MainActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

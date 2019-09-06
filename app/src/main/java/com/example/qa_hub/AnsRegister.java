package com.example.qa_hub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;

public class AnsRegister extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener {
    private EditText full_name,email,mob,pass;
    String domainVal;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
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

//    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ans_register);
        BottomNavigationView navView = findViewById(R.id.nav_view);
         full_name = findViewById(R.id.fname);
         email= findViewById(R.id.email);
         mob = findViewById(R.id.login_pass);
         pass = findViewById(R.id.pwd);
         mAuth = FirebaseAuth.getInstance();
         findViewById(R.id.tv_sign_in).setOnClickListener(this);
         findViewById(R.id.btnSignUp).setOnClickListener(this);

       progressDialog = new ProgressDialog(this);


        Spinner domain = findViewById(R.id.domain);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.domains,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        domain.setAdapter(adapter);
        domain.setOnItemSelectedListener(this);



        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

// Code for Bottom navigation bar starts here
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                        case R.id.navigation_home:
                        Intent intent1 = new Intent(AnsRegister.this, MainActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                        break;
                        case R.id.navigation_answer:
                        Intent intent2 = new Intent(AnsRegister.this, AskQuestion.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                        break;
                        case R.id.navigation_search:
                        Intent intent3 = new Intent(AnsRegister.this, SearchQuestions.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent3);
                        break;
                        case R.id.navigation_profile:
                        Intent intent4 = new Intent(AnsRegister.this, AnsRegister.class);
                        intent4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent4);
                        break;
                }
                return false;
            }
        });
        // Code for Bottom navigation bar starts here
    }
    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
//            finish();
//            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void registerUser() {
        final String name = full_name.getText().toString().trim();
        final String mail = email.getText().toString().trim();
        final String phone = mob.getText().toString().trim();
        final String field = domainVal;
        String password = pass.getText().toString().trim();

        if (name.isEmpty()) {
            full_name.setError(getString(R.string.input_error_name));
            full_name.requestFocus();
            return;
        }

        else if (mail.isEmpty()) {
            email.setError(getString(R.string.input_error_email));
            email.requestFocus();
            return;
        }

        else if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            email.setError(getString(R.string.input_error_email_invalid));
            email.requestFocus();
            return;
        }

       else if (password.isEmpty()) {
            pass.setError(getString(R.string.input_error_password));
            pass.requestFocus();
            return;
        }

        else if (password.length() < 6) {
            pass.setError(getString(R.string.input_error_password_length));
            pass.requestFocus();
            return;
        }

        else if (phone.isEmpty()) {
            mob.setError(getString(R.string.input_error_phone));
            mob.requestFocus();
            return;
        }
        else if (phone.length() != 10) {
            mob.setError(getString(R.string.input_error_phone_invalid));
           mob.requestFocus();
            return;
        }
        else {
            progressDialog.setMessage("Signing Up..");
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Users user = new Users(name, mail, phone, field);

                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(AnsRegister.this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                                    finish();
                                    //finish();
                                    startActivity(new Intent(AnsRegister.this,MainActivity.class));
                                    //full_name.setText(null);email.setText(null);mob.setText(null);pass.setText(null);
                                } else {
                                    //display a failure message
                                }
                            }
                        });
                    }else {
                        if(task.getException() instanceof FirebaseAuthUserCollisionException){
                            Toast.makeText(getApplicationContext(),"User with entered email already exists",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(AnsRegister.this, "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        domainVal=adapterView.getItemAtPosition(i).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sign_in:
                startActivity(new Intent(this,AnswrerLogin.class));
            case R.id.btnSignUp:
                registerUser();
                break;
        }
    }
}

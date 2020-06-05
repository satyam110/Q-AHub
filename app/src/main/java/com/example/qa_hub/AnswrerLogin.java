package com.example.qa_hub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AnswrerLogin extends AppCompatActivity implements View.OnClickListener{
    EditText login_mail,login_pass;
    private FirebaseAuth mAuth;
    private TextView bnTextMessage;
   // SharedPreferences sharedPreferences;
  //  SharedPreferences.Editor editor;
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
        setContentView(R.layout.activity_answrer_login);
        login_mail = findViewById(R.id.login_mail);
        login_pass = findViewById(R.id.login_pass);

        findViewById(R.id.signup_now).setOnClickListener(this);
        findViewById(R.id.forgot_pw).setOnClickListener(this);
        findViewById(R.id.btnLogin).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        // Code for Bottom navigation bar starts here
        BottomNavigationView navView = findViewById(R.id.nav_view);
        bnTextMessage = findViewById(R.id.message);
        Menu menu = navView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                        case R.id.navigation_home:
                        Intent intent1 = new Intent(AnswrerLogin.this, MainActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                        break;
                        case R.id.navigation_answer:
                        Intent intent2 = new Intent(AnswrerLogin.this, AskQuestion.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                        break;
                        case R.id.navigation_profile:
                        Intent intent4 = new Intent(AnswrerLogin.this, AnswrerLogin.class);
                        intent4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent4);
                        break;
                }
                return false;
            }
        });
        // Code for Bottom navigation bar ends here
    }
private void userLogin(){

    final String mail = login_mail.getText().toString().trim();
    String password = login_pass.getText().toString().trim();



    if (mail.isEmpty()) {
        login_mail.setError(getString(R.string.input_error_email));
        login_mail.requestFocus();
        return;
    }

    else if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
        login_mail.setError(getString(R.string.input_error_email_invalid));
        login_mail.requestFocus();
        return;
    }

    else if (password.isEmpty()) {
        login_pass.setError(getString(R.string.input_error_password));
        login_pass.requestFocus();
        return;
    }
    else{
        mAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                   Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() != null){
           // finish();
            startActivity(new Intent(this, UserDashboard.class));
        }
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.signup_now:
                startActivity(new Intent(this,AnsRegister.class));
            case R.id.btnLogin:
                    userLogin();
                break;
            case R.id.forgot_pw:
                String usrmail = login_mail.getText().toString();
                if(!usrmail.equalsIgnoreCase("")){
                    mAuth.sendPasswordResetEmail(usrmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(AnswrerLogin.this,"Please Check Your email for reseting your password!",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(AnswrerLogin.this,"Error resending password",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(AnswrerLogin.this,"Please enter valid email!",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}

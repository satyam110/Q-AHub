package com.example.qa_hub;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.core.Context;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;



public class UserDashboard extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth mAuth;
    TextView displayName,userEmail,userField;
    Button btnSignOut,camBtn;
    String dname,uid,field;
    ImageView userProfileImg;
    private TextView bnTextMessage;
    private StorageReference mStorage,imgPath;
    private DatabaseReference reff;
    private Uri uri ;
    Users user;
    Context context;


    private static final int GALLERY_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    //private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        user = new Users();
        displayName = (TextView) findViewById(R.id.display_name);
        userEmail = (TextView) findViewById(R.id.user_email);
        userProfileImg = (ImageView) findViewById(R.id.userProfileImg);
        userField = (TextView) findViewById(R.id.userField);
        camBtn = (Button) findViewById(R.id.cameraBtn);

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                field = dataSnapshot.child("field").getValue().toString();
                userField.setText(field);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(mAuth.getCurrentUser().getDisplayName()!=null) {
            dname = mAuth.getCurrentUser().getDisplayName();
            displayName.setText(dname);
            userEmail.setText(mAuth.getCurrentUser().getEmail());

        }
        else{
            displayName.setText("No display name");
            userEmail.setText(mAuth.getCurrentUser().getEmail());
        }

        findViewById(R.id.btnLogout).setOnClickListener(this);

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
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
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        Intent intent1 = new Intent(UserDashboard.this, MainActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_answer:
                        Intent intent2 = new Intent(UserDashboard.this, AskQuestion.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                        break;

                    case R.id.navigation_profile:
                        Intent intent4 = new Intent(UserDashboard.this, UserDashboard.class);
                        intent4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent4);
                        break;
                }
                return false;
            }
        });

        userProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, GALLERY_REQUEST_CODE);
                }
            }
        });
        camBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //setResult(RESULT_OK,data);
        if(requestCode==GALLERY_REQUEST_CODE && resultCode==RESULT_OK){
            uri=data.getData();
            StorageReference filePath = mStorage.child("User-photos").child(mAuth.getCurrentUser().getUid());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(UserDashboard.this,"Image Uploaded SuccessFully",Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if(requestCode==CAMERA_REQUEST_CODE && resultCode==RESULT_OK){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            userProfileImg.setImageBitmap(photo);
        }

    }

    public void getUserProfileImg(){
        imgPath = mStorage.child("User-photos").child(mAuth.getCurrentUser().getUid());

        imgPath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri downloadUri) {
                Picasso.get().load(downloadUri).fit().placeholder(R.drawable.ic_man).centerCrop().into(userProfileImg);

            }
        }) ;
    }



    @Override
    protected void onStart() {
        super.onStart();
        getUserProfileImg();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this,AnswrerLogin.class));
                break;
        }
    }


}

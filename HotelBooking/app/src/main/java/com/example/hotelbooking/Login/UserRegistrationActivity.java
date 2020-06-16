package com.example.hotelbooking.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.hotelbooking.R;
import com.example.hotelbooking.Users.UserHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class UserRegistrationActivity extends AppCompatActivity {
    private EditText name,email,phoneno,city,idcard,birthday;
    RadioGroup rg1,rg2;
    RadioButton radiosexButton,radiomarrageButton;
    private Button createAccount;
    private FirebaseAuth mAuth;
    private DatabaseReference userref;
    private String currentuerid;
    private String marragestatus,gender;
    private String check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        mAuth=FirebaseAuth.getInstance();
        currentuerid=mAuth.getCurrentUser().getUid();

        check=getIntent().getExtras().get("check").toString();

        userref= FirebaseDatabase.getInstance().getReference();
        InitializeFields();
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfoToDatabase();
            }
        });


    }


    private void InitializeFields() {
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        phoneno=findViewById(R.id.phoneno);
        city=findViewById(R.id.city_of_residence);
        idcard=findViewById(R.id.id_card);
        birthday=findViewById(R.id.birthday);
        rg1=findViewById(R.id.radiogroup_gender);
        rg2=findViewById(R.id.radiogroup_marrage);
        createAccount=findViewById(R.id.create_account);
        if(check.equals("email")){
            email.setFocusable(false);
            phoneno.setFocusable(true);
        }
        if(check.equals("phone")){
            email.setFocusable(true);
            phoneno.setFocusable(false);
        }


    }

    private void updateUserInfoToDatabase() {
        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.male:
                        gender="male";
                        break;
                    case R.id.female:
                        gender="female";
                        break;
                }

            }
        });
        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.married:
                        marragestatus="married";
                        break;
                    case R.id.unmarried:
                        marragestatus="unmarried";
                        break;
                }
            }
        });

        //String phone=mAuth.getCurrentUser().getPhoneNumber();
        HashMap<String,Object> userMap=new HashMap<>();
        userMap.put("name",name.getText().toString());
        if(check.equals("email")){
            userMap.put("phone",phoneno.getText().toString());
        }
        if(check.equals("phone")){
            userMap.put("email",email.getText().toString());
        }

       // userMap.put("email",email.getText().toString());

        userMap.put("city",city.getText().toString());
        userMap.put("idcard",idcard.getText().toString());
        userMap.put("birthday",birthday.getText().toString());
        userMap.put("gender",gender);
        userMap.put("marriagestatus",marragestatus);
        userMap.put("admin","access");
        userref.child("Users").child(currentuerid).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    sendUserToUserHomeActivity();
                    Toast.makeText(UserRegistrationActivity.this,"Registration successful",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(UserRegistrationActivity.this,"error",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void sendUserToUserHomeActivity() {
        Intent intent=new Intent(this, UserHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}

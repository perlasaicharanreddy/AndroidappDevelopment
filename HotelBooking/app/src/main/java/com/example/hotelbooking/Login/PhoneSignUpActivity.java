package com.example.hotelbooking.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hotelbooking.Agent.AgentHomeActivity;
import com.example.hotelbooking.Agent.AgentRegistrationActivity;
import com.example.hotelbooking.R;
import com.example.hotelbooking.Users.UserHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class PhoneSignUpActivity extends AppCompatActivity {
    private EditText phonenumberET,verificationcodeET;
    private Button sendvefcodeBtn,verifyBtn;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingbar;
    private DatabaseReference rootref;
    private String currentuserid;
    private String phoneno;
    private String userOragent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_sign_up);

        mAuth=FirebaseAuth.getInstance();
        rootref= FirebaseDatabase.getInstance().getReference();

        userOragent=getIntent().getExtras().get("userOragent").toString();

        Initializefields();
        sendvefcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber=phonenumberET.getText().toString();
                if(TextUtils.isEmpty(phoneNumber)){
                    Toast.makeText(PhoneSignUpActivity.this,"Phone Number is Required",Toast.LENGTH_SHORT).show();
                }
                else{
                    loadingbar.setTitle("phone Verification");
                    loadingbar.setMessage("please wait while we are authenticating your phone");
                    loadingbar.setCanceledOnTouchOutside(false);
                    loadingbar.show();
                    phoneNumber="+91"+phoneNumber;
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            PhoneSignUpActivity.this,               // Activity (for callback binding)
                            callbacks);        // OnVerificationStateChangedCallbacks

                }
            }
        });
        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phonenumberET.setVisibility(View.INVISIBLE);
                sendvefcodeBtn.setVisibility(View.INVISIBLE);

//                String code=verificationcodeET.getText().toString();
//                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
//                signInWithPhoneAuthCredential(credential);

                String verificationCode=verificationcodeET.getText().toString();
                if(TextUtils.isEmpty(verificationCode)){
                    Toast.makeText(PhoneSignUpActivity.this,"please enter code",Toast.LENGTH_SHORT).show();

                }
                else{
                    loadingbar.setTitle("Code Verification");
                    loadingbar.setMessage("please wait while we are verifying your code");
                    loadingbar.setCanceledOnTouchOutside(false);
                    loadingbar.show();
                }
                signin();

            }
        });
        callbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                loadingbar.dismiss();
                Toast.makeText(PhoneSignUpActivity.this,"Invalid Phone number Please enter correct phone number with country code",Toast.LENGTH_SHORT).show();
                phonenumberET.setVisibility(View.VISIBLE);
                sendvefcodeBtn.setVisibility(View.VISIBLE);
                verificationcodeET.setVisibility(View.INVISIBLE);
                verifyBtn.setVisibility(View.INVISIBLE);


            }
            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                loadingbar.dismiss();


                // Save verification ID and resending token so we can use them later
                Toast.makeText(PhoneSignUpActivity.this,"code has sent successfully",Toast.LENGTH_SHORT).show();

                mVerificationId = verificationId;
                mResendToken = token;
                Toast.makeText(PhoneSignUpActivity.this,"code has sent successfully",Toast.LENGTH_SHORT).show();

                phonenumberET.setVisibility(View.INVISIBLE);
                sendvefcodeBtn.setVisibility(View.INVISIBLE);
                verificationcodeET.setVisibility(View.VISIBLE);
                verifyBtn.setVisibility(View.VISIBLE);

            }
        };
    }

    private void signin() {
        String code=verificationcodeET.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
                signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loadingbar.dismiss();
                            Toast.makeText(PhoneSignUpActivity.this," Congratulations you are logged in successfully",Toast.LENGTH_SHORT).show();
                            phoneno=mAuth.getCurrentUser().getPhoneNumber();
                             checkuserExistance();

                        } else {
                            loadingbar.dismiss();
                            String message=task.getException().toString();
                            Toast.makeText(PhoneSignUpActivity.this,"Error: "+message,Toast.LENGTH_SHORT).show();


                        }
                    }
                });
    }

    private void checkuserExistance() {
        currentuserid=mAuth.getCurrentUser().getUid();
        rootref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(currentuserid)){
                    if(userOragent.equals("Users"))
                        sendUserToUserHomeActivity();
                    else sendUserToAgentHomeActivity();
                }
                else {
                    rootref.child(userOragent).child(currentuserid).child("phone").setValue(phoneno);
                    if(userOragent.equals("Users"))
                        sendusertoUserRegistrationActivity();
                    else sendUserToAgentRegistrationActivity();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


    private void Initializefields() {
        phonenumberET=findViewById(R.id.phone_number_input);
        verificationcodeET=findViewById(R.id.verification_code_input);
        sendvefcodeBtn=findViewById(R.id.send_ver_code_btn);
        verifyBtn=findViewById(R.id.verify_btn);
        loadingbar=new ProgressDialog(this,R.style.MydialogTheme);
    }
    private void sendUserToUserHomeActivity() {
        Intent intent=new Intent(this, UserHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private void sendusertoUserRegistrationActivity() {
        Intent intent=new Intent(this,UserRegistrationActivity.class);
        intent.putExtra("check","phone");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void sendUserToAgentHomeActivity() {
        Intent intent=new Intent(this, AgentHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void sendUserToAgentRegistrationActivity() {
        Intent intent=new Intent(this, AgentRegistrationActivity.class);
        intent.putExtra("check","phone");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
}

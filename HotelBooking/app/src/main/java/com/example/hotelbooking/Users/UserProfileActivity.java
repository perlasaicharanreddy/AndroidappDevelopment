package com.example.hotelbooking.Users;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hotelbooking.Agent.AgentEditRoomActivity;
import com.example.hotelbooking.Agent.AgentHomeActivity;
import com.example.hotelbooking.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class UserProfileActivity extends AppCompatActivity {
    private TextView usernameTV;
    private EditText userphoneET,useremailET,birthdayET,useraddressET,userCityET,useridcardET;
    private Button updatebtn;
    private ImageView userprofileimg;
    private DatabaseReference userref;
    private String saveCurrentDate, saveCurrentTime;
    private String UserRandomKey, downloadimgurl;
    private FirebaseAuth mauth;
    private StorageReference UserImageRef;
    private ProgressDialog progressDialog;
    private Uri Imageuri;
    private int GALLARYPICK=1;
    private String currentuserid,checker="";
    private String username,userphone,useremail,birthday,useraddress,usercity,useridcard,userimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userref= FirebaseDatabase.getInstance().getReference().child("Users");
        UserImageRef= FirebaseStorage.getInstance().getReference().child("User Images");
        mauth=FirebaseAuth.getInstance();
        currentuserid=mauth.getCurrentUser().getUid();
        InitializationFields();
        displayAvailableUserDetails();

        userprofileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
                checker="checked";
            }
        });
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checker.equals("checked")){
                    userInfosave();
                }
                else {
                    updateonlyuserinfocheck();
                }
            }
        });
    }



    private void InitializationFields() {
        usernameTV=findViewById(R.id.user_name);
        userphoneET=findViewById(R.id.user_phone_no);
        useremailET=findViewById(R.id.user_email);
        birthdayET=findViewById(R.id.user_birthday);
        useraddressET=findViewById(R.id.user_address);
        userCityET=findViewById(R.id.user_city);
        useridcardET=findViewById(R.id.user_id_card_no);
        updatebtn=findViewById(R.id.user_details_update_btn);
        userprofileimg=findViewById(R.id.user_profile_image);
        progressDialog = new ProgressDialog(this,R.style.MydialogTheme);
    }

    private void displayAvailableUserDetails() {
        userref.child(currentuserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    username=dataSnapshot.child("name").getValue().toString();
                    useremail=dataSnapshot.child("email").getValue().toString();
                    userphone=dataSnapshot.child("phone").getValue().toString();
                    birthday=dataSnapshot.child("birthday").getValue().toString();
                    usercity=dataSnapshot.child("city").getValue().toString();
                    useridcard=dataSnapshot.child("idcard").getValue().toString();

                    usernameTV.setText(username);
                    useremailET.setText(useremail);
                    userphoneET.setText(userphone);
                    birthdayET.setText(birthday);
                    userCityET.setText(usercity);
                    useridcardET.setText(useridcard);
                }
                if(dataSnapshot.hasChild("userimage")){
                    userimage=dataSnapshot.child("userimage").getValue().toString();
                    useraddress=dataSnapshot.child("address").getValue().toString();
                    Picasso.get().load(userimage).into(userprofileimg);
                    useraddressET.setText(useraddress);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateonlyuserinfocheck() {
        userphone=userphoneET.getText().toString();
        useremail=useremailET.getText().toString();
        birthday=birthdayET.getText().toString();
        useraddress=useraddressET.getText().toString();
        usercity=userCityET.getText().toString();
        useridcard=useridcardET.getText().toString();
        if(TextUtils.isEmpty(userphone)){
            userphoneET.setError("enter phone no");
        }
        else if(TextUtils.isEmpty(useremail)){
            useremailET.setError("enter email");
        }
        else if(TextUtils.isEmpty(birthday)){
            birthdayET.setError("enter birthday");
        }
        else if(TextUtils.isEmpty(useraddress)){
            useraddressET.setError("enter address");
        }
        else if(TextUtils.isEmpty(usercity)){
            userCityET.setError("enter city");
        }
        else if(TextUtils.isEmpty(useridcard)){
            useridcardET.setError("enter id card no");
        }
        else {
            HashMap<String,Object> userMap=new HashMap<>();
            userMap.put("email",useremail);
            userMap.put("phone",userphone);
            userMap.put("idcard",useridcard);
            userMap.put("city",usercity);
            userMap.put("birthday",birthday);
            userMap.put("address",useraddress);
            userref.child(currentuserid).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    sendUserToUserHomeActivity();
                }
            });
        }
    }



    private void OpenGallery(){
        Intent gallaryintent=new Intent();
        gallaryintent.setAction(Intent.ACTION_GET_CONTENT);
        gallaryintent.setType("image/*");
        startActivityForResult(gallaryintent,GALLARYPICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLARYPICK && resultCode==RESULT_OK && data!=null){
            Imageuri=data.getData();
            userprofileimg.setImageURI(Imageuri);
        }
    }

    private void userInfosave() {
        userphone=userphoneET.getText().toString();
        useremail=useremailET.getText().toString();
        birthday=birthdayET.getText().toString();
        useraddress=useraddressET.getText().toString();
        usercity=userCityET.getText().toString();
        useridcard=useridcardET.getText().toString();
        if(TextUtils.isEmpty(userphone)){
            userphoneET.setError("enter phone no");
        }
        else if(TextUtils.isEmpty(useremail)){
            useremailET.setError("enter email");
        }
        else if(TextUtils.isEmpty(birthday)){
            birthdayET.setError("enter birthday");
        }
        else if(TextUtils.isEmpty(useraddress)){
            useraddressET.setError("enter address");
        }
        else if(TextUtils.isEmpty(usercity)){
            userCityET.setError("enter city");
        }
        else if(TextUtils.isEmpty(useridcard)){
            useridcardET.setError("enter id card no");
        }
        else {
            uploadImage();
        }
        }




        private void uploadImage() {
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait while we are updating your info");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        if(Imageuri!=null){
            Calendar calendar=Calendar.getInstance();
            SimpleDateFormat currentDate=new SimpleDateFormat("MMM  dd, yyyy");
            saveCurrentDate=currentDate.format(calendar.getTime());

            SimpleDateFormat currentTime=new SimpleDateFormat("HH;mm;ss a");
            saveCurrentTime=currentTime.format(calendar.getTime());
            UserRandomKey=saveCurrentDate+saveCurrentTime;

            final StorageReference filePath=UserImageRef.child(Imageuri.getLastPathSegment() + UserRandomKey+".jpg");
            final UploadTask uploadTask=filePath.putFile(Imageuri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String message=e.toString();
                    Toast.makeText(UserProfileActivity.this,"Error: "+message,Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(UserProfileActivity.this,"User Image Uploaded Successfully",Toast.LENGTH_SHORT).show();
                    Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if(!task.isSuccessful()){
                                throw task.getException();
                            }
                            downloadimgurl=filePath.getDownloadUrl().toString();
                            return filePath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                downloadimgurl=task.getResult().toString();
                                if(task.isSuccessful()){
                                    downloadimgurl=task.getResult().toString();
                                    Toast.makeText(UserProfileActivity.this,"got user image url Successfully",Toast.LENGTH_SHORT).show();
                                    HashMap<String,Object> userMap=new HashMap<>();
                                    userMap.put("email",useremail);
                                    userMap.put("phone",userphone);
                                    userMap.put("idcard",useridcard);
                                    userMap.put("city",usercity);
                                    userMap.put("birthday",birthday);
                                    userMap.put("address",useraddress);
                                    userMap.put("userimage",downloadimgurl);
                                    userref.child(currentuserid).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                progressDialog.dismiss();
                                                Toast.makeText(UserProfileActivity.this,"Update successful",Toast.LENGTH_SHORT).show();
                                                  sendUserToUserHomeActivity();
                                            }
                                        }
                                    });
                                }

                            }
                        }
                    });

                }
            });

        }
        else {
            Toast.makeText(UserProfileActivity.this,"Image is not selected",Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

    }

    private void sendUserToUserHomeActivity() {
        Intent intent=new Intent(this, UserHomeActivity.class);
        startActivity(intent);
    }


}



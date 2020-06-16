package com.example.hotelbooking.Admin;

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

import com.example.hotelbooking.R;
import com.example.hotelbooking.Users.UserHomeActivity;
import com.example.hotelbooking.Users.UserProfileActivity;
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

public class AdminProfileActivity extends AppCompatActivity {

    private TextView adminnameTV;
    private EditText adminphoneET,adminemailET,birthdayET,adminaddressET,adminCityET,adminidcardET;
    private Button updatebtn;
    private ImageView adminprofileimg;
    private DatabaseReference adminref;
    private String saveCurrentDate, saveCurrentTime;
    private String AdminRandomKey, downloadimgurl;
    private StorageReference AdminImageRef;
    private ProgressDialog progressDialog;
    private Uri Imageuri;
    private int GALLARYPICK=1;
    private String checker="";
    private String adminname,adminphone,adminemail,birthday,adminaddress,admincity,adminidcard,adminimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        adminref= FirebaseDatabase.getInstance().getReference().child("Admin");
        AdminImageRef= FirebaseStorage.getInstance().getReference().child("Admin Images");
        InitializationFields();
        displayAvailableUserDetails();

        adminprofileimg.setOnClickListener(new View.OnClickListener() {
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
        adminnameTV=findViewById(R.id.admin_name);
        adminphoneET=findViewById(R.id.admin_phone_no);
        adminemailET=findViewById(R.id.admin_email);
        birthdayET=findViewById(R.id.admin_birthday);
        adminaddressET=findViewById(R.id.admin_address);
        adminCityET=findViewById(R.id.admin_city);
        adminidcardET=findViewById(R.id.admin_id_card_no);
        updatebtn=findViewById(R.id.admin_details_update_btn);
        adminprofileimg=findViewById(R.id.admin_profile_image);
        progressDialog = new ProgressDialog(this,R.style.MydialogTheme);
    }

    private void displayAvailableUserDetails() {
        adminref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    adminname=dataSnapshot.child("name").getValue().toString();
                    adminnameTV.setText(adminname);
                }
                if(dataSnapshot.hasChild("adminimage")){
                    adminimage=dataSnapshot.child("adminimage").getValue().toString();
                    adminaddress=dataSnapshot.child("address").getValue().toString();
                    Picasso.get().load(adminimage).into(adminprofileimg);
                    adminaddressET.setText(adminaddress);

                    adminemail=dataSnapshot.child("email").getValue().toString();
                    adminphone=dataSnapshot.child("phone").getValue().toString();
                    birthday=dataSnapshot.child("birthday").getValue().toString();
                    admincity=dataSnapshot.child("city").getValue().toString();
                    adminidcard=dataSnapshot.child("idcard").getValue().toString();
                    adminemailET.setText(adminemail);
                    adminphoneET.setText(adminphone);
                    birthdayET.setText(birthday);
                    adminCityET.setText(admincity);
                    adminidcardET.setText(adminidcard);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateonlyuserinfocheck() {
        adminphone=adminphoneET.getText().toString();
        adminemail=adminemailET.getText().toString();
        birthday=birthdayET.getText().toString();
        adminaddress=adminaddressET.getText().toString();
        admincity=adminCityET.getText().toString();
        adminidcard=adminidcardET.getText().toString();
        if(TextUtils.isEmpty(adminphone)){
            adminphoneET.setError("enter phone no");
        }
        else if(TextUtils.isEmpty(adminemail)){
            adminemailET.setError("enter email");
        }
        else if(TextUtils.isEmpty(birthday)){
            birthdayET.setError("enter birthday");
        }
        else if(TextUtils.isEmpty(adminaddress)){
            adminaddressET.setError("enter address");
        }
        else if(TextUtils.isEmpty(admincity)){
            adminCityET.setError("enter city");
        }
        else if(TextUtils.isEmpty(adminidcard)){
            adminidcardET.setError("enter id card no");
        }
        else {
            HashMap<String,Object> userMap=new HashMap<>();
            userMap.put("email",adminemail);
            userMap.put("phone",adminphone);
            userMap.put("idcard",adminidcard);
            userMap.put("city",admincity);
            userMap.put("birthday",birthday);
            userMap.put("address",adminaddress);
            adminref.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    sendUserToAdminHomeActivity();
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
            adminprofileimg.setImageURI(Imageuri);
        }
    }

    private void userInfosave() {
        adminphone=adminphoneET.getText().toString();
        adminemail=adminemailET.getText().toString();
        birthday=birthdayET.getText().toString();
        adminaddress=adminaddressET.getText().toString();
        admincity=adminCityET.getText().toString();
        adminidcard=adminidcardET.getText().toString();
        if(TextUtils.isEmpty(adminphone)){
            adminphoneET.setError("enter phone no");
        }
        else if(TextUtils.isEmpty(adminemail)){
            adminemailET.setError("enter email");
        }
        else if(TextUtils.isEmpty(birthday)){
            birthdayET.setError("enter birthday");
        }
        else if(TextUtils.isEmpty(adminaddress)){
            adminaddressET.setError("enter address");
        }
        else if(TextUtils.isEmpty(admincity)){
            adminCityET.setError("enter city");
        }
        else if(TextUtils.isEmpty(adminidcard)){
            adminidcardET.setError("enter id card no");
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
            AdminRandomKey=saveCurrentDate+saveCurrentTime;

            final StorageReference filePath=AdminImageRef.child(Imageuri.getLastPathSegment() + AdminRandomKey+".jpg");
            final UploadTask uploadTask=filePath.putFile(Imageuri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String message=e.toString();
                    Toast.makeText(AdminProfileActivity.this,"Error: "+message,Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AdminProfileActivity.this,"Admin Image Uploaded Successfully",Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(AdminProfileActivity.this,"got admin image url Successfully",Toast.LENGTH_SHORT).show();
                                    HashMap<String,Object> userMap=new HashMap<>();
                                    userMap.put("email",adminemail);
                                    userMap.put("phone",adminphone);
                                    userMap.put("idcard",adminidcard);
                                    userMap.put("city",admincity);
                                    userMap.put("birthday",birthday);
                                    userMap.put("address",adminaddress);
                                    userMap.put("adminimage",downloadimgurl);
                                    adminref.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                progressDialog.dismiss();
                                                Toast.makeText(AdminProfileActivity.this,"Update successful",Toast.LENGTH_SHORT).show();
                                                sendUserToAdminHomeActivity();
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
            Toast.makeText(AdminProfileActivity.this,"Image is not selected",Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

    }

    private void sendUserToAdminHomeActivity() {
        Intent intent=new Intent(this, AdminHomeActivity.class);
        startActivity(intent);
    }
}

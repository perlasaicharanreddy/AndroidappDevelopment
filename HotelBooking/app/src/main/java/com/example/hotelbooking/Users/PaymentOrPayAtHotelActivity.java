package com.example.hotelbooking.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hotelbooking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class PaymentOrPayAtHotelActivity extends AppCompatActivity {
    private String totalprice="",bookingid;
    private TextView totalpriceTV;
    private Button payAtHotelBtn,payUsingUpiBtn;
    private DatabaseReference Bookingref,agentbookingsref;
    private FirebaseAuth mauth;
    private String currentuserid,agentid;


    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_or_pay_at_hotel);

        totalprice=getIntent().getExtras().get("totalprice").toString();
        bookingid=getIntent().getExtras().get("bookingid").toString();
        agentid=getIntent().getExtras().get("agentid").toString();
        agentbookingsref=FirebaseDatabase.getInstance().getReference().child("AgentBookings");


        Bookingref= FirebaseDatabase.getInstance().getReference().child("Bookings");
        mauth=FirebaseAuth.getInstance();
        currentuserid=mauth.getCurrentUser().getUid();

        totalpriceTV=findViewById(R.id.user_book_room_price_tv);
        totalpriceTV.setText(totalprice);
        payAtHotelBtn=findViewById(R.id.pay_at_hotel_btn);
        payUsingUpiBtn=findViewById(R.id.pay_now_btn);

        builder=new AlertDialog.Builder(this,R.style.MydialogTheme);

        payUsingUpiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToUpiPaymentActivity();
            }
        });

        payAtHotelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder.setTitle("Booking conformation");
                builder.setMessage("Are you sure you want to book this room and pay at hotel");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bookRoom();
                        dialog.cancel();

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create();
                builder.show();
            }
        });



    }

    private void bookRoom() {
        final HashMap<String,Object> map=new HashMap<>();
        map.put("paymentstatus","payathotel");
        map.put("bookingstatus","booked");
        Bookingref.child(currentuserid).child(bookingid).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    agentbookingsref.child(agentid).child(bookingid).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(PaymentOrPayAtHotelActivity.this,"Booking successful",Toast.LENGTH_SHORT).show();
                                sendUserToUserHomeActivity();
                            }
                        }
                    });
                }
            }
        });
    }

    private void sendUserToUpiPaymentActivity() {
        Intent intent=new Intent(this,UpiPaymentActivity.class);
        intent.putExtra("totalprice",totalprice);
        intent.putExtra("bookingid",bookingid);
        intent.putExtra("agentid",agentid);
        startActivity(intent);
    }

    private void sendUserToUserHomeActivity() {
        Intent intent=new Intent(this,UserHomeActivity.class);
        startActivity(intent);

    }
}

package com.example.hotelbooking.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hotelbooking.Model.UserRoom;
import com.example.hotelbooking.R;
import com.example.hotelbooking.ViewHolders.UserBookingsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminCheckUsersBookingsActivity extends AppCompatActivity {
    private String userid,username;
    private DatabaseReference bookingsref;
    private RecyclerView recyclerView;
    private TextView heading;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_check_users_bookings);

        recyclerView=findViewById(R.id.admin_check_user_bookings_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        userid=getIntent().getExtras().get("userid").toString();
        username=getIntent().getExtras().get("username").toString();
        bookingsref= FirebaseDatabase.getInstance().getReference().child("Bookings");

        heading=findViewById(R.id.admin_check_user_bookings_heading);
        heading.setText(username+" Bookings");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<UserRoom> options=new FirebaseRecyclerOptions.Builder<UserRoom>()
                .setQuery(bookingsref.child(userid).orderByChild("bookingstatus").equalTo("booked"),UserRoom.class)
                .build();
        FirebaseRecyclerAdapter<UserRoom, UserBookingsViewHolder> adapter=new FirebaseRecyclerAdapter<UserRoom, UserBookingsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserBookingsViewHolder userBookingsViewHolder, int i, @NonNull UserRoom userRoom) {
                userBookingsViewHolder.hotelnameTv.setText(userRoom.getHotelname());
                userBookingsViewHolder.hoteladdressTv.setText(userRoom.getHoteladdress());
                userBookingsViewHolder.roomtypeTv.setText("Room type: "+userRoom.getRoomtype());
                userBookingsViewHolder.noofroomsTv.setText("No of Rooms: "+userRoom.getNoofrooms());
                userBookingsViewHolder.noofpersonsTv.setText("No of Persons: "+userRoom.getNoofpersons());
                userBookingsViewHolder.bookingdatesTv.setText("Booking dates: "+userRoom.getBookingdates());
                userBookingsViewHolder.totalpriceTv.setText("Total Price: "+userRoom.getTotalprice());
                userBookingsViewHolder.paymentstatus.setText("Payment status: "+userRoom.getPaymentstatus());
                Picasso.get().load(userRoom.getRoomimage()).into(userBookingsViewHolder.roomimage);
            }
            @NonNull
            @Override
            public UserBookingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_bookings_card_view,parent,false);
                final UserBookingsViewHolder holder=new UserBookingsViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}

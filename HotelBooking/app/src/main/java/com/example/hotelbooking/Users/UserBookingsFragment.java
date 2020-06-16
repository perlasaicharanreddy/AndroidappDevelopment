package com.example.hotelbooking.Users;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hotelbooking.Admin.AdminCheckUsersBookingsActivity;
import com.example.hotelbooking.Model.UserRoom;
import com.example.hotelbooking.R;
import com.example.hotelbooking.ViewHolders.UserBookingsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserBookingsFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference bookingref,selectedbookingref,agentBookingref;
    private FirebaseAuth mauth;
    public String currentuserid,selectectedbookingid1,selectectedbookingid2;
    public String selectedbookingagentid,selectedbookingrefkey;

    public UserBookingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_user_bookings, container, false);
        mauth=FirebaseAuth.getInstance();
        currentuserid=mauth.getCurrentUser().getUid();
        bookingref= FirebaseDatabase.getInstance().getReference().child("Bookings").child(currentuserid);
        agentBookingref= FirebaseDatabase.getInstance().getReference().child("AgentBookings");

        recyclerView=view.findViewById(R.id.user_bookings_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<UserRoom> options=new FirebaseRecyclerOptions.Builder<UserRoom>()
                .setQuery(bookingref.orderByChild("bookingstatus").equalTo("booked"),UserRoom.class)
                .build();
        FirebaseRecyclerAdapter<UserRoom, UserBookingsViewHolder> adapter=new FirebaseRecyclerAdapter<UserRoom, UserBookingsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserBookingsViewHolder userBookingsViewHolder, final int i, @NonNull UserRoom userRoom) {
                userBookingsViewHolder.hotelnameTv.setText(userRoom.getHotelname());
                userBookingsViewHolder.hoteladdressTv.setText(userRoom.getHoteladdress());
                userBookingsViewHolder.roomtypeTv.setText("Room type: "+userRoom.getRoomtype());
                userBookingsViewHolder.noofroomsTv.setText("No of Rooms: "+userRoom.getNoofrooms());
                userBookingsViewHolder.noofpersonsTv.setText("No of Persons: "+userRoom.getNoofpersons());
                userBookingsViewHolder.bookingdatesTv.setText("Booking dates: "+userRoom.getBookingdates());
                userBookingsViewHolder.totalpriceTv.setText("Total Price: "+userRoom.getTotalprice());
                userBookingsViewHolder.paymentstatus.setText("Payment status: "+userRoom.getPaymentstatus());
                Picasso.get().load(userRoom.getRoomimage()).into(userBookingsViewHolder.roomimage);
                userBookingsViewHolder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                        menu.add("Cancel Booking").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                selectectedbookingid1= getRef(i).getKey();
                                selectectedbookingid2= getRef(i).getKey();
                                removeOrder(selectectedbookingid1);
                                return true;
                            }
                        });
                    }
                });
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

    public void removeOrder(String selectectedbookingid) {
        bookingref.child(selectectedbookingid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                selectedbookingagentid = dataSnapshot.child("agentid").getValue().toString();
                agentBookingref.child(selectedbookingagentid).child(selectectedbookingid1).removeValue();
                bookingref.child(selectectedbookingid2).removeValue();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}


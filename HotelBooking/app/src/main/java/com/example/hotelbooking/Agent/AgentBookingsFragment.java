package com.example.hotelbooking.Agent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hotelbooking.Model.AgentBookings;
import com.example.hotelbooking.Model.UserRoom;
import com.example.hotelbooking.R;
import com.example.hotelbooking.ViewHolders.AgentBookingsViewHolder;
import com.example.hotelbooking.ViewHolders.UserBookingsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgentBookingsFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference agentbookingref;
    private FirebaseAuth mauth;
    private String currentuserid;
    private Log log;

    public AgentBookingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_agent_bookings, container, false);
        agentbookingref= FirebaseDatabase.getInstance().getReference().child("AgentBookings");
        mauth= FirebaseAuth.getInstance();
        currentuserid=mauth.getCurrentUser().getUid();

        recyclerView=view.findViewById(R.id.agent_bookings_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AgentBookings> options=new FirebaseRecyclerOptions.Builder<AgentBookings>()
                .setQuery(agentbookingref.child(currentuserid).orderByChild("bookingstatus").equalTo("booked"),AgentBookings.class)
                .build();
        FirebaseRecyclerAdapter<AgentBookings, AgentBookingsViewHolder> adapter=new FirebaseRecyclerAdapter<AgentBookings, AgentBookingsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AgentBookingsViewHolder agentBookingsViewHolder, int i, @NonNull AgentBookings agentBookings) {
                agentBookingsViewHolder.guestname.setText("Guest name: "+agentBookings.getGuestname());
                agentBookingsViewHolder.guestphone.setText("Guest phone: "+agentBookings.getGuestphone());
                agentBookingsViewHolder.guestemail.setText("Guest email: "+agentBookings.getGuestemail());
                agentBookingsViewHolder.roomtype.setText("ROom type:"+agentBookings.getRoomtype());
                agentBookingsViewHolder.noofrooms.setText("No of Rooms: "+agentBookings.getNoofrooms());
                agentBookingsViewHolder.noofpersons.setText("No of persons: "+agentBookings.getNoofpersons());
                agentBookingsViewHolder.bookingdates.setText("date of booking: "+agentBookings.getBookingdates());
                agentBookingsViewHolder.totalprice.setText("Total price: "+agentBookings.getTotalprice());
                agentBookingsViewHolder.paymentstatus.setText("Payment status: "+agentBookings.getPaymentstatus());
                Picasso.get().load(agentBookings.getRoomimage()).into(agentBookingsViewHolder.roomImage);
            }

            @NonNull
            @Override
            public AgentBookingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.agent_bookings_card_view,parent,false);
                final AgentBookingsViewHolder holder=new AgentBookingsViewHolder(view);
                return holder;            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

}

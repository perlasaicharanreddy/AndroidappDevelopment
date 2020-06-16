package com.example.hotelbooking.Agent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hotelbooking.Model.AgentRoom;
import com.example.hotelbooking.R;
import com.example.hotelbooking.ViewHolders.AgentRoomsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgentHomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference roomref;
    private FirebaseAuth mauth;
    private String currentuserid;

    public AgentHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_agent_home, container, false);

        roomref= FirebaseDatabase.getInstance().getReference().child("Rooms");
        mauth=FirebaseAuth.getInstance();
        currentuserid=mauth.getCurrentUser().getUid();

        recyclerView=view.findViewById(R.id.agent_home_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        registerForContextMenu(recyclerView);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<AgentRoom> options=new FirebaseRecyclerOptions.Builder<AgentRoom>()
                .setQuery(roomref.orderByChild("agentid").equalTo(currentuserid),AgentRoom.class)
                .build();

        FirebaseRecyclerAdapter<AgentRoom, AgentRoomsViewHolder> adapter=new FirebaseRecyclerAdapter<AgentRoom, AgentRoomsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AgentRoomsViewHolder agentRoomsViewHolder, int i, @NonNull AgentRoom agentRoom) {
                agentRoomsViewHolder.roomtype.setText("Room type: "+agentRoom.getRoomtype());
                agentRoomsViewHolder.roomprice.setText("Room price: "+agentRoom.getRoomprice());
                agentRoomsViewHolder.roomdiscription.setText(agentRoom.getRoomdiscription());
                agentRoomsViewHolder.noofrooms.setText("No of Rooms: "+agentRoom.getNoofrooms());
                Picasso.get().load(agentRoom.getRoomimage()).into(agentRoomsViewHolder.roomimage);


            }

            @NonNull
            @Override
            public AgentRoomsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.agent_room_card_view,parent,false);
                final AgentRoomsViewHolder holder=new AgentRoomsViewHolder(view);
                return holder;


            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }
}

package com.example.hotelbooking.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

public class AdminCheckAgentRoomsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference roomref,selectedroomref;
    private String agentid,agentname;
    private TextView heading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_check_agent_rooms);

        agentid=getIntent().getExtras().get("agentid").toString();
        agentname=getIntent().getExtras().get("agentname").toString();
        roomref= FirebaseDatabase.getInstance().getReference().child("Rooms");

        heading=findViewById(R.id.admin_check_agent_rooms_heading);
        heading.setText(agentname+" Rooms");


        recyclerView=findViewById(R.id.admin_check_agent_rooms_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        registerForContextMenu(recyclerView);

    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<AgentRoom> options=new FirebaseRecyclerOptions.Builder<AgentRoom>()
                .setQuery(roomref.orderByChild("agentid").equalTo(agentid),AgentRoom.class)
                .build();

        FirebaseRecyclerAdapter<AgentRoom, AgentRoomsViewHolder> adapter=new FirebaseRecyclerAdapter<AgentRoom, AgentRoomsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AgentRoomsViewHolder agentRoomsViewHolder, final int i, @NonNull AgentRoom agentRoom) {
                agentRoomsViewHolder.roomtype.setText("Room type: "+agentRoom.getRoomtype());
                agentRoomsViewHolder.roomprice.setText("Room price: "+agentRoom.getRoomprice());
                agentRoomsViewHolder.roomdiscription.setText(agentRoom.getRoomdiscription());
                agentRoomsViewHolder.noofrooms.setText("No of Rooms: "+agentRoom.getNoofrooms());
                Picasso.get().load(agentRoom.getRoomimage()).into(agentRoomsViewHolder.roomimage);
                agentRoomsViewHolder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                        menu.add("delete room").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                selectedroomref=getRef(i);
                                selectedroomref.removeValue();
                                return true;
                            }
                        });
                    }
                });
            }
            @NonNull
            @Override
            public AgentRoomsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.agent_room_card_view,parent,false);
                final AgentRoomsViewHolder holder=new AgentRoomsViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }
}

package com.example.hotelbooking.Admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hotelbooking.Agent.AgentEditRoomActivity;
import com.example.hotelbooking.Model.AdminManageUsers;
import com.example.hotelbooking.R;
import com.example.hotelbooking.ViewHolders.AdminManageUsersViewHolder;
import com.example.hotelbooking.ViewHolders.AgentBookingsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminMagnageUsersFragment extends Fragment {
    DatabaseReference usersref,selecteduserref;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    public AdminMagnageUsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_admin_magnage_users, container, false);
        usersref= FirebaseDatabase.getInstance().getReference().child("Users");
        recyclerView=view.findViewById(R.id.admin_manage_users_recyclear_view);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<AdminManageUsers> options=new FirebaseRecyclerOptions.Builder<AdminManageUsers>()
                .setQuery(usersref.orderByChild("admin").equalTo("access"),AdminManageUsers.class)
                .build();
        FirebaseRecyclerAdapter<AdminManageUsers,AdminManageUsersViewHolder> adapter=new FirebaseRecyclerAdapter<AdminManageUsers, AdminManageUsersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminManageUsersViewHolder adminManageUsersViewHolder, final int i, @NonNull final AdminManageUsers adminManageUsers) {
                adminManageUsersViewHolder.name.setText(adminManageUsers.getName());
                adminManageUsersViewHolder.phone.setText("Phone no: "+adminManageUsers.getPhone());
                adminManageUsersViewHolder.email.setText("Email: "+adminManageUsers.getEmail());
                adminManageUsersViewHolder.address.setText("address: "+adminManageUsers.getAddress());
                adminManageUsersViewHolder.idcard.setText("id card: "+adminManageUsers.getIdcard());
                adminManageUsersViewHolder.city.setText("city: "+adminManageUsers.getCity());
                adminManageUsersViewHolder.birthady.setText("birthday: "+adminManageUsers.getBirthday());
                Picasso.get().load(adminManageUsers.getUserimage()).placeholder(R.drawable.profile).into(adminManageUsersViewHolder.userimage);

                adminManageUsersViewHolder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                        menu.add("check bookings").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                selecteduserref=getRef(i);
                                String userid=selecteduserref.getKey();
                                //  Toast.makeText(getContext(),userid,Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(getContext(), AdminCheckUsersBookingsActivity.class);
                                intent.putExtra("userid",userid);
                                intent.putExtra("username",adminManageUsers.getName());
                                startActivity(intent);
                                return true;
                            }
                        });
                        menu.add("delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                selecteduserref=getRef(i);
                                selecteduserref.removeValue();
                                return true;
                            }
                        });


                    }
                });
            }

            @NonNull
            @Override
            public AdminManageUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_users_display_card_view,parent,false);
                final AdminManageUsersViewHolder holder=new AdminManageUsersViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

}

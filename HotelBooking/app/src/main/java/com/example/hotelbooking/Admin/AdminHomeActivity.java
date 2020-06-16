package com.example.hotelbooking.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hotelbooking.Agent.AddNewRoomCategoryFragment;
import com.example.hotelbooking.Agent.AgentBookingsFragment;
import com.example.hotelbooking.Agent.AgentHomeActivity;
import com.example.hotelbooking.Agent.AgentHomeFragment;
import com.example.hotelbooking.Agent.AgentManageFragment;
import com.example.hotelbooking.Agent.AgentProfileActivity;
import com.example.hotelbooking.Agent.HotelDetailsActivity;
import com.example.hotelbooking.MainActivity;
import com.example.hotelbooking.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdminHomeActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private DatabaseReference adminref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        adminref= FirebaseDatabase.getInstance().getReference().child("Admin");

        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        drawer=findViewById(R.id.admin_draw_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.Open_navigation_drawer,R.string.Close_navigation_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.admin_nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent intent;
                switch (menuItem.getItemId()){
                    case R.id.admin_nav_profile:
                        intent = new Intent(AdminHomeActivity.this, AdminProfileActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.admin_nav_settings:
                        intent = new Intent(AdminHomeActivity.this, AdminProfileActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.admin_nav_logout:
                        intent=new Intent(AdminHomeActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        break;

                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment_container,new AdminMagnageUsersFragment()).commit();
        BottomNavigationView bottomNavigationView=findViewById(R.id.admin_bottom_navigation);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedfragment=null;
                switch (menuItem.getItemId()){
                    case R.id.admin_bottom_nav_manage_users:
                        selectedfragment=new AdminMagnageUsersFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment_container,selectedfragment).commit();
                        break;
                     case R.id.admin_nav_manage_agents:
                        selectedfragment=new AdminManageAgentsFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment_container,selectedfragment).commit();
                        break;
                    case R.id.admin_nav_manage_rooms:
                        selectedfragment=new AdminManageRoomsFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment_container,selectedfragment).commit();
                        break;
                }
                return true;
            }
        });
        displayUserNameandImage();
    }
    private void displayUserNameandImage() {
        NavigationView navigationView = findViewById(R.id.admin_nav_view);
        View HeaderView=navigationView.getHeaderView(0);
       final TextView adminprofilename=HeaderView.findViewById(R.id.admin_profile_name_nav);
       final ImageView  adminprofileimage=HeaderView.findViewById(R.id.admin_profile_image_nav);
        adminref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()&&dataSnapshot.hasChild("name")){
                    adminprofilename.setText(dataSnapshot.child("name").getValue().toString());
                }
                if(dataSnapshot.exists()&&dataSnapshot.hasChild("adminimage")){
                    Picasso.get().load(dataSnapshot.child("adminimage").getValue().toString()).into(adminprofileimage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}

package com.example.hotelbooking.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.hotelbooking.Login.SignInORSignUpActivity;
import com.example.hotelbooking.MainActivity;
import com.example.hotelbooking.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserHomeActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private FirebaseAuth mauth;
    private DatabaseReference userref;
    private String currentuserid;
    private TextView userprofilename;
    private ImageView userprofileimage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        mauth=FirebaseAuth.getInstance();
        currentuserid=mauth.getCurrentUser().getUid();
        userref= FirebaseDatabase.getInstance().getReference().child("Users");


        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        drawer=findViewById(R.id.draw_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.Open_navigation_drawer,R.string.Close_navigation_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent intent;
                switch (menuItem.getItemId()){
                    case R.id.user_nav_profile:
                        intent = new Intent(UserHomeActivity.this, UserProfileActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.user_nav_search:
                        intent=new Intent(UserHomeActivity.this,SearchActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.user_nav_all_rooms:
                        intent=new Intent(UserHomeActivity.this,UserAllRoomsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.user_nav_settings:
                        intent = new Intent(UserHomeActivity.this, UserProfileActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.user_nav_logout:
                        final FirebaseAuth mauth;
                        mauth=FirebaseAuth.getInstance();
                        mauth.signOut();
                        intent=new Intent(UserHomeActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_contact_us:
                        intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+"7901048098"));
                        startActivity(intent);
                        break;
                    case R.id.nav_send:
                        intent = new Intent(Intent.ACTION_SENDTO,Uri.parse("mailto:"+"charan@gmail.com"));
                        intent.putExtra(Intent.EXTRA_SUBJECT,"complaint");
                        intent.putExtra(Intent.EXTRA_TEXT,"enter text here:");
                        startActivity(Intent.createChooser(intent,"Chooser title"));
                        break;
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });




        getSupportFragmentManager().beginTransaction().replace(R.id.user_fragment_container,new UserHomeFragment()).commit();
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedfragment=null;
                switch (menuItem.getItemId()){
                    case R.id.user_bottom_nav_home:
                        selectedfragment=new UserHomeFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.user_fragment_container,selectedfragment).commit();
                        break;
                     case R.id.user_bottom_nav_saved:
                         selectedfragment=new UserSavedFragment();
                         getSupportFragmentManager().beginTransaction().replace(R.id.user_fragment_container,selectedfragment).commit();
                        break;
                    case R.id.user_bottom_nav_bookings:
                        selectedfragment=new UserBookingsFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.user_fragment_container,selectedfragment).commit();
                        break;
                }
                return true;
            }
        });

        displayUserNameandImage();

    }

    private void displayUserNameandImage() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View HeaderView=navigationView.getHeaderView(0);
        userprofilename=HeaderView.findViewById(R.id.user_profile_name_nav);
        userprofileimage=HeaderView.findViewById(R.id.user_profile_image_nav);
        userref.child(currentuserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()&&dataSnapshot.hasChild("name")){
                    userprofilename.setText(dataSnapshot.child("name").getValue().toString());
                }
                if(dataSnapshot.exists()&&dataSnapshot.hasChild("userimage")){
                    Picasso.get().load(dataSnapshot.child("userimage").getValue().toString()).into(userprofileimage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.search,menu);
//        SearchManager searchManager=(SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView=(SearchView)menu.findItem(R.id.action_search);
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setMaxWidth(Integer.MAX_VALUE);
//        searchView.setQueryHint(getString(R.string.search()));
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//        return true;
//    }
}

package com.example.apoorva.pictonification;

import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    Fragment events = new Events();
    Fragment study_material = new StudyMaterial();
    Fragment forum = new Forum();

    private List<FeedData> feedList = new ArrayList<>();
    private RecyclerView feedRecyclerView;
    private FeedAdapter feedAdapter;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("PICTONIFICATION");

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view = navigationView.getHeaderView(0);
        TextView EmailView = (TextView) view.findViewById(R.id.NavEmail);
        TextView UserView = (TextView) view.findViewById(R.id.NavUser);
        EmailView.setText(firebaseUser.getEmail());
        SharedPreferences sharedPreferences = getSharedPreferences("myfile", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name","");
        UserView.setText(name);

        feedRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Feed").child("Notices");
        childEventListener=null;

        feedAdapter = new FeedAdapter(feedList,getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        feedRecyclerView.setLayoutManager(layoutManager);
        feedRecyclerView.setItemAnimator(new DefaultItemAnimator());
        feedRecyclerView.setAdapter(feedAdapter);

        attachDatabaseReadListener();
    }

    public void attachDatabaseReadListener(){
        if(childEventListener==null){
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(Dashboard.this);
                    mBuilder.setSmallIcon(R.drawable.ic_action_notification);
                    mBuilder.setContentTitle("Pictonification");
                    mBuilder.setContentText("New notice uploaded");
                    long[] v = {500,1000};
                    mBuilder.setVibrate(v);
                    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    mBuilder.setSound(uri);
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(0,mBuilder.build());
                    FeedData feedData = dataSnapshot.getValue(FeedData.class);
                    feedList.add(0, feedData);
                    feedAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    FeedData feedData = dataSnapshot.getValue(FeedData.class);
                    feedList.add(feedData);
                    feedAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    FeedData feedData = dataSnapshot.getValue(FeedData.class);
                    feedList.add(feedData);
                    feedAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(),databaseError.toString(), Toast.LENGTH_SHORT).show();
                }
            };
            databaseReference.addChildEventListener(childEventListener);
        }
    }

    public void detachDatabaseListener(){
        if (childEventListener != null) {
            databaseReference.removeEventListener(childEventListener);
            childEventListener = null;
        }

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finishAffinity();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_events) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_dashboard_frame,events).commit();

        } else if (id == R.id.nav_home) {
            Intent i=new Intent(Dashboard.this, Dashboard.class);
            startActivity(i);

        } else if (id == R.id.nav_study_material) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_dashboard_frame, study_material).commit();

        } else if (id == R.id.nav_forum) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_dashboard_frame, forum).commit();

        }else if (id == R.id.nav_logout) {

            FirebaseAuth.getInstance().signOut();
            Intent logintent=new Intent(Dashboard.this,LogIn.class);
            finish();
            startActivity(logintent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        /*Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Hello World!"));*/

        return true;
    }

}

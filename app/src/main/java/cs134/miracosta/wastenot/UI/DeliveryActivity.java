package cs134.miracosta.wastenot.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import cs134.miracosta.wastenot.Model.Donation;
import cs134.miracosta.wastenot.Model.DonationListAdapter;
import cs134.miracosta.wastenot.Model.FirebaseDBHelper;
import cs134.miracosta.wastenot.Model.Location;
import cs134.miracosta.wastenot.Model.User;
import cs134.miracosta.wastenot.R;

public class DeliveryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    public static final String TAG = "WasteNot";

    private FirebaseDBHelper db;
    private List<Donation> donationsList;
    private DonationListAdapter donationsListAdapter;
    private User user;
    private boolean toggleMap;
    // View elements
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private Button newDonationButton;
    private GoogleMap map;
    private SupportMapFragment mapFragment;
    private ListFragment listFragment;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        // Instantiate DBHelper
        db = new FirebaseDBHelper();

        // TODO: get User from authentication
        user = new User();

        // TODO: populate locations list

        // Link View
        toolbar = findViewById(R.id.donation_list_toolbar);
        drawer = findViewById(R.id.donation_list_drawer_layout);
        newDonationButton = findViewById(R.id.newDonationButton);
        setSupportActionBar(toolbar);


        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
        toggleMap = true;

        fm = getSupportFragmentManager();

        NavigationView navigationView = findViewById(R.id.donation_list_nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delivery_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.miToggle) {
            if (toggleMap) {
                toolbar.setNavigationIcon(R.drawable.ic_map_white);
                fm.beginTransaction()
                        .setCustomAnimations(R.anim.rotate, R.anim.rotate)
                        .show(listFragment)
                        .commit();
            }
            return true;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.donation_list_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_donate:
                startActivity(new Intent(this, DonationListActivity.class));
                break;
            case R.id.nav_claim:
                startActivity(new Intent(this, ClaimsListActivity.class));
                break;
            case R.id.nav_deliver:
                // Do nothing
                break;
            case R.id.nav_logout:
                startActivity(new Intent(this, LoginActivity.class));
                finishAffinity();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void goToDeliveryDetails(View v)
    {
        Intent newDonationIntent = new Intent(this, NewDonationActivity.class);
        // TODO: parcelable User
        //newDonationIntent.putExtra("User", user);
        startActivity(newDonationIntent);
    }

    public void goToUserDeliveryList(View v)
    {
        Intent newDonationIntent = new Intent(this, NewDonationActivity.class);
        // TODO: parcelable User
        //newDonationIntent.putExtra("User", user);
        startActivity(newDonationIntent);
    }

    public void toggleFragment(View v)
    {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG, "Entering onMapReady");    // debug

        map = googleMap;
        // Specify our location with LatLng class
        LatLng myPosition = new LatLng(33.190802, -117.301805);

        // Add position to the map
        map.addMarker(new MarkerOptions()
                .position(myPosition)
                .title("Current Location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.my_marker)));

        // Create new camera location at current location
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(myPosition)
                .zoom(15.0f)
                .build();
        // Update the position (move to location)
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        // Instruct map to move to this position
        map.moveCamera(cameraUpdate);

        // Add all caffeine locations
        LatLng position;
        /*
        for (Location location : allLocationsList)
        {
            position = new LatLng(location.getLatitude(), location.getLongitude());
            map.addMarker(new MarkerOptions()
                    .position(position)
                    .title(location.getName()));
        }
        */

    }
}

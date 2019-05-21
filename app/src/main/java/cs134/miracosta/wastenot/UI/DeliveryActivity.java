package cs134.miracosta.wastenot.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.google.android.gms.maps.GoogleMap;

import java.util.List;

import cs134.miracosta.wastenot.Model.Delivery;
import cs134.miracosta.wastenot.Model.Donation;
import cs134.miracosta.wastenot.Model.FirebaseDBHelper;
import cs134.miracosta.wastenot.Model.User;
import cs134.miracosta.wastenot.R;
import cs134.miracosta.wastenot.UI.Adapters.DonationListAdapter;

public class DeliveryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "WasteNot";

    private FirebaseDBHelper db;
    private List<Donation> donationsList;
    private DonationListAdapter donationsListAdapter;
    private User user;
    private boolean toggleMap;
    // View elements
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private GoogleMap map;
    private DeliveryMapFragment mapFragment;
    private DeliveryListFragment listFragment;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        // Instantiate DBHelper
        db = new FirebaseDBHelper();

        // TODO: populate locations list

        // Link View
        toolbar = findViewById(R.id.delivery_toolbar);
        drawer = findViewById(R.id.delivery_drawer_layout);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.delivery_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mapFragment = new DeliveryMapFragment();
        listFragment = new DeliveryListFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, listFragment)
                .commit();

        toggleMap = true;

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
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.anim_rotate_reverse, R.anim.anim_rotate)
                        .replace(R.id.fragment_container, mapFragment)
                        .commit();
                item.setIcon(R.drawable.ic_list_white);
            } else {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.anim_rotate_reverse, R.anim.anim_rotate)
                        .replace(R.id.fragment_container, listFragment)
                        .commit();
                item.setIcon(R.drawable.ic_map_white);

            }
            Log.i(TAG, "Is List Fragment hidden? " + listFragment.isHidden());
            Log.i(TAG, "Is Map Fragment hidden? " + mapFragment.isHidden());
            toggleMap = !toggleMap;
            return true;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
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
        if (v.getTag() == null)
            return;

        Delivery delivery = (Delivery) v.getTag();
        Intent deliveryDetailsIntent = new Intent(this, DeliveryDetailsActivity.class);
        deliveryDetailsIntent.putExtra("Claimer", delivery.getClaimer());
        deliveryDetailsIntent.putExtra("Donor", delivery.getDonor());
        deliveryDetailsIntent.putExtra("Donation", delivery.getDonation());
        deliveryDetailsIntent.putExtra("DonorLocation", delivery.getDonor().getLocation());
        deliveryDetailsIntent.putExtra("ClaimerLocation", delivery.getClaimer().getLocation());
        startActivity(deliveryDetailsIntent);
    }

    public void goToUserDeliveries(View v)
    {
        Intent userDeliveriesIntent = new Intent(this, UserDeliveriesActivity.class);
        startActivity(userDeliveriesIntent);
    }





    /*
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mapFragment)
                .commit();
         */



        /*
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            toggleMap = true;
        } else {
            Log.w(TAG, "Error loading Google Map.");
        }

        fm = getSupportFragmentManager();
        //listFragment = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.listFragment);
        */
    //mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);

    /*
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


    }

    public class MapFragment extends com.google.android.gms.maps.MapFragment {
        @Override
        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            return layoutInflater.inflate(R.layout.fragment_delivery_map, viewGroup, false);
            toolbar =
        }
    }
    */
}

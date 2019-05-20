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

import cs134.miracosta.wastenot.Model.Donation;
import cs134.miracosta.wastenot.Model.FirebaseDBHelper;
import cs134.miracosta.wastenot.Model.User;
import cs134.miracosta.wastenot.R;
import cs134.miracosta.wastenot.UI.Adapters.DonationListAdapter;

public class DeliveryDetailsActivity extends AppCompatActivity
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

        // TODO: get User from authentication
        user = new User();

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
        DrawerLayout drawer = findViewById(R.id.delivery_drawer_layout);
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

    public void claimDelivery(View v)
    {

    }

}

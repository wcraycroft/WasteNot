package cs134.miracosta.wastenot.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import cs134.miracosta.wastenot.Model.Delivery;
import cs134.miracosta.wastenot.R;

/**
 * This controller class handles the Delivery Activity, which consists of two exclusive fragments,
 * a Google Maps fragment and a List fragment, both showing all local Deliveries near the user.
 *
 * @author Will Craycroft
 */
public class DeliveryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "WasteNot";

    private boolean toggleMap;
    // View elements
    /***/
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private DeliveryMapFragment mapFragment;
    private DeliveryListFragment listFragment;

    /**
     * Inflates View and instantiate both fragments used in this layout.
     * @param savedInstanceState - Bundle data from previous instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

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
                .replace(R.id.fragment_container, mapFragment)
                .commit();

        toggleMap = false;
    }

    /**
     * Starts a new DeliveryDetails Activity and passes the intent Delivery information.
     * @param v - the calling Button
     */
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

    /**
     * Starts a new UserDeliveriesActivity. Does not pass any additional data.
     * @param v - the calling Button
     */
    public void goToUserDeliveries(View v)
    {
        Intent userDeliveriesIntent = new Intent(this, UserDeliveriesActivity.class);
        startActivity(userDeliveriesIntent);
    }


    /**
     * Navigation Drawer overrides
     */

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



}

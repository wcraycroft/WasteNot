package cs134.miracosta.wastenot.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cs134.miracosta.wastenot.Model.Delivery;
import cs134.miracosta.wastenot.Model.FirebaseDBHelper;
import cs134.miracosta.wastenot.R;
import cs134.miracosta.wastenot.UI.Adapters.UserDeliveryListAdapter;

/**
 * This controller class handles the UserDeliveriesActivity, which shows the Driver a list of all
 * their active Deliveries. The inflated list item has a button to handle completing the delivery,
 * which will remove the Delivery from the DB.
 *
 * @author Will Craycroft
 */
public class UserDeliveriesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "WasteNot";
    private ListView myDeliveriesListView;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    FirebaseDBHelper db;
    List<Delivery> userDeliveriesList = new ArrayList<>();
    UserDeliveryListAdapter deliveryListAdapter;

    /**
     * Inflates view, instantiates DBHelper and gets all user Deliveries
     * @param savedInstanceState - Bundle data from previous instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_deliveries);

        // Instantiate DBHelper
        db = new FirebaseDBHelper();

        // Link View
        myDeliveriesListView = findViewById(R.id.myDeliveriesListView);
        toolbar = findViewById(R.id.user_deliveries_toolbar);
        drawer = findViewById(R.id.user_deliveries_drawer_layout);
        setSupportActionBar(toolbar);

        // Set drawer, toolbar and listeners
        NavigationView navigationView = findViewById(R.id.user_deliveries_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Get deliveries from DB
        getUserDeliveries();

    }

    /**
     * Populates a list of the current user's active Deliveries from DB
     */
    private void getUserDeliveries() {
        final String email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

        // Populate list of donations
        db.getUserDeliveriesRealTime(email, new FirebaseDBHelper.DataStatus() {
            @Override
            public void DataIsRead(List<?> items) {
                userDeliveriesList.clear();
                userDeliveriesList = (List<Delivery>) items;
                setListAdapter();
            }

            @Override
            public void DataIsProcessed() { }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(UserDeliveriesActivity.this,
                        getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Refreshes the list adapted in case of realtime DB callbacks.
     */
    private void setListAdapter()
    {
        // Set list adapter
        deliveryListAdapter = new UserDeliveryListAdapter(UserDeliveriesActivity.this, R.layout.list_item_user_delivery, userDeliveriesList);
        myDeliveriesListView.setAdapter(deliveryListAdapter);
    }

    /**
     * Handles the deletion of a Delivery (Donation + Makes) once the user indicates they have
     * completed the delivery. There is currently no confirmation needed from Claimer.
     * @param v - the calling Button (from list item)
     */
    public void onClickDelivered(View v)
    {
        // Get Delivery from tag
        Delivery delivery = (Delivery) v.getTag();
        // Delete Delivery (Donation + Makes) from database
        db.deleteDelivery(delivery.getDonation().getKey(), delivery.getMakesKey(), new FirebaseDBHelper.DataStatus() {
            @Override
            public void DataIsRead(List<?> items) { }

            @Override
            public void DataIsProcessed() {
                Toast.makeText(UserDeliveriesActivity.this, getString(R.string.delivery_registered), Toast.LENGTH_LONG).show();
                // Refresh list
                userDeliveriesList.clear();
                deliveryListAdapter.notifyDataSetChanged();
                getUserDeliveries();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(UserDeliveriesActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Overrides the Back button press while the Navigation drawer is open. Closes the navigation
     * drawer rather than the activity.
     */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Navigation Drawer Override. Handles the item clicks for any list item in the drawer.
     * @param item - the drawer item that was clicked
     * @return - True if the event was handled
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_donate:
                startActivity(new Intent(this, DonationListActivity.class));
                finish();
                break;
            case R.id.nav_claim:
                startActivity(new Intent(this, ClaimsListActivity.class));
                finish();
                break;
            case R.id.nav_deliver:
                startActivity(new Intent(this, DeliveryActivity.class));
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

package cs134.miracosta.wastenot.UI;

import android.content.Context;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cs134.miracosta.wastenot.Model.Delivery;
import cs134.miracosta.wastenot.Model.Donation;
import cs134.miracosta.wastenot.Model.FirebaseDBHelper;
import cs134.miracosta.wastenot.Model.User;
import cs134.miracosta.wastenot.R;
import cs134.miracosta.wastenot.UI.Adapters.DeliveryListAdapter;

public class UserDeliveriesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "WasteNot";
    private ListView myDeliveriesListView;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    FirebaseDBHelper db;
    List<Delivery> userDeliveriesList = new ArrayList<>();
    DeliveryListAdapter deliveryListAdapter;

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

    public void getUserDeliveries() {
        final String email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

        // Populate list of donations
        db.getUserDeliveries(email, new FirebaseDBHelper.DataStatus() {
            @Override
            public void DataIsRead(List<?> items) {
                userDeliveriesList.clear();
                userDeliveriesList = (List<Delivery>) items;
                Log.i(TAG, "Data retrieved. User Deliveries in list = " + userDeliveriesList.size());
                // Set list adapter
                deliveryListAdapter = new DeliveryListAdapter(UserDeliveriesActivity.this, R.layout.list_item_delivery, userDeliveriesList);
                myDeliveriesListView.setAdapter(deliveryListAdapter);
            }

            @Override
            public void DataIsProcessed() { }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(UserDeliveriesActivity.this,
                        "Error retrieving your deliveries. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });

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

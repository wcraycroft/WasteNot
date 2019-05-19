// TODO: Copy layout to a new unique nav drawer


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
import android.widget.Button;

import java.util.List;

import cs134.miracosta.wastenot.Model.Donation;
import cs134.miracosta.wastenot.Model.DonationListAdapter;
import cs134.miracosta.wastenot.Model.FirebaseDBHelper;
import cs134.miracosta.wastenot.R;

public class NewDonationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "WasteNot";

    private FirebaseDBHelper db;
    // View elements
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private Button addDonationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_donation);

        // Instantiate DBHelper
        db = new FirebaseDBHelper();

        // Link View
        toolbar = findViewById(R.id.new_donation_toolbar);
        drawer = findViewById(R.id.new_donation_drawer_layout);
        addDonationButton = findViewById(R.id.addDonationButton);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.new_donation_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
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
                break;
            case R.id.nav_claim:
                startActivity(new Intent(this, ClaimsListActivity.class));
                break;
            case R.id.nav_deliver:
                startActivity(new Intent(this, DeliveryActivity.class));
                break;
            case R.id.nav_logout:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }

        finish();
        return true;
    }

    public void addDonation(View v)
    {
        // TODO: add donation to DB
        finish();
    }
}

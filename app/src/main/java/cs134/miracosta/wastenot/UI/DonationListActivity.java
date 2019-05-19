package cs134.miracosta.wastenot.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

import cs134.miracosta.wastenot.Model.Donation;
import cs134.miracosta.wastenot.UI.Adapters.DonationListAdapter;
import cs134.miracosta.wastenot.Model.FirebaseDBHelper;
import cs134.miracosta.wastenot.Model.User;
import cs134.miracosta.wastenot.R;

public class DonationListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "WasteNot";

    private FirebaseDBHelper db;
    private List<Donation> donationsList;
    private DonationListAdapter donationsListAdapter;
    private User user;
    // View elements
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private ListView donationsListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_list);

        // Instantiate DBHelper
        db = new FirebaseDBHelper();

        // Link View
        toolbar = findViewById(R.id.donation_list_toolbar);
        drawer = findViewById(R.id.donation_list_drawer_layout);
        donationsListView = findViewById(R.id.donationsListView);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.donation_list_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Populate list of donations
        getUserDonations();
    }

    private void getUserDonations()
    {
        String userEmail = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

        db.getUserByEmail(userEmail, new FirebaseDBHelper.DataStatus() {
            @Override
            public void DataIsRead(List<?> items) {
                user = (User) items.get(0);
                db.getDonationsByUser(user.getKey(), new FirebaseDBHelper.DataStatus() {
                    @Override
                    public void DataIsRead(List<?> items) {
                        donationsList = (List<Donation>) items;
                        setListAdapter();
                    }

                    @Override
                    public void DataIsProcessed() { }
                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(DonationListActivity.this,
                                "Error retrieving user information.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void DataIsProcessed() { }
            @Override
            public void onError(String errorMessage) {
                Toast.makeText(DonationListActivity.this,
                        "Error retrieving user information.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setListAdapter()
    {
        donationsListAdapter = new DonationListAdapter(this, R.layout.donation_list_item, donationsList);
        donationsListView.setAdapter(donationsListAdapter);
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
                // Do nothing
                break;
            case R.id.nav_claim:
                startActivity(new Intent(this, ClaimsListActivity.class));
                finish();
                break;
            case R.id.nav_deliver:
                startActivity(new Intent(this, DeliveryActivity.class));
                finish();
                break;
            case R.id.nav_logout:
                startActivity(new Intent(this, LoginActivity.class));
                finishAffinity();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void newDonation(View v)
    {
        if (user == null)
        {
            Toast.makeText(this, "Error. Please wait a moment and try again.", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent newDonationIntent = new Intent(this, NewDonationActivity.class);
        newDonationIntent.putExtra("User", user);
        startActivity(newDonationIntent);
    }
}

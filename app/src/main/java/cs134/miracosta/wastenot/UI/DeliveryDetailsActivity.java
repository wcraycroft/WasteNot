package cs134.miracosta.wastenot.UI;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import cs134.miracosta.wastenot.Model.Delivery;
import cs134.miracosta.wastenot.Model.Donation;
import cs134.miracosta.wastenot.Model.FirebaseDBHelper;
import cs134.miracosta.wastenot.Model.Location;
import cs134.miracosta.wastenot.Model.User;
import cs134.miracosta.wastenot.R;

/**
 * This controller class handles the DeliveryDetails Activity, which displays delivery information
 * passed to it through an intent.
 *
 * @author Will Craycroft
 */
public class DeliveryDetailsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {

    public static final String TAG = "WasteNot";

    private FirebaseDBHelper db;
    private Delivery delivery;
    private Donation donation;
    private String pickupTime = "";

    // View elements
    private TextView deliveryDetailsFoodTypeTextView;
    private TextView deliveryDetailsServingsTextView;
    private TextView deliveryDetailsReadyTimeTextView;
    private TextView deliveryDetailsDropoffTimeTextView;
    private TextView deliveryDetailsPickupTimeTextView;
    private TextView deliveryDetailsPickupNameTextView;
    private TextView deliveryDetailsDropoffNameTextView;
    private TextView deliveryDetailsPickupAddressTextView;
    private TextView deliveryDetailsDropoffAddressTextView;

    private Spinner deliveryDetailsPickupSpinner;
    private Button claimDeliveryButton;
    private ImageView deliveryDetailsImageView;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    /**
     * Inflates the view and navigation bar. Grabs data from the intent and populates TextView.
     * @param savedInstanceState - Bundle data from previous instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_details);

        db = new FirebaseDBHelper();

        // Link View
        toolbar = findViewById(R.id.delivery_details_toolbar);
        drawer = findViewById(R.id.delivery_details_drawer_layout);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.delivery_details_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Create Delivery object from intent data
        Intent intent = getIntent();
        delivery = new Delivery();
        delivery.setClaimer((User) intent.getParcelableExtra("Claimer"));
        delivery.setDonor((User) intent.getParcelableExtra("Donor"));
        delivery.setDonation((Donation) intent.getParcelableExtra("Donation"));
        delivery.getDonor().setLocation((Location) intent.getParcelableExtra("DonorLocation"));
        delivery.getClaimer().setLocation((Location) intent.getParcelableExtra("ClaimerLocation"));

        donation = delivery.getDonation();

        // Get View references
        deliveryDetailsFoodTypeTextView = findViewById(R.id.deliveryDetailsFoodTypeTextView);
        deliveryDetailsServingsTextView = findViewById(R.id.deliveryDetailsServingsTextView);
        deliveryDetailsReadyTimeTextView = findViewById(R.id.deliveryDetailsReadyTimeTextView);
        deliveryDetailsDropoffTimeTextView = findViewById(R.id.deliveryDetailsDropoffTimeTextView);
        deliveryDetailsPickupTimeTextView = findViewById(R.id.deliveryDetailsPickupTimeTextView);
        deliveryDetailsPickupNameTextView = findViewById(R.id.deliveryDetailsPickupNameTextView);
        deliveryDetailsDropoffNameTextView = findViewById(R.id.deliveryDetailsDropoffNameTextView);
        deliveryDetailsPickupAddressTextView = findViewById(R.id.deliveryDetailsPickupAddressTextView);
        deliveryDetailsDropoffAddressTextView = findViewById(R.id.deliveryDetailsDropoffAddressTextView);

        deliveryDetailsImageView = findViewById(R.id.deliveryDetailsImageView);
        claimDeliveryButton = findViewById(R.id.claimDeliveryButton);

        // Set Text
        deliveryDetailsServingsTextView.setText(getResources().getString(
                R.string.servings_suffix, donation.getServings()));
        deliveryDetailsPickupTimeTextView.setText(getResources().getString(
                R.string.pickup_by_prefix, donation.getPickupEndTime()));
        deliveryDetailsReadyTimeTextView.setText(getResources().getString(
                R.string.ready_at_prefix, donation.getReadyTime()));
        deliveryDetailsDropoffTimeTextView.setText(getResources().getString(
                R.string.dropoff_by_prefix, donation.getDropoffEndTime()));

        deliveryDetailsPickupNameTextView.setText(delivery.getDonor().getCompanyName());
        deliveryDetailsDropoffNameTextView.setText(delivery.getClaimer().getCompanyName());
        deliveryDetailsPickupAddressTextView.setText(delivery.getDonor().getLocation().getFullAddress());
        deliveryDetailsDropoffAddressTextView.setText(delivery.getClaimer().getLocation().getFullAddress());

        // Set image view
        // Type switch cases
        String imageName = "image.png";
        switch (donation.getFoodType())
        {
            case MEAT:
                imageName = "meat.png";
                deliveryDetailsFoodTypeTextView.setText(R.string.meat);
                break;
            case DAIRY:
                imageName = "dairy.png";
                deliveryDetailsFoodTypeTextView.setText(R.string.dairy);
                break;
            case OTHER:
                imageName = "other.png";
                deliveryDetailsFoodTypeTextView.setText(R.string.other);
                break;
            case PRODUCE:
                imageName = "produce.png";
                deliveryDetailsFoodTypeTextView.setText(R.string.produce);
                break;
            case BAKED_GOODS:
                imageName = "baked.png";
                deliveryDetailsFoodTypeTextView.setText(R.string.baked);
                break;
            case PREPARED_TRAY:
                imageName = "tray.png";
                deliveryDetailsFoodTypeTextView.setText(R.string.packaged_tray);
                break;
            case PREPARED_PACKAGED:
                imageName = "packaged.png";
                deliveryDetailsFoodTypeTextView.setText(R.string.packaged_indiv);
                break;
        }

        // Set image drawable
        AssetManager am = this.getAssets();
        try {
            InputStream stream = am.open(imageName);
            Drawable event = Drawable.createFromStream(stream, imageName);
            deliveryDetailsImageView.setImageDrawable(event);
        }
        catch (IOException ex)
        {
            Log.e(TAG, "Error loading " + imageName, ex);
        }

        // Setup Spinner
        deliveryDetailsPickupSpinner = findViewById(R.id.deliveryDetailsPickupSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.pickup_time_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deliveryDetailsPickupSpinner.setAdapter(adapter);
        deliveryDetailsPickupSpinner.setOnItemSelectedListener(this);

    }

    /**
     * Handle a driver claiming a donation. Calls DBHelper method which will change the states of the
     * delivery and updates the relation in the Makes DB. When done, finishes this activity.
     * @param v - The calling Button
     */
    public void claimDelivery(View v)
    {
        // Check if spinner data is valid
        String[] pickupTimeArray = getResources().getStringArray(R.array.pickup_time_array);
        if (pickupTime.equals(pickupTimeArray[0]) || pickupTime.equals(""))
        {
            Toast.makeText(this, getString(R.string.select_pickup_time), Toast.LENGTH_SHORT).show();
            return;
        }
        // Grab Pickup Time
        delivery.getDonation().setPickupTime(pickupTime);
        // Grab user email
        String email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        // Update DB
        db.claimDelivery(email, delivery, new FirebaseDBHelper.DataStatus() {
            @Override
            public void DataIsRead(List<?> items) { }

            @Override
            public void DataIsProcessed() {
                // Make toast and go back to delivery list
                Toast.makeText(DeliveryDetailsActivity.this, getString(R.string.delivery_claimed_from)
                        + delivery.getDonor().getCompanyName() + " "+getString(R.string.at)+" " + delivery.getDonation().getPickupTime()
                        + ". "+getString(R.string.see_you)+"!", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onError(String errorMessage) { }
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


    /**
     * Spinner override. Used to store the item selected by the spinner dropdown
     * @param parent - the parent AdapterView
     * @param view - reference to the Spinner list item
     * @param position - the position of the item in the list
     * @param id - the id of the item
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Grab pickup time from Spinner
        pickupTime = parent.getItemAtPosition(position).toString();
    }

    /**
     * Spinner override. Used if no item is selected.
     * @param parent
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

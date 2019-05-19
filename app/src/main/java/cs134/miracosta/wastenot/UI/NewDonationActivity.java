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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import cs134.miracosta.wastenot.Model.Donation;
import cs134.miracosta.wastenot.Model.Enums.FoodType;
import cs134.miracosta.wastenot.Model.FirebaseDBHelper;
import cs134.miracosta.wastenot.Model.User;
import cs134.miracosta.wastenot.R;

public class NewDonationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {

    public static final String TAG = "WasteNot";

    private FirebaseDBHelper db;
    private User user;
    // View elements
    private DrawerLayout drawer;
    private EditText otherInfoEditText;
    private Toolbar toolbar;
    private Button addDonationButton;
    private Spinner foodTypeSpinner;
    private Spinner servingsSpinner;
    private Spinner readyTimeSpinner;
    private Spinner fitInCarSpinner;
    private Spinner pickupEndTimeSpinner;

    private String foodType, servings, readyTime, pickupEndTime;
    private boolean fitInCar;

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
        otherInfoEditText = findViewById(R.id.otherInfoEditText);
        foodTypeSpinner = findViewById(R.id.foodTypeSpinner);
        servingsSpinner = findViewById(R.id.servingsSpinner);
        fitInCarSpinner = findViewById(R.id.fitInCarSpinner);
        readyTimeSpinner = findViewById(R.id.readyTimeSpinner);
        pickupEndTimeSpinner = findViewById(R.id.pickupEndTimeSpinner);
        setSupportActionBar(toolbar);

        // For each Spinner, set adapter and add listener
        setupSpinner(foodTypeSpinner, R.array.food_types_array);
        setupSpinner(servingsSpinner, R.array.servings_array);
        setupSpinner(fitInCarSpinner, R.array.fit_in_car_array);
        setupSpinner(readyTimeSpinner, R.array.ready_time_array);
        setupSpinner(pickupEndTimeSpinner, R.array.end_time_array);

        NavigationView navigationView = findViewById(R.id.new_donation_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Get user key from intent
        Intent intent = getIntent();
        user = intent.getParcelableExtra("User");
    }

    private void setupSpinner(Spinner spinner, int arrayId)
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, arrayId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
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
                finish();
                break;
            case R.id.nav_deliver:
                startActivity(new Intent(this, DeliveryActivity.class));
                finish();
                break;
            case R.id.nav_logout:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }

        finish();
        return true;
    }

    public void addDonation(View v)
    {
        // Data to be validated
        FoodType valType;
        int valServings;
        String valReady, valEnd, otherInfo;

        otherInfo = otherInfoEditText.getText().toString();
        // Get locale safe string array of FoodTypes
        String[] foodTypeArray = getResources().getStringArray(R.array.food_types_array);

        // Validate FoodType
        if (foodType.equals(foodTypeArray[0])) {
            // Default selection, send error message to user
            Toast.makeText(this, "Please select a type of food.", Toast.LENGTH_LONG).show();
            return;
        } else if (foodType.equals(foodTypeArray[1])) {
            valType = FoodType.DAIRY;
        } else if (foodType.equals(foodTypeArray[2])) {
            valType = FoodType.MEAT;
        } else if (foodType.equals(foodTypeArray[3])) {
            valType = FoodType.PRODUCE;
        } else if (foodType.equals(foodTypeArray[4])) {
            valType = FoodType.BAKED_GOODS;
        } else if (foodType.equals(foodTypeArray[5])) {
            valType = FoodType.PREPARED_PACKAGED;
        } else if (foodType.equals(foodTypeArray[6])) {
            valType = FoodType.PREPARED_TRAY;
        } else if (foodType.equals(foodTypeArray[7])) {
            valType = FoodType.OTHER;
        } else {
            Log.w(TAG, "How did we get here?");
            return;
        }

        // Validate servings
        if (servings.equals("100+"))
            valServings = 100;
        else
            valServings = Integer.parseInt(servings);

        // Validate ready and end times (remove "From" and "Before")
        valReady = readyTime.substring(5);
        valEnd = pickupEndTime.substring(7);

        Donation donation= new Donation(valType, valServings, fitInCar, otherInfo, valReady, valEnd);
        // TODO: add donation to DB
        db.addDonation(donation, user.getKey(), new FirebaseDBHelper.DataStatus() {
            @Override
            public void DataIsRead(List<?> items) { }

            @Override
            public void DataIsProcessed() {
                Toast.makeText(NewDonationActivity.this, "Thank you for your donation!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(NewDonationActivity.this, "Error adding donation to database.", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        String data = parent.getItemAtPosition(position).toString();

        switch (parent.getId())
        {
            case R.id.foodTypeSpinner:
                foodType = data;
                break;
            case R.id.servingsSpinner:
                servings = data;
                break;
            case R.id.readyTimeSpinner:
                readyTime = data;
                break;
            case R.id.fitInCarSpinner:
                fitInCar = position == 0;
                break;
            case R.id.pickupEndTimeSpinner:
                pickupEndTime = data;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}

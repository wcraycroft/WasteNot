package cs134.miracosta.wastenot.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import cs134.miracosta.wastenot.Model.Donation;
import cs134.miracosta.wastenot.Model.Enums.DonationStatus;
import cs134.miracosta.wastenot.Model.FirebaseDBHelper;
import cs134.miracosta.wastenot.Model.Enums.FoodType;
import cs134.miracosta.wastenot.Model.User;
import cs134.miracosta.wastenot.R;
import cs134.miracosta.wastenot.UI.Adapters.ClaimsAdapter;

public class ClaimsListActivity extends AppCompatActivity implements View.OnClickListener {

    //Reference to FirebaseDBHelper class
    private FirebaseDBHelper mDB;

    //References to all the views
    private ConstraintLayout loader;
    private RecyclerView rvClaims;
    public ClaimsAdapter adapter;

    //ArrayList to store all the donations
    private ArrayList<Donation> donations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claims_list);

        //Initialize FirebaseDBHelper class
        mDB = new FirebaseDBHelper();


        //Bind the views with their xml ids & all listeners
        loader = findViewById(R.id.loader);
        rvClaims = findViewById(R.id.rvClaims);
        findViewById(R.id.fabAddClaim).setOnClickListener(this);
        findViewById(R.id.btMyClaims).setOnClickListener(this);
        loader.setOnClickListener(this);

        //Create layoutManager & ClaimsAdapter classes and set both to recycleView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvClaims.setLayoutManager(linearLayoutManager);

        adapter = new ClaimsAdapter(this,donations, listener);
        rvClaims.setAdapter(adapter);


        getAllClaims();
    }

    /**
     * Get the donations list using 'getDonationsByStatus' helper method and
     * add them to list. If list is empty make empty view visible
     * otherwise, reload the list by calling 'getAllClaims'
     */
    private void getAllClaims() {

        loader.setVisibility(View.VISIBLE);
        mDB.getDonationsByStatus(DonationStatus.UNCLAIMED, new FirebaseDBHelper.DataStatus() {
            @Override
            public void DataIsRead(List<?> items)
            {
                loader.setVisibility(View.GONE);

                donations.clear();
                donations.addAll ((Collection<? extends Donation>) items);

                if (donations.size() > 0)
                {
                    //In case user add first one
                    rvClaims.setVisibility(View.VISIBLE);
                    findViewById(R.id.tvNoClaims).setVisibility(View.GONE);

                    adapter = new ClaimsAdapter(ClaimsListActivity.this,donations,listener);
                    rvClaims.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else
                {
                    //In case list is empty
                    rvClaims.setVisibility(View.GONE);
                    findViewById(R.id.tvNoClaims).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void DataIsProcessed()
            {
                loader.setVisibility(View.GONE);
            }

            @Override
            public void onError(String errorMessage)
            {
                loader.setVisibility(View.GONE);
                Toast.makeText(ClaimsListActivity.this, errorMessage,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    OnItemClickListener listener = new OnItemClickListener()
    {
        @Override
        public void onItemClick(View view, int position)
        {
            startActivityForResult(new Intent(ClaimsListActivity.this, ClaimDetailsActivity.class)
                    .putExtra("CLAIM_MODEL", donations.get(position)), 100);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabAddClaim:
                addNewClaim();
                break;

            case R.id.btMyClaims:
                startActivity(new Intent(this, MyClaimsActivity.class));
                break;
        }
    }

    /**
     * Get the user by email using 'getUserByEmail' helper method and
     * add new donation to Could Firestore using 'addDonation' &
     * reload the list by calling 'getAllClaims'
     */
    private void addNewClaim() {
        String email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

        loader.setVisibility(View.VISIBLE);
        mDB.getUserByEmail(email,new FirebaseDBHelper.DataStatus() {
            @Override
            public void DataIsRead(List<?> items)
            {
                User user = (User) items.get(0);

                Donation donation = new Donation(FoodType.BAKED_GOODS, 10, true,
                        getString(R.string.other_dummy_info)
                        , "22:00", "23:00");

                mDB.addDonation(donation,user.getKey(), new FirebaseDBHelper.DataStatus() {
                    @Override
                    public void DataIsRead(List<?> items)
                    {
                    }


                    @Override
                    public void DataIsProcessed()
                    {
                        Toast.makeText(ClaimsListActivity.this, getString(R.string.claim_added_success),
                                Toast.LENGTH_SHORT).show();
                        getAllClaims();
                    }

                    @Override
                    public void onError(String errorMessage)
                    {
                        loader.setVisibility(View.GONE);
                        Toast.makeText(ClaimsListActivity.this, errorMessage,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void DataIsProcessed()
            {
            }

            @Override
            public void onError(String errorMessage)
            {
                loader.setVisibility(View.GONE);
                Toast.makeText(ClaimsListActivity.this, errorMessage,
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * On 'RESULT_OK', reload the list by calling 'getAllClaims'
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            getAllClaims();
        }
    }

    /**
     * Close current activity & start 'DeliveryActivity'
     * @param view
     */
    public void goToDelivery(View view)
    {
        startActivity(new Intent(ClaimsListActivity.this,DeliveryActivity.class));
        finish();
    }
}

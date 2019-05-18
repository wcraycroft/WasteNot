package cs134.miracosta.wastenot.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

import cs134.miracosta.wastenot.Model.Donation;
import cs134.miracosta.wastenot.Model.FirebaseDBHelper;
import cs134.miracosta.wastenot.Model.User;
import cs134.miracosta.wastenot.R;

public class ClaimDetailsActivity extends AppCompatActivity {

    private ConstraintLayout loader;
    private FirebaseDBHelper mDB;

    private Donation donation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_details);

        mDB = new FirebaseDBHelper();

        loader = findViewById(R.id.loader);

        findViewById(R.id.btClaim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                claimClicked();
            }
        });

        getData();
    }

    /**
     * method to donation a donation
     */
    private void claimClicked() {

        loader.setVisibility(View.VISIBLE);

        String email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

        mDB.getUserByEmail(email, new FirebaseDBHelper.DataStatus() {
            @Override
            public void DataIsRead(List<?> items)
            {
                User user = (User) items.get(0);

                mDB.claimDonation(donation,user.getKey(), new FirebaseDBHelper.DataStatus() {
                    @Override
                    public void DataIsRead(List<?> items)
                    {
                    }

                    @Override
                    public void DataIsProcessed()
                    {
                        loader.setVisibility(View.GONE);
                        Toast.makeText(ClaimDetailsActivity.this, "Claimed Successfully",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onError(String errorMessage)
                    {
                        loader.setVisibility(View.GONE);
                        Toast.makeText(ClaimDetailsActivity.this, errorMessage,
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
                Toast.makeText(ClaimDetailsActivity.this, errorMessage,
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * method to get data from previous screen
     */
    private void getData() {
        if (getIntent() != null && getIntent().getSerializableExtra("CLAIM_MODEL") != null) {
            donation = (Donation) getIntent().getSerializableExtra("CLAIM_MODEL");
            setDataInViews();
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    /**
     * method to set data in views
     */
    private void setDataInViews() {
        TextView tvInfo = findViewById(R.id.tvInfo);
        String text =
                "Donor company name: " + FirebaseAuth.getInstance().getCurrentUser().getEmail() + "\n" +
                        "Fit In Car: " + donation.isFitInCar() + "\n" +
                        "Food Type: " + donation.getFoodType() + "\n" +
                        "Other Info: " + donation.getOtherInfo() + "\n" +
                        "Pick up end Time: " + donation.getPickupEndTime() + "\n" +
                        "Ready Time: " + donation.getReadyTime() + "\n" +
                        "Servings: " + donation.getServings() + "\n";

        tvInfo.setText(text);
    }
}

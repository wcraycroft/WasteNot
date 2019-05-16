package cs134.miracosta.wastenot.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import cs134.miracosta.wastenot.R;
import cs134.miracosta.wastenot.model.Claim;
import cs134.miracosta.wastenot.model.Claimer;

public class ClaimDetailsActivity extends AppCompatActivity {

    private DatabaseReference ref;
    private ConstraintLayout loader;
    private DatabaseReference mDatabase;

    private Claim claim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_details);

        loader = findViewById(R.id.loader);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ref = mDatabase.child("claims");

        findViewById(R.id.btClaim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                claimClicked();
            }
        });

        getData();
    }

    /**
     * method to claim a claim
     */
    private void claimClicked() {

        loader.setVisibility(View.VISIBLE);

        Claimer claimer = new Claimer();
        claimer.setCompanyName(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());

        claim.setClaimer(claimer);

        ref.child(claim.getNodeName()).setValue(claim).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ClaimDetailsActivity.this, "Claimed Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    /**
     * method to get data from previous screen
     */
    private void getData() {
        if (getIntent() != null && getIntent().getSerializableExtra("CLAIM_MODEL") != null) {
            claim = (Claim) getIntent().getSerializableExtra("CLAIM_MODEL");
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
                "Donor company name: " + claim.getDonor().getCompanyName() + "\n" +
                        "Drop Off End Time: " + claim.getDropOffEndTime() + "\n" +
                        "Fit In Car: " + claim.isFitInCar() + "\n" +
                        "Food Type: " + claim.getFoodType() + "\n" +
                        "Other Info: " + claim.getOtherInfo() + "\n" +
                        "Pick up end Time: " + claim.getPickupEndTime() + "\n" +
                        "Ready Time: " + claim.getReadyTime() + "\n" +
                        "Servings: " + claim.getServings() + "\n";

        tvInfo.setText(text);
    }
}

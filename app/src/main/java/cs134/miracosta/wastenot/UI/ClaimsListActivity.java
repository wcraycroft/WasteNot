package cs134.miracosta.wastenot.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import cs134.miracosta.wastenot.R;
import cs134.miracosta.wastenot.model.Claim;
import cs134.miracosta.wastenot.model.Donor;
import cs134.miracosta.wastenot.model.FoodType;
import cs134.miracosta.wastenot.ui.adapters.ClaimsAdapter;

public class ClaimsListActivity extends AppCompatActivity implements View.OnClickListener, OnItemClickListener {

    private DatabaseReference ref;
    private ConstraintLayout loader;
    private DatabaseReference mDatabase;

    private RecyclerView rvClaims;
    private ClaimsAdapter adapter;
    private ArrayList<Claim> allClaims = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claims_list);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        ref = mDatabase.child("claims");

        loader = findViewById(R.id.loader);
        rvClaims = findViewById(R.id.rvClaims);
        findViewById(R.id.fabAddClaim).setOnClickListener(this);
        findViewById(R.id.btMyClaims).setOnClickListener(this);
        loader.setOnClickListener(this);

        adapter = new ClaimsAdapter(this, allClaims, this);
        rvClaims.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvClaims.setLayoutManager(linearLayoutManager);

        getAllClaims();
    }

    /**
     * method to get all claims
     */
    private void getAllClaims() {
        loader.setVisibility(View.VISIBLE);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allClaims.clear();
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Claim claim = dataSnapshot1.getValue(Claim.class);
                        if (claim != null && claim.getClaimer() == null)
                            allClaims.add(claim);
                    }
                    if (allClaims.size() > 0)
                        adapter.notifyDataSetChanged();

                    loader.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loader.setVisibility(View.GONE);
                Toast.makeText(ClaimsListActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

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
     * method to add a new claim
     */
    private void addNewClaim() {
        String email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

        Donor donor = new Donor(email, new ArrayList<Claim>());
        Claim newClaim = new Claim(donor, FoodType.BAKED_GOODS, 10, true, "Other dummy info"
                , "10", "22:00", "23:00");

        loader.setVisibility(View.VISIBLE);

        DatabaseReference pushed = ref.push();
        String key = pushed.getKey();
        newClaim.setNodeName(key);
        pushed.setValue(newClaim)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loader.setVisibility(View.GONE);
                        Toast.makeText(ClaimsListActivity.this, "Claim Added successfully",
                                Toast.LENGTH_SHORT).show();
                        getAllClaims();
                    }
                });

    }

    @Override
    public void onItemClick(View view, int position) {
        startActivityForResult(new Intent(ClaimsListActivity.this, ClaimDetailsActivity.class)
                .putExtra("CLAIM_MODEL", allClaims.get(position)), 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            getAllClaims();
        }
    }
}

package cs134.miracosta.wastenot.UI;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

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
import cs134.miracosta.wastenot.ui.adapters.ClaimsAdapter;

public class MyClaimsActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference ref;
    private ConstraintLayout loader;
    private DatabaseReference mDatabase;

    private RecyclerView rvClaims;
    private ClaimsAdapter adapter;
    private ArrayList<Claim> allClaims = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_claims);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        ref = mDatabase.child("claims");

        loader = findViewById(R.id.loader);
        rvClaims = findViewById(R.id.rvClaims);
        loader.setOnClickListener(this);

        adapter = new ClaimsAdapter(this, allClaims, null);
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
                        if (claim != null && claim.getClaimer() != null && claim.getClaimer().getCompanyName()
                                .equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()))
                            allClaims.add(claim);
                    }
                    if (allClaims.size() > 0)
                        adapter.notifyDataSetChanged();
                    else findViewById(R.id.tvNoClaims).setVisibility(View.VISIBLE);
                    loader.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loader.setVisibility(View.GONE);
                Toast.makeText(MyClaimsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}

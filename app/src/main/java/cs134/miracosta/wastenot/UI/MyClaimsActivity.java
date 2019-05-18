package cs134.miracosta.wastenot.UI;

import android.os.Bundle;
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
import cs134.miracosta.wastenot.Model.FirebaseDBHelper;
import cs134.miracosta.wastenot.Model.User;
import cs134.miracosta.wastenot.R;
import cs134.miracosta.wastenot.UI.Adapters.ClaimsAdapter;

public class MyClaimsActivity extends AppCompatActivity implements View.OnClickListener {

    private ConstraintLayout loader;
    private FirebaseDBHelper mDB;

    private RecyclerView rvClaims;
    private ClaimsAdapter adapter;
    private ArrayList<Donation> donations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_claims);

        mDB = new FirebaseDBHelper();

        loader = findViewById(R.id.loader);
        rvClaims = findViewById(R.id.rvClaims);
        loader.setOnClickListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvClaims.setLayoutManager(linearLayoutManager);

        adapter = new ClaimsAdapter(this,donations, null);
        rvClaims.setAdapter(adapter);



        getAllClaims();
    }

    /**
     * method to get all claims
     */
    private void getAllClaims() {
        loader.setVisibility(View.VISIBLE);

        String email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

        mDB.getUserByEmail(email,new FirebaseDBHelper.DataStatus() {
            @Override
            public void DataIsRead(List<?> items)
            {
                User user = (User) items.get(0);

                mDB.getDonationsByUser(user.getKey(), new FirebaseDBHelper.DataStatus() {
                    @Override
                    public void DataIsProcessed()
                    {
                        loader.setVisibility(View.GONE);
                    }

                    @Override
                    public void DataIsRead(List<?> items) {

                        loader.setVisibility(View.GONE);

                        donations.clear();

                        donations.addAll ((Collection<? extends Donation>) items);

                        if (donations.size() > 0)
                        {
                            adapter = new ClaimsAdapter(MyClaimsActivity.this,donations, null);
                            rvClaims.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                        else findViewById(R.id.tvNoClaims).setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(String errorMessage)
                    {
                        loader.setVisibility(View.GONE);
                        Toast.makeText(MyClaimsActivity.this, errorMessage,
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
                Toast.makeText(MyClaimsActivity.this, errorMessage,
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {

    }
}

package cs134.miracosta.wastenot.UI;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import cs134.miracosta.wastenot.Model.Donation;
import cs134.miracosta.wastenot.Model.FirebaseDBHelper;
import cs134.miracosta.wastenot.Model.User;
import cs134.miracosta.wastenot.R;

public class ClaimDetailsActivity extends AppCompatActivity {

    //Reference to FirebaseDBHelper class
    private FirebaseDBHelper mDB;

    //References to all the views
    private ConstraintLayout loader;

    //Reference to Donation object being passed through intent
    private Donation donation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_details);

        //Initialize FirebaseDBHelper class
        mDB = new FirebaseDBHelper();


        //Bind the views with their xml ids & all listeners
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
     * Call the 'claimDonation' method after selecting the dropOff endTime
     */
    private void claimClicked() {


        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(ClaimDetailsActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                donation.setDropoffEndTime( selectedHour + ":" + selectedMinute);

                claimDonation();
            }
        }, hour, minute, true);//YES 24 hour time
        mTimePicker.setTitle(getString(R.string.select_drop_off_time));
        mTimePicker.show();

    }

    /**
     * Get the user by email using 'getUserByEmail' helper method and
     * mark the donation as claimed in Could Firestore using 'claimDonation' &
     * finish the activity by setting the result 'RESULT_OK'
     */
    private void claimDonation()
    {
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
                        Toast.makeText(ClaimDetailsActivity.this, getString(R.string.claimed_successfully),
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
        if (getIntent() != null && getIntent().getParcelableExtra("CLAIM_MODEL") != null) {
            donation = getIntent().getParcelableExtra("CLAIM_MODEL");
            setDataInViews();
        } else {
            Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    /**
     * method to set data in views
     */
    private void setDataInViews() {
        TextView tvInfo = findViewById(R.id.tvInfo);

        String text =   getString(R.string.fit_in_car) + " " + donation.isFitInCar() + "\n" +
                getString(R.string.food_type) + " " + donation.getFoodType() + "\n" +
                getString(R.string.other_infos) + " "+ donation.getOtherInfo() + "\n" +
                getString(R.string.pickup_end_time_) + " " + donation.getPickupEndTime() + "\n" +
                getString(R.string.ready_time) + " " + donation.getReadyTime() + "\n" +
                getString(R.string.servings) + " " + donation.getServings() + "\n";

        tvInfo.setText(text);
    }
}

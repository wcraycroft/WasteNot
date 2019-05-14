package cs134.miracosta.wastenot.UI;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cs134.miracosta.wastenot.Model.Claimer;
import cs134.miracosta.wastenot.Model.FirebaseDBHelper;
import cs134.miracosta.wastenot.Model.Location;
import cs134.miracosta.wastenot.Model.User;
import cs134.miracosta.wastenot.R;

public class TestActivity extends AppCompatActivity {

    public static final String TAG = "WasteNot";

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    DatabaseReference locationDB;
    FirebaseDBHelper mDB;

    Button testButton;
    TextView outputTextView;
    TextView outputTextView2;
    EditText inputEditText;

    private User outputUser;
    private User testUser;
    private List<String> allUserKeys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        outputTextView = findViewById(R.id.outputTextView);
        outputTextView2 = findViewById(R.id.outputTextView2);
        inputEditText = findViewById(R.id.inputEditText);
        testButton = findViewById(R.id.testButton);

        mDB = new FirebaseDBHelper();
    }

    public void testDatabase(View v) {
        String text = inputEditText.getText().toString();


        Location testLoc = new Location("Test name 1", "123 Fake St", "Long Island",
                "NY", "51611", "85858858", 100.0, 100.0);

        testUser = new Claimer("key", "claimer", "Will", testLoc);

        mDB.addUser(testUser, new FirebaseDBHelper.DataStatus() {
            @Override
            public void DataIsProcessed() {
                Toast.makeText(TestActivity.this, "Claimer added successfully.", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Inside DataIsProcessed: " + testUser);
                mDB.getUser(testUser.getKey(), new FirebaseDBHelper.DataStatus() {
                    @Override
                    public void DataIsRead(List<?> items) {
                        outputUser = (Claimer) items.get(0);
                        if (outputUser == null) {
                            outputTextView.setText("Null object");
                        } else {
                            outputTextView.setText(outputUser.toString());
                        }
                    }

                    @Override
                    public void DataIsProcessed() {
                    }
                });
            }

            @Override
            public void DataIsRead(List<?> items) {
            }
        });

        /*allUserKeys.clear();
        mDB.getAllUserKeys(new FirebaseDBHelper.DataStatus() {
            @Override
            public void DataIsRead(List<?> items) {
                allUserKeys = (List<String>) items;
                mDB.deleteAllUsers(allUserKeys);
            }

            @Override
            public void DataIsProcessed() { }
        });
        */

    }





        /*String key = locationDB.push().getKey();
        //testLoc.setKey(key);
        //locationDB.child(key).setValue(testLoc);


        Location testLoc2 = new Location("Test name 2", "123 Fake St", "Long Island",
                "NY", "51611", "85858858", 100.0, 100.0);

        String key2 = locationDB.push().getKey();
        //testLoc.setKey(key);
        locationDB.child(key2).setValue(testLoc2);

        locationDB.child(key2).child("address").setValue("222 Fake");


        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String data = dataSnapshot.getValue(String.class);
                outputTextView.setText(data);

                //Location outputLoc = dataSnapshot.getChildren();

                //outputTextView2.setText(outputLoc.getAddress());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        */

}

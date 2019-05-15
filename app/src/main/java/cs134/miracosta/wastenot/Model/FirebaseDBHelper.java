// TODO: WIP just using as template
// Firebase code

// Write to DB
// FirebaseDatabase database = FirebaseDatabase.getInstance();
// DatabaseReference myRef = database.getReference("TestMessage");
// myRef.setValue("Hello, World!");

// Read from the database using event listener
//myRef.addValueEventListener(new ValueEventListener() {
//    @Override
//    public void onDataChange(DataSnapshot dataSnapshot) {
//        // This method is called once with the initial value and again
//        // whenever data at this location is updated.
//        String value = dataSnapshot.getValue(String.class);
//        Log.d(TAG, "Value is: " + value);
//    }
//
//    @Override
//    public void onCancelled(DatabaseError error) {
//        // Failed to read value
//        Log.w(TAG, "Failed to read value.", error.toException());
//    }
//});

package cs134.miracosta.wastenot.Model;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDBHelper {

    public static final String TAG = "WasteNot";

    // Collection Paths
    public static final String USER_COLLECTION = "USERS";
    public static final String DONATION_COLLECTION = "DONATIONS";
    public static final String MAKES_COLLECTION = "MAKES";

    // Database references
    private CollectionReference mUserDB;
    private CollectionReference mDonationDB;
    private CollectionReference mMakesDB;

    // Public interface used to link asynchronous listeners to calling classes
    public interface DataStatus {
        void DataIsRead(List<?> items);
        void DataIsProcessed();
    }

    // Other variables
    private List<User> allUsersList = new ArrayList<>();
    private List<Donation> allDonationsList = new ArrayList<>();
    private List<String> allKeysList = new ArrayList<>();
    private User focusedUser;
    private Donation focusedDonation;
    private String focusedKey;

    // Constructor
    public FirebaseDBHelper()
    {
        // Instantiate references to each collection
        mUserDB = FirebaseFirestore.getInstance().collection(USER_COLLECTION);
        mDonationDB = FirebaseFirestore.getInstance().collection(DONATION_COLLECTION);
    }


    /***************************************************
     * FIRESTORE USER DATABASE METHODS
     ***************************************************/


    public void addUser(User newUser, final DataStatus dataStatus)
    {
        // Store reference to user to set key
        focusedUser = newUser;
        // Add user to DB
        mUserDB.add(newUser).addOnCompleteListener(new OnCompleteListener<DocumentReference>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task)
            {
                if (task.isSuccessful()) {
                    Log.i(TAG, "Document was added successfully. Key = " + task.getResult().getId());
                    // Set the new user's key (note it will still be the old value in DB)
                    String key = task.getResult().getId();
                    focusedUser.setKey(key);
                    // Set the key field of the same user
                    mUserDB.document(key).set(focusedUser);

                    dataStatus.DataIsProcessed();
                }
                else {
                    Log.w(TAG, "Error adding user.", task.getException());
                }
            }
        });
    }

    public void updateUser(User updatedUser, final DataStatus dataStatus)
    {
        mUserDB.document(updatedUser.getKey()).set(updatedUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful()) {
                    Log.i(TAG, "Document was updated successfully.");
                    dataStatus.DataIsProcessed();
                }
                else {
                    Log.w(TAG, "Error updating user.", task.getException());
                }
            }
        });
    }

    public void deleteUser(String deletedKey, final DataStatus dataStatus)
    {
        mUserDB.document(deletedKey).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful()) {
                    Log.i(TAG, "Document was deleted successfully.");
                    dataStatus.DataIsProcessed();
                }
                else {
                    Log.w(TAG, "Error deleting user.", task.getException());
                }
            }
        });
    }

    public void getUser(String key, final DataStatus dataStatus)
    {
        // Store key to be assigned later
        focusedKey = key;
        // Get user from DB
        mUserDB.document(key).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                if (documentSnapshot.exists())
                {
                    // Which type of user determines what data Firestore will attempt to pull
                    focusedUser = documentSnapshot.toObject((User.class));
                    //TODO: debug
                    Log.i(TAG, "Converted object: " + focusedUser.toString());
                    // Before returning the User, assign it the generated key
                    focusedUser.setKey(focusedKey);
                    // Send the Data via interface
                    List<Object> items = new ArrayList<>();
                    items.add(focusedUser);
                    // Send data through interface
                    dataStatus.DataIsRead(items);
                }
            } // end of onSuccess
        });
    }

    public void getAllUsers(final DataStatus dataStatus)
    {
        allUsersList.clear();
        mUserDB.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        allUsersList.add(document.toObject((User.class)));
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void getAllUserKeys(final DataStatus dataStatus)
    {
        // Clear allKeysList list
        allKeysList.clear();
        // Get all allKeysList in collection
        mUserDB.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        allKeysList.add(document.getId());
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
                dataStatus.DataIsRead(allKeysList);
            }
        });
    }

    public void deleteAllUsers(List<String> userKeys)
    {
        Log.d(TAG, "Attempting to delete " + userKeys.size() + " documents from User collection.");
        // Loop through all key values
        for (String key : userKeys)
        {
            // Delete document
            mUserDB.document(key).delete();
        }
        // If used for test purposes only, no DataStatus needed.
    }

    /***************************************************
     * FIRESTORE DONATION DATABASE METHODS
     ***************************************************/


    public void addDonation(Donation newDonation, final DataStatus dataStatus)
    {
        // Store reference to Donation to set key
        focusedDonation = newDonation;
        // Add user to DB
        mDonationDB.add(newDonation).addOnCompleteListener(new OnCompleteListener<DocumentReference>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task)
            {
                if (task.isSuccessful()) {
                    Log.i(TAG, "Document was added successfully. Key = " + task.getResult().getId());
                    // Set the new user's key (note it will still be the old value in DB)
                    focusedDonation.setKey(task.getResult().getId());
                    dataStatus.DataIsProcessed();
                }
                else {
                    Log.w(TAG, "Error adding Donation.", task.getException());
                }
            }
        });
    }

    public void updateDonation(User updatedDonation, final DataStatus dataStatus)
    {
        mDonationDB.document(updatedDonation.getKey()).set(updatedDonation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful()) {
                    Log.i(TAG, "Document was updated successfully.");
                    dataStatus.DataIsProcessed();
                }
                else {
                    Log.w(TAG, "Error updating Donation.", task.getException());
                }
            }
        });
    }

    public void deleteDonation(String deletedKey, final DataStatus dataStatus)
    {
        mDonationDB.document(deletedKey).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful()) {
                    Log.i(TAG, "Document was deleted successfully.");
                    dataStatus.DataIsProcessed();
                }
                else {
                    Log.w(TAG, "Error deleting Donation.", task.getException());
                }
            }
        });
    }

    public void getDonation(String key, final DataStatus dataStatus)
    {
        // Store key to be assigned later
        focusedKey = key;
        // Get user from DB
        mDonationDB.document(key).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                if (documentSnapshot.exists())
                {
                    //TODO: debug
                    Log.d(TAG, "Found type: " + documentSnapshot.toObject(Donation.class).getStatus());
                    // Which type of user determines what data Firestore will attempt to pull
                    focusedDonation = documentSnapshot.toObject((Donation.class));
                    //TODO: debug
                    Log.d(TAG, "Converted object: " + focusedDonation.toString());
                    // Before returning the User, assign it the generated key
                    focusedDonation.setKey(focusedKey);
                    // Send the Data via interface
                    List<Donation> items = new ArrayList<>();
                    items.add(focusedDonation);
                    dataStatus.DataIsRead(items);
                }
            } // end of onSuccess
        });
    }

    public void getAllDonations(final DataStatus dataStatus)
    {
        allDonationsList.clear();
        mDonationDB.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Loop through all documents in collection
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        allDonationsList.add(document.toObject((Donation.class)));
                    }
                } else {
                    Log.w(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void getAllDonationKeys(final DataStatus dataStatus)
    {
        // Clear allKeysList list
        allKeysList.clear();
        // Get all allKeysList in collection
        mDonationDB.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        allKeysList.add(document.getId());
                    }
                } else {
                    Log.w(TAG, "Error getting documents: ", task.getException());
                }
                dataStatus.DataIsRead(allKeysList);
            }
        });
    }

    public void deleteAllDonations(List<String> donationKeys)
    {
        Log.d(TAG, "Attempting to delete " + donationKeys.size() + " documents from Donations collection.");
        // Loop through all key values
        for (String key : donationKeys)
        {
            // Delete document
            mDonationDB.document(key).delete();
        }
        // If used for non-test purposes, add DataStatus call
    }


}  // end of class

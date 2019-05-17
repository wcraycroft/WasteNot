// TODO: Add Makes Collection which will handle all interactions between Users and Donations

/**
 *
 *
 *
 *
 */

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
    private List<Donation> donationsList = new ArrayList<>();
    private List<String> allKeysList = new ArrayList<>();
    private List<String> donationKeysList = new ArrayList<>();
    private User focusedUser;
    private Donation focusedDonation;
    private Makes focusedMakes;
    private String focusedKey;

    // Constructor
    public FirebaseDBHelper()
    {
        // Instantiate references to each collection
        mUserDB = FirebaseFirestore.getInstance().collection(USER_COLLECTION);
        mDonationDB = FirebaseFirestore.getInstance().collection(DONATION_COLLECTION);
        mMakesDB = FirebaseFirestore.getInstance().collection(MAKES_COLLECTION);
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

    public void getUserByKey(final String key, final DataStatus dataStatus)
    {
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
                    // Send the Data via interface
                    List<User> items = new ArrayList<>();
                    items.add(focusedUser);
                    // Send data through interface
                    dataStatus.DataIsRead(items);
                    Log.i(TAG, "Document was retrieved successfully by key.");
                }
            } // end of onSuccess
        });
    }

    public void getUserByEmail(final String email, final DataStatus dataStatus)
    {
        mUserDB.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        focusedUser = document.toObject((User.class));
                        if (focusedUser.getEmail().equals(email)) {
                            List<User> items = new ArrayList<>();
                            items.add(focusedUser);
                            // Send the Data via interface
                            dataStatus.DataIsRead(items);
                            Log.i(TAG, "Document was retrieved successfully by email.");
                            return;
                        }
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
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

    // TODO: update key in DB
    // Adds a new Donation to the Donation Collection and calls method to create a new Makes relationship
    public void addDonation(final Donation newDonation, final String userKey, final DataStatus dataStatus)
    {
        // Add new Donation to DB
        mDonationDB.add(newDonation).addOnCompleteListener(new OnCompleteListener<DocumentReference>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task)
            {
                if (task.isSuccessful()) {
                    String donationKey = task.getResult().getId();
                    Log.i(TAG, "Donation was added successfully. Key = " + donationKey);
                    // Set the new user's key (note it will still be the old value in DB)
                    newDonation.setKey(donationKey);
                    // Set new ket in database
                    mDonationDB.document(donationKey).set(newDonation);
                    // Create new Makes relation
                    addNewMakes(donationKey, userKey, dataStatus);
                    // This now gets called in addNewMakes()
                    //dataStatus.DataIsProcessed();
                }
                else {
                    Log.w(TAG, "Error adding Donation.", task.getException());
                }
            }
        });
    }

    // Helper method that adds a new Makes relationship between the passed Donation and User keys
    private void addNewMakes(String donationKey, String userKey, final DataStatus dataStatus)
    {
        // Instantiate Makes
        final Makes newMakes = new Makes(donationKey, userKey);
        mMakesDB.add(newMakes).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    String makesKey = task.getResult().getId();
                    Log.i(TAG, "Makes was added successfully. Key = " + makesKey);
                    // Set the new Make's key
                    newMakes.setKey(makesKey);
                    // Update database with new key value
                    mMakesDB.document(makesKey).set(newMakes);
                    // Task complete
                    dataStatus.DataIsProcessed();
                }
                else {
                    Log.w(TAG, "Error adding Donation.", task.getException());
                }
            }
        });
    }

    /**
     * Call this method when a user claims a donation. It will update the appropriate info in database.
     * This will also update the Donation object in the Donations collection, so if you make any changes
     * like adding a pickup/dropoff time, you do not need to call another update() in addition to this method.
     */
    public void claimDonation(final Donation claimedDonation, final String claimerKey, final DataStatus dataStatus)
    {
        mMakesDB.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        focusedMakes = document.toObject((Makes.class));
                        // Check if the keys match
                        if (focusedMakes.getDonationKey().equals(claimedDonation.getKey())) {
                            // Set the new claimer key and update DB
                            focusedMakes.setClaimerKey(claimerKey);
                            mMakesDB.document(focusedMakes.getKey()).set(focusedMakes);
                            // Set the donation status to claimed and update DB (in case we forgot)
                            claimedDonation.setStatus(DonationStatus.DONATION_CLAIMED);
                            mDonationDB.document(focusedDonation.getKey()).set(focusedDonation);
                            // Task complete
                            dataStatus.DataIsProcessed();
                            Log.i(TAG, "Donation claim was updated in DB.");
                            return;
                        }
                    }
                } else {
                    Log.d(TAG, "Error updating donation to claim: ", task.getException());
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
                    Log.i(TAG, "Donation was updated successfully.");
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
                    focusedDonation = documentSnapshot.toObject((Donation.class));
                    // TODO: remove key setter
                    // Before returning the Donation, assign it the generated key
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
        donationsList.clear();
        mDonationDB.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Loop through all documents in collection
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        donationsList.add(document.toObject((Donation.class)));
                    }
                } else {
                    Log.w(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }


    /**
     *  This method returns (via interface) a list of all donations linked to a User (in any way)
     */
    public void getDonationsByUser(String userKey, final DataStatus dataStatus)
    {
        donationsList.clear();
        // Get list of Donation keys that have not been claimed from Makes db
        getDonationKeysByUser(userKey, new DataStatus() {
            @Override
            public void DataIsRead(List<?> items) { }   // not used
            @Override
            public void DataIsProcessed() {
                // Keys list is updated and we are ready to party
                mDonationDB.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Loop through all Donations
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // If keys match, parse Donation and add it to list
                                if (donationKeysList.contains(document.getId()))
                                {
                                    donationsList.add(document.toObject((Donation.class)));
                                }
                            }
                            // Task complete, send data via interface
                            dataStatus.DataIsRead(donationsList);
                            Log.i(TAG, "User Donation List successfully fetched from Donation DB.");
                        } else {
                            Log.w(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
            }
        });

    }

    /** Helper method which returns various sets of Donation keys from Makes DB based on the status of the Donation
     *  Status types: UNCLAIMED, DONATION_CLAIMED, DELIVERY_CLAIMED
     */
    private void getDonationKeysByUser(final String userKey, final DataStatus dataStatus)
    {
        donationKeysList.clear();
        mMakesDB.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        focusedMakes = document.toObject((Makes.class));
                        // Check if the user key match any of the Makes user keys
                        if (userKey.equals(focusedMakes.getClaimerKey()) ||
                                userKey.equals(focusedMakes.getDonorKey()) ||
                                userKey.equals(focusedMakes.getDriverKey()))
                        {
                            donationKeysList.add(focusedMakes.getDonationKey());
                        }

                    } // end of document for loop

                    // Task complete, no need to send data since we will be staying in this class
                    // and have access to the keys list
                    dataStatus.DataIsProcessed();
                    Log.i(TAG, "Donation keys were fetched successfully from Makes DB.");

                } else {
                    Log.d(TAG, "Error fetching donation keys from Makes collection: ", task.getException());
                }
            }
        });
    }


    /**
     *  This method returns (via interface) a list of all donations with the passed status
     *  Status types: UNCLAIMED, DONATION_CLAIMED, DELIVERY_CLAIMED
     */
    public void getDonationsByStatus(DonationStatus status, final DataStatus dataStatus)
    {
        donationsList.clear();
        // Get list of Donation keys that have not been claimed from Makes db
        getDonationKeysByStatus(status, new DataStatus() {
            @Override
            public void DataIsRead(List<?> items) { }   // not used
            @Override
            public void DataIsProcessed() {
                // Keys list is updated and we are ready to party
                mDonationDB.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Loop through all Donations
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // If keys match, parse Donation and add it to list
                                if (donationKeysList.contains(document.getId()))
                                {
                                    donationsList.add(document.toObject((Donation.class)));
                                }
                            }
                            // Task complete, send data via interface
                            dataStatus.DataIsRead(donationsList);
                            Log.i(TAG, "Status Donation List successfully fetched from Donation DB.");
                        } else {
                            Log.w(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
            }
        });

    }

    /**
     * Helper method which returns various sets of Donation keys from Makes DB based on the status of the Donation
     *  Status types: UNCLAIMED, DONATION_CLAIMED, DELIVERY_CLAIMED
     */
    private void getDonationKeysByStatus(final DonationStatus status, final DataStatus dataStatus)
    {
        donationKeysList.clear();
        mMakesDB.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        focusedMakes = document.toObject((Makes.class));
                        // Check if the keys match the requested status
                        switch (status) {
                            // Unclaimed donation: no claimer (or driver) key
                            case UNCLAIMED:
                                if (focusedMakes.getClaimerKey().equals("")) {
                                    donationKeysList.add(focusedMakes.getDonationKey());
                                }
                                break;
                            // Claimed donation: claimer key, no driver key
                            case DONATION_CLAIMED:
                                if (!focusedMakes.getClaimerKey().equals("")
                                && focusedMakes.getDriverKey().equals("")) {
                                    donationKeysList.add(focusedMakes.getDonationKey());
                                }
                                break;
                            // Delivery Claimed donation: claimer and driver key
                            case DELIVERY_CLAIMED:
                                if (!focusedMakes.getClaimerKey().equals("")
                                        && !focusedMakes.getDriverKey().equals("")) {
                                    donationKeysList.add(focusedMakes.getDonationKey());
                                }
                                break;
                        } // end of status switch statement
                    } // end of document for loop

                    // Task complete, no need to send data since we will be staying in this class
                    // and have access to the keys list
                    dataStatus.DataIsProcessed();
                    Log.i(TAG, "Donation keys were fetched successfully from Makes DB.");

                } else {
                    Log.d(TAG, "Error fetching donation keys from Makes collection: ", task.getException());
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

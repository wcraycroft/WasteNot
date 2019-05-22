
package cs134.miracosta.wastenot.Model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import cs134.miracosta.wastenot.Model.Enums.DonationStatus;

/**
 * The <code>FirebaseDBHelper</code> class handles all calls to the Cloud Firestore database linked
 * to this project. The database consists of a Donation collection, a User collection and a Makes
 * collection, which represents all active links between Users and Donations.
 *
 * @author Will Craycroft
 * @author Ahmad Abbasi
 */

public class FirebaseDBHelper {

    public static final String TAG = "WasteNot";

    // Collection Paths
    private static final String USER_COLLECTION = "USERS";
    private static final String DONATION_COLLECTION = "DONATIONS";
    private static final String MAKES_COLLECTION = "MAKES";

    // Database references
    private CollectionReference mUserDB;
    private CollectionReference mDonationDB;
    private CollectionReference mMakesDB;


    // Lists
    private List<Donation> donationsList = new ArrayList<>();    // TODO: convert to final local variable
    private List<String> donationKeysList = new ArrayList<>();

    private User focusedUser;
    private Donation focusedDonation;
    private Makes focusedMakes;
    private String focusedKey;

    /**
     * This interface handles all asynchronous calls coming from the database.
     *  - DataIsRead(): All data will be returned through this method.
     *  - DataIsProcessed(): Called if no data needs to be returned.
     *  - onError(): Called if an error is encountered and the database query could not be completed.
     */
    public interface DataStatus {
        void DataIsRead(List<?> items);
        void DataIsProcessed();
        void onError(String errorMessage);
    }

    /**
     * Creates a new <code>FirebaseDBHelper</code> instance and instantiate a reference to each
     * collection.
     */
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

    /**
     * Adds the passed User to the database, then updates their key field with the generated key.
     * @param newUser - the User to be added
     * @param dataStatus - DataStatus interface instance used to handle asynchronous calls.
     */
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
                else
                {
                    dataStatus.onError(task.getException().getMessage());
                    Log.w(TAG, "Error adding user.", task.getException());
                }
            }
        });
    }

    /**
     * Returns the User with the passed db key, via the DataStatus interface
     * @param key - the User's database key
     * @param dataStatus - DataStatus interface instance used to handle asynchronous calls.
     */
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

    /**
     * Returns the User with the passed email, via the DataStatus interface
     * @param email - the User's unique email
     * @param dataStatus - DataStatus interface instance used to handle asynchronous calls.
     */
    public void getUserByEmail(String email, final DataStatus dataStatus)
    {
        mUserDB.whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                List<User> items = new ArrayList<>();
                                items.add(document.toObject((User.class)));

                                // Send the Data via interface
                                dataStatus.DataIsRead(items);

                                Log.i(TAG, "User Document was retrieved successfully by email.");
                                return;
                            }
                        } else {
                            dataStatus.onError(task.getException().getMessage());
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }


    /***************************************************
     * FIRESTORE DONATION DATABASE METHODS
     ***************************************************/

    /**
     * Adds this Donation to the Donation Collection and calls method to create a new Makes relationship
     * @param newDonation - the Donation to be added
     * @param userKey - the Donor user key
     * @param dataStatus - DataStatus interface instance used to handle asynchronous calls.
     */
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
                    dataStatus.onError(task.getException().getMessage());
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
                    dataStatus.onError(task.getException().getMessage());
                    Log.w(TAG, "Error adding Donation.", task.getException());
                }
            }
        });
    }

    /**
     * This method is called when a Claimer claims a donation. It will update the Makes keys, as
     * well as the Donation object in the Donations collection.
     * @param claimedDonation - the Donation that has been claimed by a Claimer
     * @param claimerKey - the Claimer's user key
     * @param dataStatus - DataStatus interface instance used to handle asynchronous calls.
     */
    public void claimDonation(final Donation claimedDonation, final String claimerKey, final DataStatus dataStatus)
    {
        mMakesDB.whereEqualTo("donationKey", claimedDonation.getKey())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                focusedMakes = document.toObject((Makes.class));
                                // Set the new claimer key and update DB
                                focusedMakes.setClaimerKey(claimerKey);
                                mMakesDB.document(focusedMakes.getKey()).set(focusedMakes);
                                // Set the donation status to claimed and update DB (in case we forgot)
                                claimedDonation.setStatus(DonationStatus.DONATION_CLAIMED);
                                mDonationDB.document(claimedDonation.getKey()).set(claimedDonation);
                                // Task complete
                                dataStatus.DataIsProcessed();
                                Log.i(TAG, "Donation claim was updated in DB.");
                                return;
                            }
                        } else {
                            Log.d(TAG, "Error updating donation to claim: ", task.getException());
                        }
                    }
                });
    }

    /**
     * This method is called when a Driver claims a Delivery. It will update the Makes keys, as well
     * as the Donation object in the Donations collection
     * @param driverEmail - the email address associated to the Driver user's account
     * @param claimedDelivery - the Delivery to be updated
     * @param dataStatus - DataStatus interface instance used to handle asynchronous calls.
     */
    public void claimDelivery(String driverEmail, final Delivery claimedDelivery, final DataStatus dataStatus)
    {
        // get Driver's key
        getUserByEmail(driverEmail, new DataStatus() {
            @Override
            public void DataIsRead(List<?> items) {
                User user = (User) items.get(0);
                final String driverKey = user.getKey();
                // Get
                mMakesDB.whereEqualTo("donationKey", claimedDelivery.getDonation().getKey())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Makes makes = document.toObject((Makes.class));
                                        Donation claimedDonation = claimedDelivery.getDonation();
                                        // Set the new claimer key and update DB
                                        makes.setDriverKey(driverKey);
                                        mMakesDB.document(makes.getKey()).set(makes);
                                        // Set the donation status to delivery claimed and update DB (in case we forgot)
                                        claimedDonation.setStatus(DonationStatus.DELIVERY_CLAIMED);
                                        mDonationDB.document(claimedDonation.getKey()).set(claimedDonation);
                                        // Task complete
                                        dataStatus.DataIsProcessed();
                                        Log.i(TAG, "Delivery claim was updated in DB.");
                                        return;
                                    }
                                } else {
                                    Log.d(TAG, "Error updating delivery to claim: ", task.getException());
                                }
                            }
                        });
            }

            @Override
            public void DataIsProcessed() {

            }

            @Override
            public void onError(String errorMessage) {

            }
        });

    }


    /**
     * Returns the Donation object (via DataStatus interface) with the passed key
     * @param key - Donation database key
     * @param dataStatus - DataStatus interface instance used to handle asynchronous calls.
     */
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


    /**
     * Sets up a realtime listener that will be called anytime a change is made to the Donations
     * collection. Returns via interface a list of Donation linked in any way to the User
     * @param userKey - the User key associated with returned Donations
     * @param dataStatus - DataStatus interface instance used to handle asynchronous calls.
     */
    public void getDonationsByUserRealTime(final String userKey, final DataStatus dataStatus) {

        mDonationDB.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                // Store our documents snapshot as final to be accessed in Makes query
                final QuerySnapshot donationSnapshot = queryDocumentSnapshots;

                if (donationSnapshot == null) {
                    Log.w(TAG, "Error retrieving realtime Donations.");
                    dataStatus.onError("Error retrieving realtime Donations.");
                }
                else {
                    mMakesDB.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                donationKeysList.clear();
                                // Populate list with all donation keys linked to our User
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    focusedMakes = document.toObject((Makes.class));

                                    // Check if the user key match any of the Makes user keys
                                    if (userKey.equals(focusedMakes.getClaimerKey()) ||
                                            userKey.equals(focusedMakes.getDonorKey()) ||
                                            userKey.equals(focusedMakes.getDriverKey())) {
                                        donationKeysList.add(focusedMakes.getDonationKey());
                                    }
                                } // end of Makes document retrieval

                                donationsList.clear();
                                // Loop through Donations, find any that match our user donation keys
                                for (QueryDocumentSnapshot document : donationSnapshot) {
                                    // If keys match, parse Donation and add it to list
                                    if (donationKeysList.contains(document.getId())) {
                                        donationsList.add(document.toObject((Donation.class)));
                                    }
                                }
                                // Task complete, send data via interface
                                dataStatus.DataIsRead(donationsList);
                                Log.i(TAG, "User Donation List successfully fetched from Donation DB.");

                            } else {
                                Log.d(TAG, "Error fetching donation keys from Makes collection: ", task.getException());
                                dataStatus.onError(task.getException().getMessage());
                            }
                        }
                    }); // end of Makes query

                }   // end of document snapshot !isEmpty condition
            }
        }); // end of Donation query
    }


    /**
     * Returns (via interface) a list of all donations linked in any way to a User.
     * @param userKey - the User key associated with returned Donations
     * @param dataStatus - DataStatus interface instance used to handle asynchronous calls.
     */
    public void getDonationsByUser(String userKey, final DataStatus dataStatus)
    {
        donationsList.clear();
        // Get list of Donation keys that have not been claimed from Makes db
        populateDonationKeysByUser(userKey, new DataStatus() {

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

            @Override
            public void onError(String errorMessage) { }
            @Override
            public void DataIsRead(List<?> items) { }   // not used
        });

    }

    /*
     * Helper method which returns various sets of Donation keys from Makes DB based on the status of the Donation
     * Status types: UNCLAIMED, DONATION_CLAIMED, DELIVERY_CLAIMED
     */
    private void populateDonationKeysByUser(final String userKey, final DataStatus dataStatus)
    {
        donationKeysList.clear();
        mMakesDB.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
     *
     */
    /**
     * This method returns (via interface) a list of all donations with the passed status
     * Status types: UNCLAIMED, DONATION_CLAIMED, DELIVERY_CLAIMED
     * @param status - the DonationStatus of the Donation
     * @param dataStatus - DataStatus interface instance used to handle asynchronous calls.
     */
    public void getDonationsByStatus(DonationStatus status, final DataStatus dataStatus)
    {
        donationsList.clear();
        // Get list of Donation keys that have not been claimed from Makes db
        populateDonationKeysByStatus(status, new DataStatus() {
            @Override
            public void DataIsRead(List<?> items) { }   // not used
            @Override
            public void DataIsProcessed() {
                // Keys list is updated
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

            @Override
            public void onError(String errorMessage) { }
        });

    }

    /*
     * Helper method which returns various sets of Donation keys from Makes DB based on the status of the Donation
     *  Status types: UNCLAIMED, DONATION_CLAIMED, DELIVERY_CLAIMED
     */
    private void populateDonationKeysByStatus(final DonationStatus status, final DataStatus dataStatus)
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

    /***************************************************
     * FIRESTORE DONATION DATABASE METHODS
     ***************************************************/

    /**
     * Returns (via interface) a list of all active Deliveries, built from Donations and Makes DB
     * @param dataStatus - DataStatus interface instance used to handle asynchronous calls.
     */
    public void getAllDeliveries(final DataStatus dataStatus)
    {
        populateDeliveryKeys(new DataStatus() {
            @Override
            public void DataIsRead(List<?> items) {
                // Get key lists
                final List<String> donationsKeysList = (List<String>) items.get(0);
                final List<String> donorKeysList = (List<String>) items.get(1);
                final List<String> claimerKeysList = (List<String>) items.get(2);
                final List<String> makesKeysList = (List<String>) items.get(3);
                // Makes sure list sizes match
                if (donationsKeysList.size() != donorKeysList.size() ||
                        donationsKeysList.size() != claimerKeysList.size() ||
                        donationsKeysList.size() != makesKeysList.size())
                {
                    dataStatus.onError("Error retrieving delivery keys (List size mismatch)");
                    Log.e(TAG, "Error retrieving delivery keys (List size mismatch)");
                }
                else {
                    final List<Delivery> deliveriesList = new ArrayList<>();
                    final int size = donationsKeysList.size();
                    for (int i = 0; i < size; i++) {
                        Log.i(TAG, "Building delivery for " + i + " Key = " + donationsKeysList.get(i));

                        buildDelivery(donationsKeysList.get(i), donorKeysList.get(i),
                                      claimerKeysList.get(i), makesKeysList.get(i), new DataStatus() {
                            @Override
                            public void DataIsRead(List<?> items) {
                                Delivery delivery = (Delivery) items.get(0);
                                deliveriesList.add(delivery);
                                Log.i(TAG, "Added to list : " + delivery.getDonation());

                                // Check if last entry
                                if (deliveriesList.size() == size)
                                {
                                    dataStatus.DataIsRead(deliveriesList);
                                    Log.i(TAG, "By some miracle, " + size + " Deliveries were retrieved.");
                                }
                            }
                            @Override
                            public void DataIsProcessed() { }
                            @Override
                            public void onError(String errorMessage) { }
                        });

                    }  // end of keys for loop
                }
            }
            @Override
            public void DataIsProcessed() { }
            @Override
            public void onError(String errorMessage) { }
        });

    }


    /**
     * Instantiate a realtime listener that will be called anytime the a change is made to the Makes db.
     * Returns all active Deliveries, including Donation and User information
     * @param dataStatus - DataStatus interface instance used to handle asynchronous calls.
     */
    public void getAllDeliveriesRealTime(final DataStatus dataStatus)
    {
        mMakesDB.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                getAllDeliveries(new DataStatus() {
                    @Override
                    public void DataIsRead(List<?> items) {
                        dataStatus.DataIsRead(items);
                    }

                    @Override
                    public void DataIsProcessed() {

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
            }
        });
    }

    /**
     * Returns (via interface) a list of all Deliveries claimed by the passed Driver (email).
     * @param driverEmail - the Driver's email
     * @param dataStatus - DataStatus interface instance used to handle asynchronous calls.
     */
    public void getUserDeliveries(String driverEmail, final DataStatus dataStatus)
    {
        getUserByEmail(driverEmail, new DataStatus() {
            @Override
            public void DataIsRead(List<?> items) {
                User user = (User) items.get(0);
                populateUserDeliveryKeys(user.getKey(), new DataStatus() {
                    @Override
                    public void DataIsRead(List<?> items) {
                        // Get key lists
                        final List<String> donationsKeysList = (List<String>) items.get(0);
                        final List<String> donorKeysList = (List<String>) items.get(1);
                        final List<String> claimerKeysList = (List<String>) items.get(2);
                        final List<String> makesKeysList = (List<String>) items.get(3);
                        // Makes sure list sizes match
                        if (donationsKeysList.size() != donorKeysList.size() ||
                                donationsKeysList.size() != claimerKeysList.size())
                        {
                            dataStatus.onError("Error retrieving delivery keys (List size mismatch)");
                            Log.e(TAG, "Error retrieving delivery keys (List size mismatch)");
                        }
                        else {
                            final List<Delivery> deliveriesList = new ArrayList<>();
                            final int size = donationsKeysList.size();
                            for (int i = 0; i < size; i++) {
                                Log.i(TAG, "Building delivery for " + i + " Key = " + donationsKeysList.get(i));

                                buildDelivery(donationsKeysList.get(i), donorKeysList.get(i),
                                        claimerKeysList.get(i), makesKeysList.get(i), new DataStatus() {
                                    @Override
                                    public void DataIsRead(List<?> items) {
                                        Delivery delivery = (Delivery) items.get(0);
                                        deliveriesList.add(delivery);
                                        Log.i(TAG, "Added to list : " + delivery.getDonation());

                                        // Check if last entry
                                        if (deliveriesList.size() == size)
                                        {
                                            dataStatus.DataIsRead(deliveriesList);
                                            Log.i(TAG, size + " User Deliveries were retrieved from this mess.");
                                        }
                                    }
                                    @Override
                                    public void DataIsProcessed() { }
                                    @Override
                                    public void onError(String errorMessage) { }
                                });

                            }  // end of keys for loop
                        }
                    }
                    @Override
                    public void DataIsProcessed() { }
                    @Override
                    public void onError(String errorMessage) {
                        Log.w(TAG, "Error retrieving user deliver keys.");
                    }
                });
            }
            @Override
            public void DataIsProcessed() { }
            @Override
            public void onError(String errorMessage) {
                Log.w(TAG, "Error retrieving user by email.");
            }
        });

    }


    /**
     * Instantiate a realtime listener that will be called anytime the a change is made to the Makes db.
     * Returns (via interface) a list of all Deliveries claimed by the passed Driver (email).
     * @param driverEmail - the Driver's email
     * @param dataStatus - DataStatus interface instance used to handle asynchronous calls.
     */
    public void getUserDeliveriesRealTime(final String driverEmail, final DataStatus dataStatus) {

        mMakesDB.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                getUserDeliveries(driverEmail, new DataStatus() {
                    @Override
                    public void DataIsRead(List<?> items) {
                        dataStatus.DataIsRead(items);
                    }

                    @Override
                    public void DataIsProcessed() {
                    }

                    @Override
                    public void onError(String errorMessage) {
                    }
                });
            }
        });
    }


    /*
     * Returns a list of all key lists associated with Deliveries.
     * List order: 0:donation keys > 1:donor keys > 2:claimer keys> 3:makes keys.
     */
    private void populateDeliveryKeys(final DataStatus dataStatus) {

        mMakesDB.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Create key lists
                    final List<String> donationsKeysList = new ArrayList<>();
                    final List<String> donorKeysList = new ArrayList<>();
                    final List<String> claimerKeysList = new ArrayList<>();
                    final List<String> makesKeysList = new ArrayList<>();

                    // Loop through Makes documents
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Makes makes = document.toObject((Makes.class));
                        // Check if the donation is claimed (but not delivered)
                        if (!makes.getClaimerKey().equals("") && makes.getDriverKey().equals(""))
                        {
                            // Add keys to lists
                            donationsKeysList.add(makes.getDonationKey());
                            donorKeysList.add(makes.getDonorKey());
                            claimerKeysList.add(makes.getClaimerKey());
                            makesKeysList.add(makes.getKey());
                        }
                    } // end of document for loop

                    // Create a list of key lists to send out
                    List<List<String>> keysLists = new ArrayList<List<String>>();
                    keysLists.add(donationsKeysList);
                    keysLists.add(donorKeysList);
                    keysLists.add(claimerKeysList);
                    keysLists.add(makesKeysList);
                    // Task complete, no need to send data since we will be staying in this class
                    // and have access to the keys list
                    dataStatus.DataIsRead(keysLists);
                    Log.i(TAG, "Delivery keys were fetched successfully from Makes DB.");

                } else {
                    Log.d(TAG, "Error fetching delivery keys from Makes collection: ", task.getException());
                }
            }
        });
    }

    /*
     * Returns a list of all key lists associated with Deliveries claimed by the passed driver key
     * List order: 0:donation keys > 1:donor keys > 2:claimer keys> 3:makes keys.
     */
    private void populateUserDeliveryKeys(String driverKey, final DataStatus dataStatus) {

        Log.i(TAG, "Driver key = " + driverKey);
        mMakesDB.whereEqualTo("driverKey", driverKey)
            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                    // Create key lists
                    final List<String> donationsKeysList = new ArrayList<>();
                    final List<String> donorKeysList = new ArrayList<>();
                    final List<String> claimerKeysList = new ArrayList<>();
                    final List<String> makesKeysList = new ArrayList<>();

                    // Loop through Makes documents
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Makes makes = document.toObject((Makes.class));

                        // Add keys to lists
                        donationsKeysList.add(makes.getDonationKey());
                        donorKeysList.add(makes.getDonorKey());
                        claimerKeysList.add(makes.getClaimerKey());
                        makesKeysList.add(makes.getKey());

                    } // end of document for loop

                    // Create a list of key lists to send out
                    List<List<String>> keysLists = new ArrayList<List<String>>();
                    keysLists.add(donationsKeysList);
                    keysLists.add(donorKeysList);
                    keysLists.add(claimerKeysList);
                    keysLists.add(makesKeysList);
                    // Task complete, no need to send data since we will be staying in this class
                    // and have access to the keys list
                    dataStatus.DataIsRead(keysLists);
                    Log.i(TAG, donationsKeysList.size() + " User Delivery keys were fetched successfully from Makes DB.");

                } else {
                    Log.d(TAG, "Error fetching delivery keys from Makes collection: ", task.getException());
                }
            }
        });
    }

    /*
     * Builds a Delivery object from the passed keys. Returns via interface. Be careful with
     * asynchronous calls here, they are not built in a predictable order.
     */
    private void buildDelivery(String donationKey, String donorKey, String claimerKey, final String makesKey, final DataStatus dataStatus)
    {
        final Delivery outputDelivery = new Delivery();
        final List<Delivery> deliveriesList = new ArrayList<>();
        // Get Donation
        getDonation(donationKey, new DataStatus() {
            @Override
            public void DataIsRead(List<?> items) {
                outputDelivery.setDonation((Donation) items.get(0));
                // Check if all fields have been filled
                if (outputDelivery.isComplete())
                {
                    outputDelivery.setMakesKey(makesKey);
                    deliveriesList.clear();
                    deliveriesList.add(outputDelivery);
                    dataStatus.DataIsRead(deliveriesList);
                }
            }
            @Override
            public void DataIsProcessed() { }
            @Override
            public void onError(String errorMessage) { }
        });

        // Get Donor
        getUserByKey(donorKey, new DataStatus() {
            @Override
            public void DataIsRead(List<?> items) {
                outputDelivery.setDonor((User) items.get(0));
                // Check if all fields have been filled
                if (outputDelivery.isComplete())
                {
                    outputDelivery.setMakesKey(makesKey);
                    deliveriesList.clear();
                    deliveriesList.add(outputDelivery);
                    dataStatus.DataIsRead(deliveriesList);
                }
            }
            @Override
            public void DataIsProcessed() { }
            @Override
            public void onError(String errorMessage) { }
        });

        // Get Claimer
        getUserByKey(claimerKey, new DataStatus() {
            @Override
            public void DataIsRead(List<?> items) {
                outputDelivery.setClaimer((User) items.get(0));
                // Check if all fields have been filled
                if (outputDelivery.isComplete())
                {
                    outputDelivery.setMakesKey(makesKey);
                    deliveriesList.clear();
                    deliveriesList.add(outputDelivery);
                    dataStatus.DataIsRead(deliveriesList);
                }                                 }
            @Override
            public void DataIsProcessed() { }
            @Override
            public void onError(String errorMessage) { }
        });
    }


    /**
     * Deletes a Donation and Makes from the database
     * @param donationKey - the donation to be deleted
     * @param makesKey - the makes to be deleted
     * @param dataStatus - DataStatus interface instance used to handle asynchronous calls.
     */
    public void deleteDelivery(String donationKey, final String makesKey, final DataStatus dataStatus)
    {
        // Delete donation
        mDonationDB.document(donationKey).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, "Donation was deleted successfully.");
                    // Delete Makes
                    mMakesDB.document(makesKey).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.i(TAG, "Makes was deleted successfully.");
                                dataStatus.DataIsProcessed();
                            }
                            else
                            {
                                dataStatus.onError(task.getException().getMessage());
                                Log.w(TAG, "Error deleting Makes.", task.getException());
                            }
                        }
                    });
                }
                else
                {
                    dataStatus.onError(task.getException().getMessage());
                    Log.w(TAG, "Error deleting Donation.", task.getException());
                }
            }
        });
    }



}  // end of class



    /*


    DEPRECATED



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

    public void updateDonation(User updatedDonation, final DataStatus dataStatus)
    {
        mDonationDB.document(updatedDonation.getKey())
                .set(updatedDonation)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
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

    */
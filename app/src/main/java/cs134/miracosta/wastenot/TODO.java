/**
 *
 *  ***Will***
 * Firebase calls:
 *    - DONE: addUser(User)   // used for login
 *    - DONE: getUserByEmail(String email)     // user for login
 *    - DONE: getUserByKey(String key)
 *    - DONE: getDonationsByStatus(DonationStatus status)   // used for ClaimListActivity
 *    -         e.g if you pass this DonationStatus.UNCLAIMED, it will return a list of all unclaimed donations
 *    - DONE: getDonationsByUser(String userKey)    - used for MyClaimsActivity
 *    - DONE: claimDonation(Donation, String userKey (claimer))   - used for ClaimDetailsActivity
 *              note: claimDonation will also update the db with any changes you make to that passed Donations (like giving it a dropoff time)
 *
 * Other:
 *    DONE: (PRIO) Add Ahmad's strings.xml
 *    TODO: finish delivery/google maps layout
 *    TODO: finish New Donation spinners
 *    TODO: translate strings
 *    TODO: Javadocs, JUnit testing, Animation
 *    TODO: delete this file :)
 *
 *
 *
 *
 *
 * ***Ahmad***
 *
 * TODO: Get location information from user, add location getter/setters etc. to User class
 *      Address, City, State, Zip
*  TODO: Replace Database calls with FirebaseDBHelper methods (see above for the methods I've added and see TestActivity for example on how to user the DataStatus interface to implement them)
 * TODO: In ClaimDetailsActivity: prompt the user for a dropoff end time (the latest a driver can pick up). I will have a spinner with set times you copy if you want.
 * TODO: Type migrate Claimer/Driver to User
 * TODO: Type migrate Claim/Delivery to Donation
 * TODO: Add Button in ClaimListActivity that take user to DeliveryActivity
 *
 * Don't forget to download the google-services.json I emailed you and put it in app directory.
  */





package cs134.miracosta.wastenot;

public class TODO {
}


package cs134.miracosta.wastenot.Model;

import java.util.Objects;

/**
 * The <code>Makes</code> class contains all database keys pertaining to a single Donation, providing
 * a link between a Donation and all user types associated with it(Donor, Claimer and Driver).
 *
 * @author Will Craycroft
 */
public class Makes {

    private String mKey;
    private String mDonationKey;
    private String mDonorKey;
    private String mClaimerKey;
    private String mDriverKey;

    /**
     * Creates a new Makes object given the Donation and Donor keys. Every donation will start with this
     * information, with Claimer and Driver keys added later.
     * @param donationKey - the database key of the Donation object
     * @param donorKey - the database key of the Donor.
     */
    public Makes(String donationKey, String donorKey) {
        mKey = "";
        mDonationKey = donationKey;
        mDonorKey = donorKey;
        mClaimerKey = "";
        mDriverKey = "";
    }

    /**
     * Default constructor used by Cloud Firestore to build Makes objects from database.
     */
    public Makes() {
    }

    /**
     * Returns the database key for this Makes object.
     * @return - Makes DB key
     */
    public String getKey() {
        return mKey;
    }

    /**
     * Sets the database key for this Makes object.
     * @param key - the database key
     */
    public void setKey(String key) {
        mKey = key;
    }

    /**
     * Returns the database key of the Donation being made
     * @return - the Donation key
     */
    public String getDonationKey() {
        return mDonationKey;
    }

    /**
     * Sets the database key of the Donation to be made (keep public for Firebase)
     * @param donationKey - the Donation key
     */
    public void setDonationKey(String donationKey) {
        mDonationKey = donationKey;
    }

    /**
     * Returns the database key of the Donor
     * @return - the Donor key.
     */
    public String getDonorKey() {
        return mDonorKey;
    }

    /**
     * Sets the database key of the Donor
     * @param donorKey - the Donor key
     */
    public void setDonorKey(String donorKey) {
        mDonorKey = donorKey;
    }

    /**
     * Returns the database key of the user claiming the donation
     * @return - the Claimer key
     */
    public String getClaimerKey() {
        return mClaimerKey;
    }

    /**
     * Sets the database key of the user claiming the donation
     * @param claimerKey - the Claimer key
     */
    public void setClaimerKey(String claimerKey) {
        mClaimerKey = claimerKey;
    }

    /**
     * Returns the database key of the driver delivering the Donation
     * @return - the Driver key
     */
    public String getDriverKey() {
        return mDriverKey;
    }

    /**
     * Sets the database key of the driver delivering the Donation
     * @param driverKey - the Driver key
     */
    public void setDriverKey(String driverKey) {
        mDriverKey = driverKey;
    }

    /**
     * Returns true of the pass Object is a Makes and all fields are equal to this instance.
     * @param o - the passed Object
     * @return - True if Objects is of type Makes and all fields are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Makes makes = (Makes) o;
        return Objects.equals(mKey, makes.mKey) &&
                Objects.equals(mDonationKey, makes.mDonationKey) &&
                Objects.equals(mDonorKey, makes.mDonorKey) &&
                Objects.equals(mClaimerKey, makes.mClaimerKey) &&
                Objects.equals(mDriverKey, makes.mDriverKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mKey, mDonationKey, mDonorKey, mClaimerKey, mDriverKey);
    }

    /**
     * Returns a String representation of a Makes object, including all database keys.
     * @return - String representation of a Makes
     */
    @Override
    public String toString() {
        return "Makes{" +
                "Key='" + mKey + '\'' +
                ", DonationKey='" + mDonationKey + '\'' +
                ", DonorKey='" + mDonorKey + '\'' +
                ", ClaimerKey='" + mClaimerKey + '\'' +
                ", DriverKey='" + mDriverKey + '\'' +
                '}';
    }
}

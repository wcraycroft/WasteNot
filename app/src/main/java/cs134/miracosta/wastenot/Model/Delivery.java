package cs134.miracosta.wastenot.Model;

import java.util.Objects;

/**
 * The <code>Delivery</code> class represents a Donation that has at least been claimed by a food
 * relief destination, and possibly a driver. It encapsulates the Donation and all User classes needed
 * to display full Donation information, including addresses of Donors and Claimers.
 *
 * @author Will Craycroft
 */

public class Delivery {

    private Donation mDonation;
    private User mDonor;
    private User mClaimer;
    private User mDriver;
    private String mMakesKey;

    /**
     * Creates a new Delivery object with empty fields.
     */
    public Delivery() {
        mDonation = null;
        mDonor = null;
        mClaimer = null;
        mDriver = null;
        mMakesKey = "";
    }

    /**
     * Returns the Makes database key
     * @return - the Makes database key
     */
    public String getMakesKey() {
        return mMakesKey;
    }

    /**
     * Sets the Makes database key
     * @param makesKey - the Makes database key
     */
    public void setMakesKey(String makesKey) {
        mMakesKey = makesKey;
    }

    /**
     * Returns the Driver object if one has claimed this <code>Delivery</code>
     * @return - the Driver object
     */
    public User getDriver() {
        return mDriver;
    }

    /**
     * Sets the Driver object once the Delivery is claimed
     * @param driver - the Driver object
     */
    public void setDriver(User driver) {
        mDriver = driver;
    }

    /**
     * Returns the Donation object, with all Donation information
     * @return - the Donation associated with this Delivery
     */
    public Donation getDonation() {
        return mDonation;
    }

    /**
     * Sets the Donation for this <code>Delivery</code>
     * @param donation - - the Donation associated with this Delivery
     */
    public void setDonation(Donation donation) {
        mDonation = donation;
    }

    /**
     * Returns the Donor user, including pickup location information
     * @return - the Donor user
     */
    public User getDonor() {
        return mDonor;
    }

    /**
     * Sets the Donor user
     * @param donor - the Donor user
     */
    public void setDonor(User donor) {
        mDonor = donor;
    }

    /**
     * Gets the Claimer, including the delivery destination for this <code>Delivery</code>
     * @return - the Claimer user
     */
    public User getClaimer() {
        return mClaimer;
    }

    /**
     * Sets the Claimer user
     * @param claimer - the Claimer user
     */
    public void setClaimer(User claimer) {
        mClaimer = claimer;
    }

    /**
     * Returns true once the Donor, Claimer and Donation have been set.
     * This is called to determine when asynchronous database calls are completed.
     * @return - True if select fields have been set.
     */
    public boolean isComplete()
    {
        return mDonor != null && mClaimer != null && mDonation != null;
    }

    /**
     * Returns true if the passed Objects is a Delivery and all fields match this Delivery
     * @param o
     * @return - the equality of the passed Delivery with this Delivery
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Delivery delivery = (Delivery) o;
        return Objects.equals(mDonation, delivery.mDonation) &&
                Objects.equals(mDonor, delivery.mDonor) &&
                Objects.equals(mClaimer, delivery.mClaimer) &&
                Objects.equals(mDriver, delivery.mDriver) &&
                Objects.equals(mMakesKey, delivery.mMakesKey);
    }

    /**
     * Retuns a String representation of this Delivery, including all User fields and locations.
     * @return - a String representation of this Delivery
     */
    @Override
    public String toString() {
        return "Delivery{" +
                "Donation=" + mDonation +
                ", Donor=" + mDonor +
                ", Claimer=" + mClaimer +
                ", Driver=" + mDriver +
                ", MakesKey='" + mMakesKey + '\'' +
                '}';
    }
}

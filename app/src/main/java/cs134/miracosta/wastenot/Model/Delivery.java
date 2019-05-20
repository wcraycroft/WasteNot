package cs134.miracosta.wastenot.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Delivery {

    private Donation mDonation;
    private User mDonor;
    private User mClaimer;
    private User mDriver;
    private String mMakesKey;

    public Delivery() {
        mDonation = null;
        mDonor = null;
        mClaimer = null;
        mDriver = null;
        mMakesKey = "";
    }

    public String getMakesKey() {
        return mMakesKey;
    }

    public void setMakesKey(String makesKey) {
        mMakesKey = makesKey;
    }

    public User getDriver() {
        return mDriver;
    }

    public void setDriver(User driver) {
        mDriver = driver;
    }

    public Donation getDonation() {
        return mDonation;
    }

    public void setDonation(Donation donation) {
        mDonation = donation;
    }

    public User getDonor() {
        return mDonor;
    }

    public void setDonor(User donor) {
        mDonor = donor;
    }

    public User getClaimer() {
        return mClaimer;
    }

    public void setClaimer(User claimer) {
        mClaimer = claimer;
    }

    public boolean isComplete()
    {
        return mDonor != null && mClaimer != null && mDonation != null;
    }


}

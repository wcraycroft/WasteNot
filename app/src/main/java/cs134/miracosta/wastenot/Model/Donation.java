package cs134.miracosta.wastenot.Model;

import java.sql.Time;
import java.util.Objects;

public class Donation {

    private String mKey;
    private String mType;
    private Donor mDonor;
    private Claimer mClaimer;      // Null if has not been claimed yet
    private FoodType mFoodType;    // Enum class
    private int mServings;
    private boolean fitInCar;
    private String otherInfo;
    private Time readyTime;
    private Time pickupEndTime;

    /**
     *
     * @param donor
     * @param claimer
     * @param foodType
     * @param servings
     * @param fitInCar
     * @param otherInfo
     * @param readyTime
     * @param pickupEndTime
     */
    public Donation(String type, Donor donor, Claimer claimer, FoodType foodType, int servings, boolean fitInCar, String otherInfo, Time readyTime, Time pickupEndTime) {
        mKey = "void";
        mType = type;
        mDonor = donor;
        mClaimer = claimer;
        mFoodType = foodType;
        mServings = servings;
        this.fitInCar = fitInCar;
        this.otherInfo = otherInfo;
        this.readyTime = readyTime;
        this.pickupEndTime = pickupEndTime;
    }

    public Donation() {
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public Donor getDonor() {
        return mDonor;
    }

    public void setDonor(Donor donor) {
        mDonor = donor;
    }

    public Claimer getClaimer() {
        return mClaimer;
    }

    public void setClaimer(Claimer claimer) {
        mClaimer = claimer;
    }

    public FoodType getFoodType() {
        return mFoodType;
    }

    public void setFoodType(FoodType foodType) {
        mFoodType = foodType;
    }

    public int getServings() {
        return mServings;
    }

    public void setServings(int servings) {
        mServings = servings;
    }

    public boolean isFitInCar() {
        return fitInCar;
    }

    public void setFitInCar(boolean fitInCar) {
        this.fitInCar = fitInCar;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public Time getReadyTime() {
        return readyTime;
    }

    public void setReadyTime(Time readyTime) {
        this.readyTime = readyTime;
    }

    public Time getPickupEndTime() {
        return pickupEndTime;
    }

    public void setPickupEndTime(Time pickupEndTime) {
        this.pickupEndTime = pickupEndTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Donation donation = (Donation) o;
        return mServings == donation.mServings &&
                fitInCar == donation.fitInCar &&
                Objects.equals(mKey, donation.mKey) &&
                Objects.equals(mDonor, donation.mDonor) &&
                Objects.equals(mClaimer, donation.mClaimer) &&
                mFoodType == donation.mFoodType &&
                Objects.equals(otherInfo, donation.otherInfo) &&
                Objects.equals(readyTime, donation.readyTime) &&
                Objects.equals(pickupEndTime, donation.pickupEndTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mKey, mDonor, mClaimer, mFoodType, mServings, fitInCar, otherInfo, readyTime, pickupEndTime);
    }

    @Override
    public String toString() {
        return "Donation{" +
                "Key = " + mKey +
                "Type = " + mType +
                "Donor=" + mDonor +
                ", Claimer=" + mClaimer +
                ", Food Type=" + mFoodType +
                ", Servings=" + mServings +
                ", Fit In Car=" + fitInCar +
                ", Other Info='" + otherInfo + '\'' +
                ", Ready Time=" + readyTime +
                ", Pickup End Time=" + pickupEndTime +
                '}';
    }
}

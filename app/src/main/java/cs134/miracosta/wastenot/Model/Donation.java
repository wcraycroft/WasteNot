
// Picasso Library for open source images

package cs134.miracosta.wastenot.Model;

import java.io.Serializable;
import java.util.Objects;

import cs134.miracosta.wastenot.Model.Enums.DonationStatus;
import cs134.miracosta.wastenot.Model.Enums.FoodType;

public class Donation implements Serializable
{

    private String mKey;
    private DonationStatus mStatus;
    private FoodType mFoodType;    // Enum class
    private int mServings;
    private boolean fitInCar;
    private String mOtherInfo;
    private String mReadyTime;
    private String mPickupEndTime;
    private String mPickupTime;
    private String mDropoffEndTime;

    /**
     *
     * @param status
     * @param foodType
     * @param servings
     * @param fitInCar
     * @param otherInfo
     * @param readyTime
     * @param pickupEndTime
     */
    public Donation(FoodType foodType, int servings, boolean fitInCar, String otherInfo, String readyTime, String pickupEndTime)
    {
        mKey = "void";
        mStatus = DonationStatus.UNCLAIMED;
        mFoodType = foodType;
        mServings = servings;
        this.fitInCar = fitInCar;
        mOtherInfo = otherInfo;
        mReadyTime = readyTime;
        mPickupEndTime = pickupEndTime;
    }

    public Donation() {
    }


    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public DonationStatus getStatus() {
        return mStatus;
    }

    public void setStatus(DonationStatus status) {
        mStatus = status;
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
        return mOtherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.mOtherInfo = otherInfo;
    }

    public String getReadyTime() {
        return mReadyTime;
    }

    public void setReadyTime(String readyTime) {
        this.mReadyTime = readyTime;
    }

    public String getPickupEndTime() {
        return mPickupEndTime;
    }

    public void setPickupEndTime(String pickupEndTime) {
        this.mPickupEndTime = pickupEndTime;
    }

    public String getPickupTime() {
        return mPickupTime;
    }

    public void setPickupTime(String pickupTime) {
        mPickupTime = pickupTime;
    }

    public String getDropoffEndTime() {
        return mDropoffEndTime;
    }

    public void setDropoffEndTime(String dropoffEndTime) {
        mDropoffEndTime = dropoffEndTime;
    }

    @Override
    public String toString() {
        return "Donation{" +
                "Key='" + mKey + '\'' +
                ", Status='" + mStatus + '\'' +
                ", FoodType=" + mFoodType +
                ", Servings=" + mServings +
                ", fitInCar=" + fitInCar +
                ", OtherInfo='" + mOtherInfo + '\'' +
                ", ReadyTime=" + mReadyTime +
                ", PickupEndTime=" + mPickupEndTime +
                ", PickupTime=" + mPickupTime +
                ", DropoffEndTime=" + mDropoffEndTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Donation donation = (Donation) o;
        return mServings == donation.mServings &&
                fitInCar == donation.fitInCar &&
                Objects.equals(mKey, donation.mKey) &&
                mStatus == donation.mStatus &&
                mFoodType == donation.mFoodType &&
                Objects.equals(mOtherInfo, donation.mOtherInfo) &&
                Objects.equals(mReadyTime, donation.mReadyTime) &&
                Objects.equals(mPickupEndTime, donation.mPickupEndTime) &&
                Objects.equals(mPickupTime, donation.mPickupTime) &&
                Objects.equals(mDropoffEndTime, donation.mDropoffEndTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mKey, mStatus, mFoodType, mServings, fitInCar, mOtherInfo, mReadyTime, mPickupEndTime, mPickupTime, mDropoffEndTime);
    }
}


// Picasso Library for open source images

package cs134.miracosta.wastenot.Model;

import java.io.Serializable;

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
    private String claimerKey;

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
    public Donation(DonationStatus status, FoodType foodType, int servings, boolean fitInCar, String otherInfo, String readyTime, String pickupEndTime)
    {
        mKey = "void";
        mStatus = status;
        mFoodType = foodType;
        mServings = servings;
        fitInCar = fitInCar;
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
}

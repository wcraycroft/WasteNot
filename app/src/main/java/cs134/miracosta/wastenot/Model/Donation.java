
package cs134.miracosta.wastenot.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

import cs134.miracosta.wastenot.Model.Enums.DonationStatus;
import cs134.miracosta.wastenot.Model.Enums.FoodType;

/**
 * The <code>Donation</code> class contains information specific to a food donation, including all
 * food details, pickup and dropoff times, the database key, and the status of the donation.
 *
 * @author Will Craycroft
 */

public class Donation implements Parcelable
{
    // Member variables
    /** The Donation database key*/
    private String mKey;
    /** The status of this donation (UNCLAIMED, CLAIMED, DELIVERY_CLAIMED*/
    private DonationStatus mStatus;
    /** The food type of this donation (see enum)*/
    private FoodType mFoodType;
    /** The approximate number of servings*/
    private int mServings;
    /** True if the donation will fit in one car*/
    private boolean fitInCar;
    /** Other information entered by user*/
    private String mOtherInfo;
    /** Time the donation will be ready for be picked up from Donor*/
    private String mReadyTime;
    /** The Latest Time the donation can be picked up from Donor*/
    private String mPickupEndTime;
    /** Time the Driver has selected for pickup */
    private String mPickupTime;
    /** The Latest Time the donation can be dropped off at Claimer */
    private String mDropoffEndTime;

    /**
     * Full constructor sets all <code>Donation</code> fields to the passed values, except for the
     * key, which will be set later by the database.
     * @param foodType - The type of food (enum)
     * @param servings - The approximate number of servings
     * @param fitInCar - True if the donation will fit in one car
     * @param otherInfo - Other information entered by user
     * @param readyTime - Time the donation will be ready for be picked up from Donor
     * @param pickupEndTime - Latest Time the donation can be picked up from Donor
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

    /**
     * Default constructor. Used by Cloud Firestore in conjunction with setters to build objects
     * from database.
     */
    public Donation() {
    }

    /**
     * Parcelable Constructor. Reads all fields from the passed Parcel object.
     * Note: converts food type back to enum (sent as String)
     * @param in - The Parcel to be read from
     */
    protected Donation(Parcel in) {
        mKey = in.readString();
        mServings = in.readInt();
        fitInCar = in.readByte() != 0;
        mOtherInfo = in.readString();
        mReadyTime = in.readString();
        mPickupEndTime = in.readString();
        mPickupTime = in.readString();
        mDropoffEndTime = in.readString();
        String foodTypeStr = in.readString();

        switch(foodTypeStr) {
            case "Meat":
                mFoodType = FoodType.MEAT;
                break;
            case "Dairy":
                mFoodType = FoodType.DAIRY;
                break;
            case "Other":
                mFoodType = FoodType.OTHER;
                break;
            case "Produce":
                mFoodType = FoodType.PRODUCE;
                break;
            case "Baked":
                mFoodType = FoodType.BAKED_GOODS;
                break;
            case "Tray":
                mFoodType = FoodType.PREPARED_TRAY;
                break;
            case "Packaged":
                mFoodType = FoodType.PREPARED_PACKAGED;
                break;
            default:
                mFoodType = FoodType.OTHER;
        }
    }

    /**
     * Overrides the Parcel Creator's method to create a Donation from Parcel
     */
    public static final Creator<Donation> CREATOR = new Creator<Donation>() {
        @Override
        public Donation createFromParcel(Parcel in) {
            return new Donation(in);
        }

        @Override
        public Donation[] newArray(int size) {
            return new Donation[size];
        }
    };

    /**
     * Returns the database key for this <code>Donation</code>.
     * @return - the database key
     */
    public String getKey() {
        return mKey;
    }

    /**
     * Sets the database key for this <code>Donation</code>
     * @param key - the database key
     */
    public void setKey(String key) {
        mKey = key;
    }

    /**
     * Returns the status of the <code>Donation</code> (claimed, unclaimed or delivery claimed). See enum.
     * @return - the <code>DonationStatus</code> for this <code>Donation</code>
     */
    public DonationStatus getStatus() {
        return mStatus;
    }

    /**
     * Sets the status of the <code>Donation</code>
     * @param status - the <code>DonationStatus</code> for this <code>Donation</code>
     */
    public void setStatus(DonationStatus status) {
        mStatus = status;
    }

    /**
     * Returns the food type for this Donation
     * @return - the <code>FoodType</code> for this <code>Donation</code>
     */
    public FoodType getFoodType() {
        return mFoodType;
    }

    /**
     * Sets the food type for this Donation
     * @param foodType - the <code>FoodType</code> for this <code>Donation</code>
     */
    public void setFoodType(FoodType foodType) {
        mFoodType = foodType;
    }

    /**
     * Gets the approximate number of servings of this Donation
     * @return - the number of servings
     */
    public int getServings() {
        return mServings;
    }

    /**
     * Sets the number of servings of this Donation
     * @param servings - the number of servings
     */
    public void setServings(int servings) {
        mServings = servings;
    }

    /**
     * Returns true if this Donation can fit in one car
     * @return - True if Donation fits in one car
     */
    public boolean isFitInCar() {
        return fitInCar;
    }

    /**
     * Set whether or not this Donation can fit in one car
     * @param fitInCar - True if Donation fits in one car
     */
    public void setFitInCar(boolean fitInCar) {
        this.fitInCar = fitInCar;
    }

    /**
     * Returns a String containing any other information the Donor entered
     * @return - other Donation information
     */
    public String getOtherInfo() {
        return mOtherInfo;
    }

    /**
     * Sets any other information the Donor entered as a String
     * @param otherInfo - other Donation information
     */
    public void setOtherInfo(String otherInfo) {
        this.mOtherInfo = otherInfo;
    }

    /**
     * Returns the time at which the Donation will be ready for pickup
     * @return - pickup ready time
     */
    public String getReadyTime() {
        return mReadyTime;
    }

    /**
     * Sets the time at which the Donation will be ready for pickup
     * @param readyTime - pickup ready time
     */
    public void setReadyTime(String readyTime) {
        this.mReadyTime = readyTime;
    }

    /**
     * Returns the latest time at which the Donation can be picked up
     * @return - the pickup end time
     */
    public String getPickupEndTime() {
        return mPickupEndTime;
    }

    /**
     * Sets the latest time at which the Donation can be picked up
     * @param pickupEndTime - the pickup end time
     */
    public void setPickupEndTime(String pickupEndTime) {
        this.mPickupEndTime = pickupEndTime;
    }

    /**
     * Returns the time at which the Driver has requested to pickup the Donation
     * @return - the pickup time
     */
    public String getPickupTime() {
        return mPickupTime;
    }

    /**
     * Sets the time at which the Driver has requested to pickup the Donation
     * @param pickupTime - the pickup time
     */
    public void setPickupTime(String pickupTime) {
        mPickupTime = pickupTime;
    }

    /**
     * Returns the latest time at which the Donation can be delivered to the Claimer
     * @return - dropoff end time
     */
    public String getDropoffEndTime() {
        return mDropoffEndTime;
    }

    /**
     * Sets the latest time at which the Donation can be delivered to the Claimer
     * @param dropoffEndTime - dropoff end time
     */
    public void setDropoffEndTime(String dropoffEndTime) {
        mDropoffEndTime = dropoffEndTime;
    }

    /**
     * Returns a String representation of this Donation, including all field values
     * @return - a String representation of this Donation
     */
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

    /**
     * Returns true if the passed Object is a Donation and all fields are equal to this Donation
     * @param o
     * @return - true if the Donation fields are equal to the passed Object
     */
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
    public int describeContents() {
        return 0;
    }

    /**
     * Writes this Donation to the destination Parcel object.
     * Note: Sends the food type as a String (see Parcel constructor).
     * @param dest - the destination Parcel
     * @param flags - Parcel flags (not used)
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mKey);
        dest.writeInt(mServings);
        dest.writeByte((byte) (fitInCar ? 1 : 0));
        dest.writeString(mOtherInfo);
        dest.writeString(mReadyTime);
        dest.writeString(mPickupEndTime);
        dest.writeString(mPickupTime);
        dest.writeString(mDropoffEndTime);

        switch (mFoodType)
        {
            case MEAT:
                dest.writeString("Meat");
                break;
            case DAIRY:
                dest.writeString("Dairy");
                break;
            case OTHER:
                dest.writeString("Other");
                break;
            case PRODUCE:
                dest.writeString("Produce");
                break;
            case BAKED_GOODS:
                dest.writeString("Baked");
                break;
            case PREPARED_TRAY:
                dest.writeString("Tray");
                break;
            case PREPARED_PACKAGED:
                dest.writeString("Packaged");
                break;
            default:
                dest.writeString("Meat");
        }
    }
}

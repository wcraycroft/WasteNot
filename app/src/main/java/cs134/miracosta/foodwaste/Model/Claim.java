package cs134.miracosta.foodwaste.Model;

import java.sql.Time;
import java.util.Objects;

public class Claim extends Donation {

    private Time mDropOffEndTime;

    /**
     *
     * @param donor
     * @param foodType
     * @param servings
     * @param fitInCar
     * @param otherInfo
     * @param readyTime
     * @param pickupEndTime
     * @param dropOffEndTime
     */
    // Does not need claimer because it will be null at first and set later
    public Claim(Donor donor, FoodType foodType, int servings, boolean fitInCar, String otherInfo, Time readyTime, Time pickupEndTime, Time dropOffEndTime) {
        super(donor, null, foodType, servings, fitInCar, otherInfo, readyTime, pickupEndTime);
        mDropOffEndTime = dropOffEndTime;
    }

    public Time getDropOffEndTime() {
        return mDropOffEndTime;
    }

    public void setDropOffEndTime(Time dropOffEndTime) {
        mDropOffEndTime = dropOffEndTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Claim claim = (Claim) o;
        return Objects.equals(mDropOffEndTime, claim.mDropOffEndTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), mDropOffEndTime);
    }

    @Override
    public String toString() {
        return "Claim{" +
                "Donor=" + getDonor() +
                ", Claimer=" + getClaimer() +
                ", Food Type=" + getFoodType() +
                ", Servings=" + getServings() +
                ", Fit In Car=" + isFitInCar() +
                ", Other Info='" + getOtherInfo() + '\'' +
                ", Ready Time=" + getReadyTime() +
                ", Pickup End Time=" + getPickupEndTime() +
                ", DropOff End Time=" + mDropOffEndTime +
                '}';
    }
}

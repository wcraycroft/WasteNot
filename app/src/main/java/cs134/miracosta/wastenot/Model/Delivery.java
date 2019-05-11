package cs134.miracosta.wastenot.Model;

import java.sql.Time;
import java.util.Objects;

public class Delivery extends Donation {

    private Driver mDriver;
    private Time mPickupTime;

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
     * @param driver
     * @param pickupTime
     */
    public Delivery(Donor donor, Claimer claimer, FoodType foodType, int servings, boolean fitInCar, String otherInfo, Time readyTime, Time pickupEndTime, Driver driver, Time pickupTime) {
        super(donor, claimer, foodType, servings, fitInCar, otherInfo, readyTime, pickupEndTime);
        mDriver = driver;
        mPickupTime = pickupTime;
    }

    public Driver getDriver() {
        return mDriver;
    }

    public void setDriver(Driver driver) {
        mDriver = driver;
    }

    public Time getPickupTime() {
        return mPickupTime;
    }

    public void setPickupTime(Time pickupTime) {
        mPickupTime = pickupTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Delivery delivery = (Delivery) o;
        return Objects.equals(mDriver, delivery.mDriver) &&
                Objects.equals(mPickupTime, delivery.mPickupTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), mDriver, mPickupTime);
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "Donor=" + getDonor() +
                ", Claimer=" + getClaimer() +
                ", Food Type=" + getFoodType() +
                ", Servings=" + getServings() +
                ", Fit In Car=" + isFitInCar() +
                ", Other Info='" + getOtherInfo() + '\'' +
                ", Ready Time=" + getReadyTime() +
                ", Pickup End Time=" + getPickupEndTime() +
                ", Driver=" + mDriver +
                ", Pickup Time=" + mPickupTime +
                '}';
    }
}

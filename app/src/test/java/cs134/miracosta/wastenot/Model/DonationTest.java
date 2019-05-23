package cs134.miracosta.wastenot.Model;

import org.junit.Before;
import org.junit.Test;

import cs134.miracosta.wastenot.Model.Enums.DonationStatus;
import cs134.miracosta.wastenot.Model.Enums.FoodType;

import static org.junit.Assert.*;

public class DonationTest {

    private Donation testDonation;

    @Before
    public void setUp() throws Exception {
        testDonation = new Donation(FoodType.OTHER, 10, true,
                "other", "123", "111");
    }

    @Test
    public void getKey() {
        assertEquals("Testing getKey() method", "void", testDonation.getKey());
    }

    @Test
    public void setKey() {
        testDonation.setKey("testKey");
        assertEquals("Testing setKey() method", "testKey", testDonation.getKey());
    }

    @Test
    public void getStatus() {
        assertEquals("Testing getStatus() method", DonationStatus.UNCLAIMED, testDonation.getStatus());
    }

    @Test
    public void setStatus() {
        testDonation.setStatus(DonationStatus.DONATION_CLAIMED);
        assertEquals("Testing setStatus() method", DonationStatus.DONATION_CLAIMED, testDonation.getStatus());
    }

    @Test
    public void getFoodType() {
        assertEquals("Testing getFoodType() method", FoodType.OTHER, testDonation.getFoodType());
    }

    @Test
    public void setFoodType() {
        testDonation.setFoodType(FoodType.DAIRY);
        assertEquals("Testing setFoodType() method", FoodType.DAIRY, testDonation.getFoodType());
    }

    @Test
    public void getServings() {
        assertEquals("Testing getServings() method", 10, testDonation.getServings());
    }

    @Test
    public void setServings() {
        testDonation.setServings(15);
        assertEquals("Testing setServings() method", 15, testDonation.getServings());
    }

    @Test
    public void isFitInCar() {
        assertTrue("Testing getKey() method", testDonation.isFitInCar());
    }

    @Test
    public void setFitInCar() {
        testDonation.setFitInCar(false);
        assertFalse("Testing setFitInCar() method", testDonation.isFitInCar());
    }

    @Test
    public void getOtherInfo() {
        assertEquals("Testing getOtherInfo() method", "other", testDonation.getOtherInfo());
    }

    @Test
    public void setOtherInfo() {
        testDonation.setOtherInfo("testInfo");
        assertEquals("Testing setOtherInfo() method", "testInfo", testDonation.getOtherInfo());
    }

    @Test
    public void getReadyTime() {
        assertEquals("Testing getReadyTime() method", "123", testDonation.getReadyTime());
    }

    @Test
    public void setReadyTime() {
        testDonation.setReadyTime("321");
        assertEquals("Testing setReadyTime() method", "321", testDonation.getReadyTime());
    }

    @Test
    public void getPickupEndTime() {
        assertEquals("Testing getPickupEndTime() method", "111", testDonation.getPickupEndTime());
    }

    @Test
    public void setPickupEndTime() {
        testDonation.setPickupEndTime("testTime");
        assertEquals("Testing setPickupEndTime() method", "testTime", testDonation.getPickupEndTime());
    }

    @Test
    public void getPickupTime() {
        testDonation.setPickupTime("testTime");
        assertEquals("Testing getPickupTime() method", "testTime", testDonation.getPickupTime());
    }

    @Test
    public void setPickupTime() {
        testDonation.setPickupTime("test2Time");
        assertEquals("Testing setPickupTime() method", "test2Time", testDonation.getPickupTime());
    }

    @Test
    public void getDropoffEndTime() {
        testDonation.setDropoffEndTime("test3Time");
        assertEquals("Testing getDropoffEndTime() method", "test3Time", testDonation.getDropoffEndTime());
    }

    @Test
    public void setDropoffEndTime() {
        testDonation.setDropoffEndTime("test4Time");
        assertEquals("Testing setDropoffEndTime() method", "test4Time", testDonation.getDropoffEndTime());
    }

    @Test
    public void equals() {
        Donation equalDonation = testDonation;
        assertEquals("Testing equals() method", testDonation, equalDonation);
    }
}
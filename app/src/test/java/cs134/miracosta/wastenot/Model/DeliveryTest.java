package cs134.miracosta.wastenot.Model;

import org.junit.Before;
import org.junit.Test;

import cs134.miracosta.wastenot.Model.Enums.FoodType;

import static org.junit.Assert.*;

public class DeliveryTest {

    private Delivery testDelivery;
    private User testUser;
    private Donation testDonation;

    @Before
    public void setUp() throws Exception {
        testDelivery = new Delivery();
        testUser = new User("donor", "Will", "C",
                "wcraycroft@gmail.com", "Will's Company", new Location());
        testDonation = new Donation(FoodType.OTHER, 10, true,
                "", "12", "11");
    }

    @Test
    public void getMakesKey() {
        testDelivery.setMakesKey("testKey");
        assertEquals("Testing getMakesKey() method", "testKey", testDelivery.getMakesKey());
    }

    @Test
    public void setMakesKey() {
        testDelivery.setMakesKey("testKey");
        assertEquals("Testing setMakesKey() method", "testKey", testDelivery.getMakesKey());
    }

    @Test
    public void getDriver() {
        testDelivery.setDriver(testUser);
        assertEquals("Testing getDriver() method", testUser, testDelivery.getDriver());
    }

    @Test
    public void setDriver() {
        testDelivery.setDriver(testUser);
        assertEquals("Testing setDriver() method", testUser, testDelivery.getDriver());
    }

    @Test
    public void getDonation() {
        testDelivery.setDonation(testDonation);
        assertEquals("Testing getDonation() method", testDonation, testDelivery.getDonation());
    }

    @Test
    public void setDonation() {
        testDelivery.setDonation(testDonation);
        assertEquals("Testing setDonation() method", testDonation, testDelivery.getDonation());
    }

    @Test
    public void getDonor() {
        testDelivery.setDonor(testUser);
        assertEquals("Testing getDonor() method", testUser, testDelivery.getDonor());
    }

    @Test
    public void setDonor() {
        testDelivery.setDonor(testUser);
        assertEquals("Testing setDonor() method", testUser, testDelivery.getDonor());
    }

    @Test
    public void getClaimer() {
        testDelivery.setClaimer(testUser);
        assertEquals("Testing getClaimer() method", testUser, testDelivery.getClaimer());
    }

    @Test
    public void setClaimer() {
        testDelivery.setClaimer(testUser);
        assertEquals("Testing setClaimer() method", testUser, testDelivery.getClaimer());
    }

    @Test
    public void isComplete() {
        testDelivery.setDonor(testUser);
        testDelivery.setClaimer(testUser);
        testDelivery.setDonation(testDonation);
        assertTrue("Testing isComplete() method", testDelivery.isComplete());
    }

    @Test
    public void equals() {
        Delivery equalsTest = testDelivery;
        assertEquals("Testing equals() method", testDelivery, equalsTest);
    }
}
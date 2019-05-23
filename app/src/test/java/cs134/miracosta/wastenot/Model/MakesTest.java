package cs134.miracosta.wastenot.Model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MakesTest {

    private Makes testMakes;

    @Before
    public void setUp() throws Exception {
        testMakes = new Makes("donationKey", "donorKey");
        testMakes.setKey("makesKey");
        testMakes.setDriverKey("driverKey");
        testMakes.setClaimerKey("claimerKey");
    }

    @Test
    public void getKey() {
        assertEquals("Testing getKey() method", "makesKey", testMakes.getKey());
    }

    @Test
    public void setKey() {
        testMakes.setKey("testKey");
        assertEquals("Testing getKey() method", "testKey", testMakes.getKey());
    }

    @Test
    public void getDonationKey() {
        assertEquals("Testing getDonationKey() method", "donationKey", testMakes.getDonationKey());
    }

    @Test
    public void setDonationKey() {
        testMakes.setDonationKey("testKey");
        assertEquals("Testing getDonationKey() method", "testKey", testMakes.getDonationKey());
    }

    @Test
    public void getDonorKey() {
        assertEquals("Testing getDonorKey() method", "donorKey", testMakes.getDonorKey());
    }

    @Test
    public void setDonorKey() {
        testMakes.setDonorKey("testKey");
        assertEquals("Testing getDonorKey() method", "testKey", testMakes.getDonorKey());
    }

    @Test
    public void getClaimerKey() {
        assertEquals("Testing getClaimerKey() method", "claimerKey", testMakes.getClaimerKey());
    }

    @Test
    public void setClaimerKey() {
        testMakes.setClaimerKey("testKey");
        assertEquals("Testing getClaimerKey() method", "testKey", testMakes.getClaimerKey());
    }

    @Test
    public void getDriverKey() {
        assertEquals("Testing getDriverKey() method", "driverKey", testMakes.getDriverKey());
    }

    @Test
    public void setDriverKey() {
        testMakes.setDriverKey("testKey");
        assertEquals("Testing getDriverKey() method", "testKey", testMakes.getDriverKey());
    }
}
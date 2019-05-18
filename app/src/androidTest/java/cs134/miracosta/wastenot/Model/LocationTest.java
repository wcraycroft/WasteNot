package cs134.miracosta.wastenot.Model;

import org.junit.Assert;
import org.junit.Test;

/**
 * @CreatedBy Ahmad Abbasi
 * Synavos Solutions
 * Author Email: ahmad.adil.abbasi@gmail.com
 * Created on: 2019-05-17
 */
public class LocationTest
{
   Location location = new Location();

    /**
     * test case to check location's address
     */
    @Test
    public void testAddress()
    {
        String expected = "123 Fake St";
        location.setAddress(expected);
        String actual = location.getAddress();

        Assert.assertEquals(expected, actual);
    }

    /**
     * test case to check location's city
     */
    @Test
    public void testCity()
    {
        String expected = "Long Island";
        location.setCity(expected);
        String actual = location.getCity();

        Assert.assertEquals(expected, actual);
    }

    /**
     * test case to check location's state
     */
    @Test
    public void tesState()
    {
        String expected = "NY";
        location.setState(expected);
        String actual = location.getState();

        Assert.assertEquals(expected, actual);
    }

    /**
     * test case to check location's zipCode
     */
    @Test
    public void testZipCode()
    {
        String expected = "51611";
        location.setZipCode(expected);
        String actual = location.getZipCode();

        Assert.assertEquals(expected, actual);
    }

    /**
     * test case to check location's latitude
     */
    @Test
    public void testLatitude()
    {
        double expected = 40.753453;
        location.setLatitude(expected);
        double actual = location.getLatitude();

        Assert.assertEquals(expected, actual,0.0);
    }

    /**
     * test case to check location's longitude
     */
    @Test
    public void getLongitude()
    {
        double expected = -73.936400;
        location.setLongitude(expected);
        double actual = location.getLongitude();

        Assert.assertEquals(expected, actual,0.0);
    }
}
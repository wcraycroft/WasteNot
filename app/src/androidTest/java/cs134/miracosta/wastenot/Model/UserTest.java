package cs134.miracosta.wastenot.Model;

import org.junit.Assert;
import org.junit.Test;

/**
 * @CreatedBy Ahmad Abbasi
 * Synavos Solutions
 * Author Email: ahmad.adil.abbasi@gmail.com
 * Created on: 2019-05-17
 */
public class UserTest
{
 User user = new User();

    /**
     * test case to check user type
     */
    @Test
    public void testUserType()
    {
       String expected = "Donor";
       user.setUserType(expected);
       String actual = user.getUserType();

       Assert.assertEquals(expected, actual);
    }

    /**
     * test case to check user key
     */
    @Test
    public void testUserKey()
    {
        String expected = "key";
        user.setKey(expected);
        String actual = user.getKey();

        Assert.assertEquals(expected, actual);
    }

    /**
     * test case to check user's first name
     */
    @Test
    public void testUserFirstName()
    {
        String expected = "Ahmad";
        user.setFirstName(expected);
        String actual = user.getFirstName();

        Assert.assertEquals(expected, actual);
    }

    /**
     * test case to check user's last name
     */
    @Test
    public void testUserLastName()
    {
        String expected = "Abbasi";
        user.setLastName(expected);
        String actual = user.getLastName();

        Assert.assertEquals(expected, actual);
    }

    /**
     * test case to check user's email
     */
    @Test
    public void testUserEmail()
    {
        String expected = "ahmad.adil.abbasi@gmail.com";
        user.setEmail(expected);
        String actual = user.getEmail();

        Assert.assertEquals(expected, actual);
    }

    /**
     * test case to check user's company name
     */
    @Test
    public void testUserCompanyName()
    {
        String expected = "ahmad.adil.abbasi@gmail.com";
        user.setCompanyName(expected);
        String actual = user.getCompanyName();

        Assert.assertEquals(expected, actual);
    }
}
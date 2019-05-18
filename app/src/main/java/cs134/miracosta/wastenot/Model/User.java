//TODO: For testing purposes, feel free to replace with your own code


package cs134.miracosta.wastenot.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable
{

    private String mKey;
    private String userType;
    private String mFirstName;
    private String mLastName;
    private String mEmail;
    private String mPassword;
    private String mCompanyName;
    private Location mLocation;

    public User(String mKey, String userType, String mFirstName, String mLastName, String mEmail, String mPassword, String mCompanyName, Location mLocation)
    {
        this.mKey = mKey;
        this.userType = userType;
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
        this.mEmail = mEmail;
        this.mPassword = mPassword;
        this.mCompanyName = mCompanyName;
        this.mLocation = mLocation;
    }



    public User() {
    }

    public String getmKey()
    {
        return mKey;
    }

    public void setmKey(String mKey)
    {
        this.mKey = mKey;
    }

    public String getmFirstName()
    {
        return mFirstName;
    }

    public void setmFirstName(String mFirstName)
    {
        this.mFirstName = mFirstName;
    }

    public String getmLastName()
    {
        return mLastName;
    }

    public void setmLastName(String mLastName)
    {
        this.mLastName = mLastName;
    }

    public String getmEmail()
    {
        return mEmail;
    }

    public void setmEmail(String mEmail)
    {
        this.mEmail = mEmail;
    }

    public String getmCompanyName()
    {
        return mCompanyName;
    }

    public void setmCompanyName(String mCompanyName)
    {
        this.mCompanyName = mCompanyName;
    }

    public Location getmLocation()
    {
        return mLocation;
    }

    public void setmLocation(Location mLocation)
    {
        this.mLocation = mLocation;
    }

    public User(String userType, String firstName, String lastName, String email, String mPassword, Location location) {
        this.userType = userType;
        mFirstName = firstName;
        mLastName = lastName;
        mEmail = email;
        mCompanyName = email;
        this.mPassword = mPassword;
        mLocation = location;
    }

    public String getUserType()
    {
        return userType;
    }

    public void setUserType(String userType)
    {
        this.userType = userType;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getCompanyName() {
        return mCompanyName;
    }

    public void setCompanyName(String companyName) {
        mCompanyName = companyName;
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location location) {
        mLocation = location;
    }

    public String getmPassword()
    {
        return mPassword;
    }

    public void setmPassword(String mPassword)
    {
        this.mPassword = mPassword;
    }

    @Override
    public String toString() {
        return "User{" +
                "mKey='" + mKey + '\'' +
                ", mFirstName='" + mFirstName + '\'' +
                ", mLastName='" + mLastName + '\'' +
                ", mEmail='" + mEmail + '\'' +
                ", mCompanyName='" + mCompanyName + '\'' +
                ", mLocation=" + mLocation +
                '}';
    }
}

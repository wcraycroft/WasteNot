//TODO: For testing purposes, feel free to replace with your own code


package cs134.miracosta.wastenot.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class User implements Parcelable {

    private String mKey;
    private String mFirstName;
    private String mLastName;
    private String mEmail;
    private String mCompanyName;
    private Location mLocation;

    public User() {
    }

    public User(String key, String firstName, String lastName, String email, String companyName, Location location) {
        mKey = key;
        mFirstName = firstName;
        mLastName = lastName;
        mEmail = email;
        mCompanyName = companyName;
        mLocation = location;
    }

    // TODO: Parcelable constructor

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(mKey, user.mKey) &&
                Objects.equals(mFirstName, user.mFirstName) &&
                Objects.equals(mLastName, user.mLastName) &&
                Objects.equals(mEmail, user.mEmail) &&
                Objects.equals(mCompanyName, user.mCompanyName) &&
                Objects.equals(mLocation, user.mLocation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mKey, mFirstName, mLastName, mEmail, mCompanyName, mLocation);
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

    @Override
    public int describeContents() {
        return 0;
    }

    // TODO
    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}

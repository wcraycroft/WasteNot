

package cs134.miracosta.wastenot.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Ahmad Abbasi
 */
public class User implements Parcelable
{

    private String mKey = "";
    private String userType;
    private String mFirstName;
    private String mLastName;
    private String mEmail;
    private String mCompanyName;
    private Location mLocation;


    public User() {
    }


    public User(String userType, String firstName, String lastName, String email, String companyName, Location location) {
        this.userType = userType;
        mFirstName = firstName;
        mLastName = lastName;
        mEmail = email;
        mCompanyName = companyName;
        //this.mPassword = mPassword;
        mLocation = location;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(mKey);
        dest.writeString(userType);
        dest.writeString(mFirstName);
        dest.writeString(mLastName);
        dest.writeString(mEmail);
        dest.writeString(mCompanyName);

    }

    private User(Parcel parcel)
    {
        mKey = parcel.readString();
        userType = parcel.readString();
        mFirstName = parcel.readString();
        mLastName = parcel.readString();
        mEmail = parcel.readString();
        mCompanyName = parcel.readString();
    }
}

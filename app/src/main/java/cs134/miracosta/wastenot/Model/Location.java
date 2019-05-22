package cs134.miracosta.wastenot.Model;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * The <code>Location</code> class represents the location of a Donor or Claimer. It will attempt to
 * find the location's latitude and longitude using Geocoder.
 *
 * @author Will Craycroft
 * @author Ahmad Abbasi
 */
public class Location implements Parcelable {

    private Context mContext;
    private String mAddress;
    private String mCity;
    private String mState;
    private String mZipCode;
    private double mLatitude;
    private double mLongitude;

    /**
     * Default constructor. Used by CloudFirestore POJO builder to created a Location object
     * from database.
     */
    public Location() {

    }

    /**
     * Returns the Parcelable Creator for this class.
     * @return - the Location <code>Creator</code> object
     */
    public static Creator<Location> getCREATOR()
    {
        return CREATOR;
    }

    // This constructor will use the passed in context to set the latitude and longitude using Geocoder

    /**
     * Creates a new Location instance, set all fields, and attempts to find the latitude and logitude
     * for this location.
     *
     * @param context - the calling activity's context. Needed to call Geocoder's LatLng lookup.
     * @param address - the street address
     * @param city - the City
     * @param state - the State
     * @param zipCode - the Zip Code
     */
    public Location(Context context, String address, String city, String state, String zipCode) {
        mContext = context;
        mAddress = address;
        mCity = city;
        mState = state;
        mZipCode = zipCode;
        setLatLng(context);
    }

    /**
     * Parcelable constructor. Builds a Location object from the passed Parcel.
     * @param in - the Parcel containing Location information
     */
    protected Location(Parcel in) {
        mAddress = in.readString();
        mCity = in.readString();
        mState = in.readString();
        mZipCode = in.readString();
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
    }

    /**
     * Returns the street address of this Location
     * @return - the street address
     */
    public String getAddress() {
        return mAddress;
    }

    /**
     * Sets the street address of this Location
     * @param address = the street address
     */
    public void setAddress(String address) {
        mAddress = address;
    }

    /**
     * Returns the city of this Location
     * @return - the city
     */
    public String getCity() {
        return mCity;
    }

    /**
     * Sets the Location's city
     * @param city - the city
     */
    public void setCity(String city) {
        mCity = city;
    }

    /**
     * Returns the Location's state (abbreviated or full)
     * @return - the State
     */
    public String getState() {
        return mState;
    }

    /**
     * Sets the Location's state (abbreviated or full)
     * @param state - the State
     */
    public void setState(String state) {
        mState = state;
    }

    /**
     * Returns the Location's zip code
     * @return - the zip code
     */
    public String getZipCode() {
        return mZipCode;
    }

    public void setZipCode(String zipCode) {
        mZipCode = zipCode;
    }

    /**
     * Returns the locations Latitude
     * @return - latitude
     */
    public double getLatitude() {
        return mLatitude;
    }

    /**
     * Sets the Location's latitude
     * @param latitude - the latitude
     */
    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    /**
     * Returns the Location's longitude
     * @return
     */
    public double getLongitude() {
        return mLongitude;
    }

    /**
     * Sets the Location's longitude
     * @param longitude - the longitude
     */
    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    /**
     * Returns a String representing the full address. Safe to use with Geocoder.
     * @return - the Full Address
     */
    public String getFullAddress()
    {
        return mAddress + "\n" + mCity + ", " + mState + "  " + mZipCode;
    }


    /**
     * Helper method uses google API to find an address' latitude and longitude
     * Note this requires a valid Context so it has to be called externally or pass it a context through
     * the appropriate constructor.
     * @param c - the Activity context used to instantiate Geocoder
     */
    public void setLatLng(Context c)
    {
        Geocoder geocoder = new Geocoder(c, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocationName(getFullAddress(), 1);
            if (addresses.size() == 0)
            {
                Log.w("WasteNot Location", "Unable to resolve address.");
                return;
            }
            Address address = addresses.get(0);
            mLatitude = address.getLatitude();
            mLongitude = address.getLongitude();
            Log.i("WasteNot Location", "LatLng updated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a String representation of the Location object.
     * @return - String representation of the Location object.
     */
    @Override
    public String toString() {
        return "Location{" +
                ", Address='" + mAddress + '\'' +
                ", City='" + mCity + '\'' +
                ", State='" + mState + '\'' +
                ", Zip Code='" + mZipCode + '\'' +
                ", Latitude=" + mLatitude +
                ", Longitude=" + mLongitude +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes all field data to a passed Parcel.
     * @param parcel - the destination Parcel
     * @param i - Parcel code (not used)
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mAddress);
        parcel.writeString(mCity);
        parcel.writeString(mState);
        parcel.writeString(mZipCode);
        parcel.writeDouble(mLatitude);
        parcel.writeDouble(mLongitude);
    }

    /**
     * Overrides the Creator's create method to send it a Location object
     */
    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}

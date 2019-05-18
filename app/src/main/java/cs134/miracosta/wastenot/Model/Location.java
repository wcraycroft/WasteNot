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
 * The <code>Location</code> class represents a place where one can get a caffeine fix, including
 * its name, address, phone number and latitude/longitude location.
 *
 * @author Will Craycroft
 */

public class Location implements Parcelable {
    private Context mContext;
    private String mAddress;
    private String mCity;
    private String mState;
    private String mZipCode;
    private double mLatitude;
    private double mLongitude;

    public Location() {

    }



    public Location(Context mContext, String mAddress, String mCity, String mState, String mZipCode, double mLatitude, double mLongitude)
    {
        this.mContext = mContext;
        this.mAddress = mAddress;
        this.mCity = mCity;
        this.mState = mState;
        this.mZipCode = mZipCode;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
    }

    public Context getmContext()
    {
        return mContext;
    }

    public void setmContext(Context mContext)
    {
        this.mContext = mContext;
    }

    public String getmAddress()
    {
        return mAddress;
    }

    public void setmAddress(String mAddress)
    {
        this.mAddress = mAddress;
    }

    public String getmCity()
    {
        return mCity;
    }

    public void setmCity(String mCity)
    {
        this.mCity = mCity;
    }

    public String getmState()
    {
        return mState;
    }

    public void setmState(String mState)
    {
        this.mState = mState;
    }

    public String getmZipCode()
    {
        return mZipCode;
    }

    public void setmZipCode(String mZipCode)
    {
        this.mZipCode = mZipCode;
    }

    public double getmLatitude()
    {
        return mLatitude;
    }

    public void setmLatitude(double mLatitude)
    {
        this.mLatitude = mLatitude;
    }

    public double getmLongitude()
    {
        return mLongitude;
    }

    public void setmLongitude(double mLongitude)
    {
        this.mLongitude = mLongitude;
    }

    public static Creator<Location> getCREATOR()
    {
        return CREATOR;
    }

    // TODO: delete this constructor if we don't end up using it
    public Location(String address, String city, String state, String zipCode) {
        mAddress = address;
        mCity = city;
        mState = state;
        mZipCode = zipCode;
        mLatitude = 0.0;
        mLongitude = 0.0;
    }

    // This constructor will use the passed in context to set the latitude and longitude using Geocoder
    public Location(Context context, String address, String city, String state, String zipCode) {
        mContext = context;
        mAddress = address;
        mCity = city;
        mState = state;
        mZipCode = zipCode;
        setLatLng(context);
    }

    protected Location(Parcel in) {
        mAddress = in.readString();
        mCity = in.readString();
        mState = in.readString();
        mZipCode = in.readString();
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    public String getZipCode() {
        return mZipCode;
    }

    public void setZipCode(String zipCode) {
        mZipCode = zipCode;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public String getFullAddress()
    {
        return mAddress + "\n" + mCity + ", " + mState + "  " + mZipCode;
    }

    public String getFormattedLatLng()
    {
        String latLng = String.valueOf(Math.abs(mLatitude));
        latLng += ((mLatitude < 0.0) ? " S  " : " N  ");
        latLng += String.valueOf(Math.abs(mLongitude));
        latLng += ((mLongitude < 0.0) ? " W" : "E");
        return latLng;
    }

    /**
     * Helper method uses google API to convert address into LatLng
     * Note this requires a Context so it has to be called externally or pass it a context through
     * the appropriate constructor
     * TODO: figure out which context is safe to use here
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

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mAddress);
        parcel.writeString(mCity);
        parcel.writeString(mState);
        parcel.writeString(mZipCode);
        parcel.writeDouble(mLatitude);
        parcel.writeDouble(mLongitude);
    }

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

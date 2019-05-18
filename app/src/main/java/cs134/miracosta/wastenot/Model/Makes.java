
// Model for the relationship database linking Users and Donations

package cs134.miracosta.wastenot.Model;

import java.util.Objects;

public class Makes {

    private String mKey;
    private String mDonationKey;
    private String mDonorKey;
    private String mClaimerKey;
    private String mDriverKey;

    public Makes(String donationKey, String donorKey) {
        mKey = "";
        mDonationKey = donationKey;
        mDonorKey = donorKey;
        mClaimerKey = "";
        mDriverKey = "";
    }

    public Makes() {
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getDonationKey() {
        return mDonationKey;
    }

    public void setDonationKey(String donationKey) {
        mDonationKey = donationKey;
    }

    public String getDonorKey() {
        return mDonorKey;
    }

    public void setDonorKey(String donorKey) {
        mDonorKey = donorKey;
    }

    public String getClaimerKey() {
        return mClaimerKey;
    }

    public void setClaimerKey(String claimerKey) {
        mClaimerKey = claimerKey;
    }

    public String getDriverKey() {
        return mDriverKey;
    }

    public void setDriverKey(String driverKey) {
        mDriverKey = driverKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Makes makes = (Makes) o;
        return Objects.equals(mKey, makes.mKey) &&
                Objects.equals(mDonationKey, makes.mDonationKey) &&
                Objects.equals(mDonorKey, makes.mDonorKey) &&
                Objects.equals(mClaimerKey, makes.mClaimerKey) &&
                Objects.equals(mDriverKey, makes.mDriverKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mKey, mDonationKey, mDonorKey, mClaimerKey, mDriverKey);
    }

    @Override
    public String toString() {
        return "Makes{" +
                "mKey='" + mKey + '\'' +
                ", mDonationKey='" + mDonationKey + '\'' +
                ", mDonorKey='" + mDonorKey + '\'' +
                ", mClaimerKey='" + mClaimerKey + '\'' +
                ", mDriverKey='" + mDriverKey + '\'' +
                '}';
    }
}

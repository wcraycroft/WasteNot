//TODO: This is just some test fields to test the FirebaseDBHelper, please replace with actual fields.

package cs134.miracosta.wastenot.Model;

public class Claimer extends User {

    private String mName;
    private Location mLocation;

    public Claimer() {
    }

    public Claimer(String key, String type, String name, Location location) {
        super(key, type);
        mName = name;
        mLocation = location;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location location) {
        mLocation = location;
    }

    @Override
    public String toString() {
        return "Claimer{" +
                "mKey='" + super.getKey() + '\'' +
                ", mType='" + super.getType() + '\'' +
                ", mName='" + mName + '\'' +
                ", mLocation=" + mLocation +
                '}';
    }
}

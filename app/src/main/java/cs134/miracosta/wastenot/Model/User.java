//TODO: This is just some test fields to test the FirebaseDBHelper, please replace with actual fields.


package cs134.miracosta.wastenot.Model;

public class User {

    private String mKey;
    private String mType;

    public User() {
    }

    public User(String key, String type) {
        mKey = key;
        mType = type;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    @Override
    public String toString() {
        return "User{" +
                "mKey='" + mKey + '\'' +
                ", mType='" + mType + '\'' +
                '}';
    }
}

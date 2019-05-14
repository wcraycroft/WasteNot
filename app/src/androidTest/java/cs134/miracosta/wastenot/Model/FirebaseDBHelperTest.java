package cs134.miracosta.wastenot.Model;

import com.google.firebase.FirebaseApp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cs134.miracosta.wastenot.UI.MainActivity;

import static org.junit.Assert.*;

public class FirebaseDBHelperTest {

    FirebaseDBHelper testDB;
    Context mContext;

    @BeforeClass
    public void setUp() {
        mContext = InstrumentationRegistry.getTargetContext();
        FirebaseApp.initializeApp(mContext);
        testDB = new FirebaseDBHelper();
    }


    @Test
    public void addUser() {

    }
}
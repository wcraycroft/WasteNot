package cs134.miracosta.wastenot.Model;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import cs134.miracosta.wastenot.Model.Enums.DonationStatus;
import cs134.miracosta.wastenot.Model.Enums.FoodType;

import static org.junit.Assert.*;

public class FirebaseDBHelperTest {

    private static FirebaseDBHelper db;

    private Delivery testDelivery;
    private Donation testDonation;
    private User testUser;

    @BeforeClass
    public void initSetUp() throws Exception {
        db = new FirebaseDBHelper();
    }

    @Before
    public void setUp() throws Exception {
        testDelivery = new Delivery();
        testUser = new User("donor", "Firebase", "Test",
                "firebasetest@gmail.com", "Will's Company", new Location());
        testDonation = new Donation(FoodType.OTHER, 10, true,
                "", "12", "11");
    }

    @After
    public void takeDown() throws Exception {

    }

    @Test
    public void addUser() {
        final String email = testUser.getEmail();
        db.addUser(testUser, new FirebaseDBHelper.DataStatus() {
            @Override
            public void DataIsRead(List<?> items) { }

            @Override
            public void DataIsProcessed() {
                db.getUserByEmail(email, new FirebaseDBHelper.DataStatus() {
                    @Override
                    public void DataIsRead(List<?> items) {
                        User outputUser = (User) items.get(0);
                        assertEquals("Testing addUser() method", testUser, outputUser);
                    }

                    @Override
                    public void DataIsProcessed() { }
                    @Override
                    public void onError(String errorMessage) { }
                });
            }

            @Override
            public void onError(String errorMessage) { }
        });
    }

    @Test
    public void getUserByKey() {
        final String key = "testKey";
        db.addUser(testUser, new FirebaseDBHelper.DataStatus() {
            @Override
            public void DataIsRead(List<?> items) { }

            @Override
            public void DataIsProcessed() {
                db.getUserByKey(testUser.getKey(), new FirebaseDBHelper.DataStatus() {
                    @Override
                    public void DataIsRead(List<?> items) {
                        User outputUser = (User) items.get(0);
                        assertEquals("Testing getUserByKey() method", testUser, outputUser);
                    }

                    @Override
                    public void DataIsProcessed() { }
                    @Override
                    public void onError(String errorMessage) { }
                });
            }

            @Override
            public void onError(String errorMessage) { }
        });
    }

    @Test
    public void getUserByEmail() {
        final String email = testUser.getEmail();
        db.addUser(testUser, new FirebaseDBHelper.DataStatus() {
            @Override
            public void DataIsRead(List<?> items) { }

            @Override
            public void DataIsProcessed() {
                db.getUserByEmail(email, new FirebaseDBHelper.DataStatus() {
                    @Override
                    public void DataIsRead(List<?> items) {
                        User outputUser = (User) items.get(0);
                        assertEquals("Testing getUserByEmail() method", testUser, outputUser);
                    }

                    @Override
                    public void DataIsProcessed() { }
                    @Override
                    public void onError(String errorMessage) { }
                });
            }

            @Override
            public void onError(String errorMessage) { }
        });
    }

    @Test
    public void addDonation() {
        db.addDonation(testDonation, testUser.getKey(), new FirebaseDBHelper.DataStatus() {
            @Override
            public void DataIsRead(List<?> items) {
            }

            @Override
            public void DataIsProcessed() {
                db.getDonation(testDonation.getKey(), new FirebaseDBHelper.DataStatus() {
                    @Override
                    public void DataIsRead(List<?> items) {
                        Donation outputDonation = (Donation) items.get(0);
                        assertEquals("Testing addDonation() method.", testDonation, outputDonation);
                    }

                    @Override
                    public void DataIsProcessed() {
                    }

                    @Override
                    public void onError(String errorMessage) {
                    }
                });
            }

            @Override
            public void onError(String errorMessage) { }
        });
    }

    @Test
    public void claimDonation() {
        db.claimDonation(testDonation, testUser.getKey(), new FirebaseDBHelper.DataStatus() {

            @Override
            public void DataIsRead(List<?> items) { }

            @Override
            public void DataIsProcessed() {
                db.getDonation(testDonation.getKey(), new FirebaseDBHelper.DataStatus() {
                    @Override
                    public void DataIsRead(List<?> items) {
                        Donation outputDonation = (Donation) items.get(0);
                        assertEquals("Testing addDonation() method.", DonationStatus.DONATION_CLAIMED, outputDonation.getStatus());
                    }

                    @Override
                    public void DataIsProcessed() { }
                    @Override
                    public void onError(String errorMessage) { }
                });
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    @Test
    public void claimDelivery() {
        db.claimDonation(testDonation, testUser.getKey(), new FirebaseDBHelper.DataStatus() {

            @Override
            public void DataIsRead(List<?> items) { }

            @Override
            public void DataIsProcessed() {
                db.getUserDeliveries(testUser.getKey(), new FirebaseDBHelper.DataStatus() {
                    @Override
                    public void DataIsRead(List<?> items) {
                        Delivery outputDelivery = (Delivery) items.get(0);
                        assertEquals("Testing claimDelivery() method.",
                                DonationStatus.DELIVERY_CLAIMED, outputDelivery.getDonation().getStatus());
                    }

                    @Override
                    public void DataIsProcessed() { }
                    @Override
                    public void onError(String errorMessage) { }
                });
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    @Test
    public void getDonation()
    {
        db.claimDonation(testDonation, testUser.getKey(), new FirebaseDBHelper.DataStatus() {

            @Override
            public void DataIsRead(List<?> items) { }

            @Override
            public void DataIsProcessed() {
                db.getDonation(testDonation.getKey(), new FirebaseDBHelper.DataStatus() {
                    @Override
                    public void DataIsRead(List<?> items) {
                        Donation outputDonation = (Donation) items.get(0);
                        assertEquals("Testing addDonation() method.", DonationStatus.DONATION_CLAIMED, outputDonation.getStatus());
                    }

                    @Override
                    public void DataIsProcessed() { }
                    @Override
                    public void onError(String errorMessage) { }
                });
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    @Test
    public void getDonationsByUser() {
        db.claimDonation(testDonation, testUser.getKey(), new FirebaseDBHelper.DataStatus() {

            @Override
            public void DataIsRead(List<?> items) { }

            @Override
            public void DataIsProcessed() {
                db.getDonationsByUser(testUser.getKey(), new FirebaseDBHelper.DataStatus() {
                    @Override
                    public void DataIsRead(List<?> items) {
                        Donation outputDonation = (Donation) items.get(0);
                        assertEquals("Testing getDonationsByUser() method.", DonationStatus.DONATION_CLAIMED, outputDonation.getStatus());
                    }

                    @Override
                    public void DataIsProcessed() { }
                    @Override
                    public void onError(String errorMessage) { }
                });
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    @Test
    public void getDonationsByStatus() {
        testDonation.setStatus(DonationStatus.DONATION_CLAIMED);
        db.addDonation(testDonation, testUser.getKey(), new FirebaseDBHelper.DataStatus() {
            @Override
            public void DataIsRead(List<?> items) {
            }

            @Override
            public void DataIsProcessed() {
                db.getDonationsByStatus(testDonation.getStatus(), new FirebaseDBHelper.DataStatus() {
                    @Override
                    public void DataIsRead(List<?> items) {
                        Donation outputDonation = (Donation) items.get(0);
                        assertEquals("Testing getDonationsByStatus() method.",
                                testDonation.getStatus(), outputDonation.getStatus());
                    }

                    @Override
                    public void DataIsProcessed() {
                    }

                    @Override
                    public void onError(String errorMessage) {
                    }
                });
            }

            @Override
            public void onError(String errorMessage) { }
        });

    }

    @Test
    public void getAllDeliveries()
    {
        db.addDonation(testDonation, testUser.getKey(), new FirebaseDBHelper.DataStatus() {
            @Override
            public void DataIsRead(List<?> items) {
            }

            @Override
            public void DataIsProcessed() {
                db.getAllDeliveries(new FirebaseDBHelper.DataStatus() {
                    @Override
                    public void DataIsRead(List<?> items) {
                        Donation outputDonation = (Donation) items.get(0);
                        assertEquals("Testing getAllDeliveries() method.",
                                testDonation, outputDonation);
                    }

                    @Override
                    public void DataIsProcessed() {
                    }

                    @Override
                    public void onError(String errorMessage) {
                    }
                });
            }

            @Override
            public void onError(String errorMessage) { }
        });
    }

    @Test
    public void getUserDeliveries() {
        db.addDonation(testDonation, testUser.getKey(), new FirebaseDBHelper.DataStatus() {
            @Override
            public void DataIsRead(List<?> items) {
            }

            @Override
            public void DataIsProcessed() {
                db.getUserDeliveries(testUser.getKey(), new FirebaseDBHelper.DataStatus() {
                    @Override
                    public void DataIsRead(List<?> items) {
                        Delivery outputDelivery = (Delivery) items.get(0);
                        assertEquals("Testing getUserDeliveries() method.",
                                testDonation, outputDelivery.getDonation());
                    }

                    @Override
                    public void DataIsProcessed() {
                    }

                    @Override
                    public void onError(String errorMessage) {
                    }
                });
            }

            @Override
            public void onError(String errorMessage) { }
        });
    }

}
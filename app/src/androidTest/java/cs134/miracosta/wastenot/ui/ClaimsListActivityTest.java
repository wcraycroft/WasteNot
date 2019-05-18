package cs134.miracosta.wastenot.ui;

import android.support.design.widget.FloatingActionButton;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cs134.miracosta.wastenot.ExampleInstrumentedTest;
import cs134.miracosta.wastenot.R;
import cs134.miracosta.wastenot.UI.ClaimsListActivity;

import static junit.framework.TestCase.assertEquals;

/**
 * @CreatedBy Ahmad Abbasi
 * Synavos Solutions
 * Author Email: ahmad.adil.abbasi@gmail.com
 * Created on: 15/05/19
 */
@RunWith(AndroidJUnit4.class)
public class ClaimsListActivityTest extends ExampleInstrumentedTest
{
    @Rule
    public ActivityTestRule<ClaimsListActivity> mActivityRule = new ActivityTestRule<>(ClaimsListActivity.class);

    /**
     * Test case to check the size of claims list after a new entry
     * @throws Exception
     */
    @Test
    public void testForNewEntry() throws Exception
    {
        final ClaimsListActivity activity = mActivityRule.getActivity();

        final FloatingActionButton addButton = activity.findViewById(R.id.fabAddClaim);

        long itemsCount = activity.adapter.getItemCount();

        activity.runOnUiThread(new Runnable() {
                                   public void run() {
                                       addButton.performClick();
                                   }
                               });

        new CountDownLatch(1).await(10000, TimeUnit.MILLISECONDS);

        if(activity.findViewById(R.id.loader).getVisibility() != View.VISIBLE)
            assertEquals(itemsCount+1, activity.adapter.getItemCount());
    }
}
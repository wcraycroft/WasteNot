package cs134.miracosta.wastenot.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.MapFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

import cs134.miracosta.wastenot.Model.Donation;
import cs134.miracosta.wastenot.Model.Enums.DonationStatus;
import cs134.miracosta.wastenot.Model.FirebaseDBHelper;
import cs134.miracosta.wastenot.Model.User;
import cs134.miracosta.wastenot.R;
import cs134.miracosta.wastenot.UI.Adapters.DonationListAdapter;

public class DeliveryListFragment extends Fragment {

    public static final String TAG = "WasteNot";
    private ListView deliveriesListView;
    private View fragView;
    private Context mContext;
    FirebaseDBHelper db;
    List<Donation> allDeliveriesList;
    DonationListAdapter donationsListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, " test Entering onCreateView for List");
        // Inflate view
        fragView = inflater.inflate(R.layout.fragment_delivery_list, container, false);
        deliveriesListView = fragView.findViewById(R.id.deliveriesListView);
        Log.i(TAG, " test Inflated ListView");

        return fragView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "test Entering onAttach for List");

        mContext = context;

        // Instantiate DBHelper
        db = new FirebaseDBHelper();

        // Populate list of donations
        // TODO: make call update in realtime
        db.getDonationsByStatus(DonationStatus.DONATION_CLAIMED, new FirebaseDBHelper.DataStatus() {
            @Override
            public void DataIsRead(List<?> items) {
                allDeliveriesList = (List<Donation>) items;
                Log.i(TAG, "Data retrieved. Donations in list = " + allDeliveriesList.size());
                if (fragView != null) {
                    if (mContext != null) {
                        donationsListAdapter = new DonationListAdapter(mContext, R.layout.donation_list_item, allDeliveriesList);
                        deliveriesListView.setAdapter(donationsListAdapter);
                    } else {
                        Log.w(TAG, "Failed to link adapter (null Context)");
                    }
                } else
                    Log.i(TAG, "Failed to link adapter (null View)");
            }
            @Override
            public void DataIsProcessed() { }
            @Override
            public void onError(String errorMessage) {
                Toast.makeText(getActivity(),
                        "Error retrieving user information.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
    private void setListAdapter()
    {
        if (mContext != null) {
            donationsListAdapter = new DonationListAdapter(mContext, R.layout.donation_list_item, allDeliveriesList);
            deliveriesListView.setAdapter(donationsListAdapter);
            Log.i(TAG, " test Linked adapter");
        }
        else {
            Log.w(TAG, "Failed to link adapter (null context)");
        }
    }
    */

}

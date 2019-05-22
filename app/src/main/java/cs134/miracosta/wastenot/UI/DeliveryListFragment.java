package cs134.miracosta.wastenot.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import cs134.miracosta.wastenot.Model.Delivery;
import cs134.miracosta.wastenot.Model.FirebaseDBHelper;
import cs134.miracosta.wastenot.R;
import cs134.miracosta.wastenot.UI.Adapters.DeliveryListAdapter;

public class DeliveryListFragment extends Fragment {

    public static final String TAG = "WasteNot";
    private ListView deliveriesListView;
    private View fragView;
    private Context mContext;
    FirebaseDBHelper db;
    List<Delivery> allDeliveriesList;
    DeliveryListAdapter deliveryListAdapter;

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
        mContext = context;

        // Instantiate DBHelper
        db = new FirebaseDBHelper();

        // Populate list of donations
        // TODO: make call update in realtime
        db.getAllDeliveriesRealTime(new FirebaseDBHelper.DataStatus() {
            @Override
            public void DataIsRead(List<?> items) {
                allDeliveriesList = (List<Delivery>) items;
                Log.i(TAG, " Delivery List Data retrieved. Deliveries in list = " + allDeliveriesList.size());
                if (fragView != null) {
                    if (mContext != null) {
                        deliveryListAdapter = new DeliveryListAdapter(mContext, R.layout.list_item_delivery, allDeliveriesList);
                        deliveriesListView.setAdapter(deliveryListAdapter);
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
                Toast.makeText(mContext,
                        getString(R.string.error_retrieving_user_info), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

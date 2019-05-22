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

/**
 * This controller class handles the DeliveryListFragment , which show a list of all Deliveries
 * that are available for claim.
 *
 * @author Will Craycroft
 */
public class DeliveryListFragment extends Fragment {

    public static final String TAG = "WasteNot";
    private ListView deliveriesListView;
    private View fragView;
    private Context mContext;
    FirebaseDBHelper db;
    List<Delivery> allDeliveriesList;
    DeliveryListAdapter deliveryListAdapter;

    /**
     * Inflates the view.
     * @param inflater - Fragment LayoutInflater instance
     * @param container - Containing ViewGroup
     * @param savedInstanceState - Bundle data from previous instances (not used)
     * @return - Fragment's view once it has been inflated
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate view
        fragView = inflater.inflate(R.layout.fragment_delivery_list, container, false);
        deliveriesListView = fragView.findViewById(R.id.deliveriesListView);

        return fragView;
    }


    /**
     * This method is called when the Fragment is attached to DeliveryActivity (not onCreateView).
     * It provides a reference to the context needed for list adapters, toasts, etc.
     * @param context - the Context of the attached Activity (DeliveryActivity)
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

        // Instantiate DBHelper
        db = new FirebaseDBHelper();

        // Populate list of donations
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

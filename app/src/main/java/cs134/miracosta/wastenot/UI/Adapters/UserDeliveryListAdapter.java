package cs134.miracosta.wastenot.UI.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cs134.miracosta.wastenot.Model.Delivery;
import cs134.miracosta.wastenot.R;

/**
 * Helper class to provide custom adapter for the <code>Location</code> list.
 */
public class UserDeliveryListAdapter extends ArrayAdapter<Delivery> {

    public static final String TAG = "WasteNot";

    private Context mContext;
    private List<Delivery> mDeliveryList = new ArrayList<>();
    private int mResourceId;

    /**
     * Creates a new <code>DeliveryListAdapter</code> given a mContext, resource id and list of deliveries.
     *
     * @param c The mContext for which the adapter is being used (typically an activity)
     * @param rId The resource id (typically the layout file name)
     * @param deliveries The list of deliveries to display
     */
    public UserDeliveryListAdapter(Context c, int rId, List<Delivery> deliveries) {
        super(c, rId, deliveries);
        mContext = c;
        mResourceId = rId;
        mDeliveryList = deliveries;
    }

    /**
     * Gets the view associated with the layout.
     * @param pos The position of the Game selected in the list.
     * @param convertView The converted view.
     * @param parent The parent - ArrayAdapter
     * @return The new view with all content set.
     */
    @Override
    public View getView(int pos, View convertView, ViewGroup parent)
    {
        final Delivery selectedDelivery = mDeliveryList.get(pos);

        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(mResourceId, null);

        LinearLayout deliveryItemLinearLayout = view.findViewById(R.id.userDeliveryItemLinearLayout);
        ImageView deliveryItemImageView = view.findViewById(R.id.userDeliveryItemImageView);
        TextView deliveryItemPickupCompanyNameTextView = view.findViewById(R.id.userDeliveryItemPickupCompanyNameTextView);
        TextView deliveryItemFoodTypeTextView = view.findViewById(R.id.userDeliveryItemFoodTypeTextView);
        TextView deliveryItemPickupTimeTextView = view.findViewById(R.id.userDeliveryItemPickupTimeTextView);
        TextView deliveryItemDropoffCompanyNameTextView = view.findViewById(R.id.userDeliveryItemDropoffCompanyNameTextView);
        Button userDeliveryItemButton = view.findViewById(R.id.userDeliveryItemButton);

        // Set Tag as current Donation object if needed elsewhere (currently unused)
        deliveryItemLinearLayout.setTag(selectedDelivery);
        userDeliveryItemButton.setTag(selectedDelivery);

        deliveryItemPickupCompanyNameTextView.setText(selectedDelivery.getDonor().getCompanyName());
        deliveryItemDropoffCompanyNameTextView.setText(selectedDelivery.getClaimer().getCompanyName());
        deliveryItemPickupTimeTextView.setText(mContext.getResources().getString(
                R.string.pickup_by_prefix, selectedDelivery.getDonation().getPickupEndTime()));

        // Type switch cases
        String imageName = "image.png";
        switch (selectedDelivery.getDonation().getFoodType())
        {
            case MEAT:
                imageName = "meat.png";
                deliveryItemFoodTypeTextView.setText(R.string.meat);
                break;
            case DAIRY:
                imageName = "dairy.png";
                deliveryItemFoodTypeTextView.setText(R.string.dairy);
                break;
            case OTHER:
                imageName = "other.png";
                deliveryItemFoodTypeTextView.setText(R.string.other);
                break;
            case PRODUCE:
                imageName = "produce.png";
                deliveryItemFoodTypeTextView.setText(R.string.produce);
                break;
            case BAKED_GOODS:
                imageName = "baked.png";
                deliveryItemFoodTypeTextView.setText(R.string.baked);
                break;
            case PREPARED_TRAY:
                imageName = "tray.png";
                deliveryItemFoodTypeTextView.setText(R.string.packaged_tray);
                break;
            case PREPARED_PACKAGED:
                imageName = "packaged.png";
                deliveryItemFoodTypeTextView.setText(R.string.packaged_indiv);
                break;
        }

        // Set image drawable
        AssetManager am = mContext.getAssets();
        try {
            InputStream stream = am.open(imageName);
            Drawable event = Drawable.createFromStream(stream, imageName);
            deliveryItemImageView.setImageDrawable(event);
        }
        catch (IOException ex)
        {
            Log.e(TAG, "Error loading " + imageName, ex);
        }

        return view;
    }
}

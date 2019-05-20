package cs134.miracosta.wastenot.UI.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cs134.miracosta.wastenot.Model.Donation;
import cs134.miracosta.wastenot.R;

public class DonationListAdapter extends ArrayAdapter<Donation> {

    public static final String TAG = "WasteNot";

    private Context mContext;
    private List<Donation> mDonationsList = new ArrayList<>();
    private int mResourceId;

    /**
     * Creates a new <code>DonationsListAdapter</code> given a mContext, resource id and list of donations.
     *
     * @param c The mContext for which the adapter is being used (typically an activity)
     * @param rId The resource id (typically the layout file name)
     * @param donations The list of donations to display
     */
    public DonationListAdapter(Context c, int rId, List<Donation> donations) {
        super(c, rId, donations);
        mContext = c;
        mResourceId = rId;
        mDonationsList = donations;
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
        final Donation selectedDonation = mDonationsList.get(pos);

        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(mResourceId, null);

        LinearLayout donationListLinearLayout = view.findViewById(R.id.donationListLinearLayout);
        ImageView donationListImageView = view.findViewById(R.id.donationListImageView);
        TextView donationItemServingsTextView = view.findViewById(R.id.donationItemServingsTextView);
        TextView donationItemTypeTextView = view.findViewById(R.id.donationItemTypeTextView);
        TextView donationItemTimeTextView = view.findViewById(R.id.donationItemTimeTextView);
        TextView donationItemStatusTextView = view.findViewById(R.id.donationItemStatusTextView);

        // Set Tag as current Donation object if needed elsewhere (currently unused)
        donationListLinearLayout.setTag(selectedDonation);

        donationItemServingsTextView.setText(mContext.getResources().getString(
                R.string.servings_suffix, selectedDonation.getServings()));

        // Status switch cases
        switch (selectedDonation.getStatus())
        {
            case DONATION_CLAIMED:
                donationItemStatusTextView.setText(R.string.status_claimed);
                donationItemTimeTextView.setText(mContext.getResources().getString(
                        R.string.pickup_by_prefix, selectedDonation.getPickupEndTime()));
                donationItemStatusTextView.setTextColor(mContext.getResources().getColor(R.color.text_orange));
                break;
            case DELIVERY_CLAIMED:
                donationItemStatusTextView.setText(R.string.status_delivery_claimed);
                donationItemTimeTextView.setText(mContext.getResources().getString(
                        R.string.pickup_around_prefix, selectedDonation.getPickupTime()));
                donationItemStatusTextView.setTextColor(mContext.getResources().getColor(R.color.text_green));
                break;
            case UNCLAIMED:
                donationItemStatusTextView.setText(R.string.status_unclaimed);
                donationItemTimeTextView.setText(mContext.getResources().getString(
                        R.string.pickup_by_prefix, selectedDonation.getPickupEndTime()));
                donationItemStatusTextView.setTextColor(mContext.getResources().getColor(R.color.text_red));
                break;
        }

        // Type switch cases
        String imageName = "image.png";
        switch (selectedDonation.getFoodType())
        {
            case MEAT:
                imageName = "meat.png";
                donationItemTypeTextView.setText(R.string.meat);
                break;
            case DAIRY:
                imageName = "dairy.png";
                donationItemTypeTextView.setText(R.string.dairy);
                break;
            case OTHER:
                imageName = "other.png";
                donationItemTypeTextView.setText(R.string.other);
                break;
            case PRODUCE:
                imageName = "produce.png";
                donationItemTypeTextView.setText(R.string.produce);
                break;
            case BAKED_GOODS:
                imageName = "baked.png";
                donationItemTypeTextView.setText(R.string.baked);
                break;
            case PREPARED_TRAY:
                imageName = "tray.png";
                donationItemTypeTextView.setText(R.string.packaged_tray);
                break;
            case PREPARED_PACKAGED:
                imageName = "packaged.png";
                donationItemTypeTextView.setText(R.string.packaged_indiv);
                break;
        }

        // Set image drawable
        AssetManager am = mContext.getAssets();
        try {
            InputStream stream = am.open(imageName);
            Drawable event = Drawable.createFromStream(stream, imageName);
            donationListImageView.setImageDrawable(event);
        }
        catch (IOException ex)
        {
            Log.e(TAG, "Error loading " + imageName, ex);
        }

        return view;
    }
}

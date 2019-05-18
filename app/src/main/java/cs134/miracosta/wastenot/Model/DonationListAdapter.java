package cs134.miracosta.wastenot.Model;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
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

import cs134.miracosta.wastenot.R;

public class DonationListAdapter extends ArrayAdapter<Donation> {

    private Context mContext;
    private List<Donation> mDonationsList = new ArrayList<>();
    private int mResourceId;

    /**
     * Creates a new <code>GameListAdapter</code> given a mContext, resource id and list of donations.
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

        // TODO: determine which info to be displayed here
        /*
        LinearLayout gameListLinearLayout =
                (LinearLayout) view.findViewById(R.id.gameListLinearLayout);
        ImageView gameListImageView =
                (ImageView) view.findViewById(R.id.gameListImageView);
        TextView gameListNameTextView =
                (TextView) view.findViewById(R.id.gameListNameTextView);
        TextView gameListDescriptionTextView =
                (TextView) view.findViewById(R.id.gameListDescriptionTextView);
        RatingBar gameListRatingBar =
                (RatingBar) view.findViewById(R.id.gameListRatingBar);


        // Set Tag as current game object to be retrieves in MainActivity
        gameListLinearLayout.setTag(selectedDonation);
        gameListNameTextView.setText(selectedDonation.getName());
        gameListDescriptionTextView.setText(selectedDonation.getDescription());
        gameListRatingBar.setRating(selectedDonation.getRating());
        */



        return view;
    }
}

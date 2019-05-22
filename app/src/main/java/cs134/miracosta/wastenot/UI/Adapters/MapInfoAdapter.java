package cs134.miracosta.wastenot.UI.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import cs134.miracosta.wastenot.R;

/**
 * This Google Maps WindowAdapter class handles the inflation of the pin details window that comes up
 * when a user selects a location on the map.
 *
 * @author Will Craycroft
 */
public class MapInfoAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;

    /**
     * Creates a new <code>MapInfoAdapter</code> given a context.
     * @param context - The context of the calling activity
     */
    public MapInfoAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(mContext).inflate(R.layout.custom_map_info, null);
    }

    /**
     * Sets all TextViews in the window to the info passed in the Marker.
     * @param marker - Marker containing title and snippet information
     * @param view - reference to the current View
     */
    private void setWindowText(Marker marker, View view)
    {

        String title = marker.getTitle();
        String info = marker.getSnippet();

        TextView mapInfoTitleTextView = view.findViewById(R.id.mapInfoTitleTextView);
        TextView mapInfoDetailsTextView = view.findViewById(R.id.mapInfoDetailsTextView);
        LinearLayout mapInfoLinearLayout = view.findViewById(R.id.mapInfoLinearLayout);

        // Transfer tag from marker to linear layout
        mapInfoLinearLayout.setTag(marker.getTag());

        if (!title.equals("")) {
            mapInfoTitleTextView.setText(title);
            mapInfoDetailsTextView.setText(info);
        }
    }

    /**
     * Returns the View associated with this adapter.
     * @param marker - Marker containing title and snippet information
     * @return - reference to the current View
     */
    @Override
    public View getInfoWindow(Marker marker) {
        setWindowText(marker, mWindow);
        return mWindow;
    }

    /**
     * Returns the View associated with this adapter.
     * @param marker - Marker containing title and snippet information
     * @return - reference to the current View
     */
    @Override
    public View getInfoContents(Marker marker) {
        setWindowText(marker, mWindow);
        return mWindow;
    }
}

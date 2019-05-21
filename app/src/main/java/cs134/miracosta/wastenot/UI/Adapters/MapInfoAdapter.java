package cs134.miracosta.wastenot.UI.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import cs134.miracosta.wastenot.R;

public class MapInfoAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;

    public MapInfoAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(mContext).inflate(R.layout.custom_map_info, null);
    }

    private void setWindowText(Marker marker, View view)
    {
        /*
        String title = delivery.getDonor().getCompanyName();
        String info = "Pickup by: " + delivery.getDonation().getPickupEndTime()
                + "\nDeliver to: " + delivery.getClaimer().getCompanyName();
                */

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


    @Override
    public View getInfoWindow(Marker marker) {
        setWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        setWindowText(marker, mWindow);
        return mWindow;
    }
}

package cs134.miracosta.wastenot.UI;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import cs134.miracosta.wastenot.R;

public class DeliveryMapFragment extends Fragment implements OnMapReadyCallback {

    public static final String TAG = "WasteNot";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_delivery_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
        Log.i(TAG, "getMap called");    // TODO: debug

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG, "Entering onMapReady");    // debug

        GoogleMap map = googleMap;
        // Specify our location with LatLng class
        LatLng myPosition = new LatLng(33.190802, -117.301805);

        // Add position to the map
        map.addMarker(new MarkerOptions()
                .position(myPosition)
                .title("Current Location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.my_marker)));

        // Create new camera location at current location
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(myPosition)
                .zoom(15.0f)
                .build();
        // Update the position (move to location)
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        // Instruct map to move to this position
        map.moveCamera(cameraUpdate);

        // Add all caffeine locations
        LatLng position;
        /*
        for (Location location : allLocationsList)
        {
            position = new LatLng(location.getLatitude(), location.getLongitude());
            map.addMarker(new MarkerOptions()
                    .position(position)
                    .title(location.getName()));
        }
        */

    }
}



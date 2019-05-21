package cs134.miracosta.wastenot.UI;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import cs134.miracosta.wastenot.Model.Delivery;
import cs134.miracosta.wastenot.Model.FirebaseDBHelper;
import cs134.miracosta.wastenot.R;
import cs134.miracosta.wastenot.UI.Adapters.DeliveryListAdapter;
import cs134.miracosta.wastenot.UI.Adapters.MapInfoAdapter;

public class DeliveryMapFragment extends Fragment implements OnMapReadyCallback {

    public static final String TAG = "WasteNot";

    private FirebaseDBHelper db;
    private GoogleMap map;
    private Context mContext;
    private LocationManager locationManager;
    private LocationListener locationListener;

    // Default to MCC location
    private Location userLocation;
    private double MCCLongitude = 33.190958;
    private double MCCLatitude = -117.301889;

    List<Delivery> allDeliveriesList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("WasteNot", "Entering onCreateView for List");
        View rootView = inflater.inflate(R.layout.fragment_delivery_map, container, false);

        db = new FirebaseDBHelper();

        // Inflate map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        locationManager = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                userLocation = location;
                Log.v(TAG, "Longitude: " + userLocation.getLongitude());
                Log.v(TAG, "Latitude: " + userLocation.getLatitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }
            @Override
            public void onProviderEnabled(String provider) { }
            @Override
            public void onProviderDisabled(String provider) { }
        };

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        userLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Log.v(TAG, "Longitude: " + userLocation.getLongitude());
        Log.v(TAG, "Latitude: " + userLocation.getLatitude());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG, "Entering onMapReady");    // debug

        map = googleMap;

        map.setInfoWindowAdapter(new MapInfoAdapter(mContext));
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getTag()!= null) {

                    Delivery delivery = (Delivery) marker.getTag();
                    Intent deliveryDetailsIntent = new Intent(mContext, DeliveryDetailsActivity.class);
                    deliveryDetailsIntent.putExtra("Claimer", delivery.getClaimer());
                    deliveryDetailsIntent.putExtra("Donor", delivery.getDonor());
                    deliveryDetailsIntent.putExtra("Donation", delivery.getDonation());
                    deliveryDetailsIntent.putExtra("DonorLocation", delivery.getDonor().getLocation());
                    deliveryDetailsIntent.putExtra("ClaimerLocation", delivery.getClaimer().getLocation());
                    startActivity(deliveryDetailsIntent);
                    return true;
                }
                return false;
            }
        });
        // Set a default location (if gps if off or hasn't loaded this session)
        LatLng myPosition = new LatLng(33.190802, -117.301805);
        // Specify our location with LatLng class
        if (userLocation != null)
            myPosition = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());

        // Add position to the map
        Marker myMarker = map.addMarker(new MarkerOptions()
                .position(myPosition)
                .title(getString(R.string.current_location))
                .snippet("")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

        myMarker.setTag(null);

        // Create new camera location at current location
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(myPosition)
                .zoom(15.0f)
                .build();
        // Update the position (move to location)
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        // Instruct map to move to this position
        map.moveCamera(cameraUpdate);

        // get Deliveries
        db.getAllDeliveries(new FirebaseDBHelper.DataStatus() {
            @Override
            public void DataIsRead(List<?> items) {
                allDeliveriesList = (List<Delivery>) items;
                Log.i(TAG, "Delivery Map Data retrieved. Deliveries in list = " + allDeliveriesList.size());
                LatLng position;
                for (Delivery delivery : allDeliveriesList)
                {
                    String snippet = "Pickup by: " + delivery.getDonation().getPickupEndTime()
                            + "\nDeliver to: " + delivery.getClaimer().getCompanyName();
                    position = new LatLng(delivery.getDonor().getLocation().getLatitude(),
                            delivery.getDonor().getLocation().getLongitude());
                    Marker marker = map.addMarker(new MarkerOptions()
                            .title(delivery.getDonor().getCompanyName())
                            .position(position)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mapicon))
                            .snippet(snippet));
                    // Store Delivery as map icon tag (to be sent to details activity)
                    marker.setTag(delivery);
                }

            }
            @Override
            public void DataIsProcessed() { }
            @Override
            public void onError(String errorMessage) {
                Toast.makeText(mContext,
                        "Error retrieving user information.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}



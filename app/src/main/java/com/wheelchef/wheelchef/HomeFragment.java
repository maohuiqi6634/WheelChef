package com.wheelchef.wheelchef;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


public class HomeFragment extends Fragment implements LocationListener {
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LocationManager locationManager;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    private Button resetCameraBtn;
    private MapView mapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setUpMapIfNeeded();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View forReturn = inflater.inflate(R.layout.fragment_home, container, false);
        resetCameraBtn = (Button) forReturn.findViewById(R.id.reset_map_camera_button);
        resetCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng latLng = new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                mMap.animateCamera(cameraUpdate);
            }
        });

        mapView = (MapView) forReturn.findViewById(R.id.map);

        return forReturn;
    }

    @Override
    public void onResume() {
        super.onResume();

//        setUpMapIfNeeded();
    }
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
//        return inflater.inflate(R.layout.fragment_home,container,false);
//    }
//    public void onSearch(View view){
//        EditText location_tf = (EditText) findViewById(R.id.TFaddress);
//        String location = location_tf.getText().toString();
//        List<Address> addressList = null;
//
//        if (location != null || location != ""){
//            Geocoder geocoder = new Geocoder(this);
//            try {
//                addressList = geocoder.getFromLocationName(location , 1);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            Address address = addressList.get(0);
//            LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
//            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
//            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//        }
//
//    }

//    private void setUpMapIfNeeded() {
//        // Do a null check to confirm that we have not already instantiated the map.
//        if (mMap == null) {
//            // Try to obtain the map from the SupportMapFragment.
//            mMap = this
//                    .getMap();
//            // Check if we were successful in obtaining the map.
//            if (mMap != null) {
//                setUpMap();
//            }
//        }
//    }

//    private void setUpMap() {
//        /*mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
//        mMap.setMyLocationEnabled(true);*/
////        mMap.addMarker(new MarkerOptions().position(new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude())).title("I'm Here!"));
//        mMap.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
//        @Override
//        public void onMyLocationChange(Location location) {
//            // TODO Auto-generated method stub
//
//        }
//    });
//}

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.animateCamera(cameraUpdate);
        mMap.addMarker(new MarkerOptions().position(latLng).title("I'm Here!"));
        try {
            locationManager.removeUpdates(this);
        } catch (Exception e) {
            Log.d("HomeFragment", "permission error!");
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }


    public void setUpMap(){
        while(mMap == null){
            mMap = mapView.getMap();
        }
        try {
            MapsInitializer.initialize(getActivity());
        }
        catch (Exception e) {
            Log.e("HomeFragment", "Have GoogleMap but then error", e);
            return;
        }
        mMap.setMyLocationEnabled(true);
        locationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
        } catch (Exception e) {
            Log.d("HomeFragment", "permission error!");
        }
    }
}

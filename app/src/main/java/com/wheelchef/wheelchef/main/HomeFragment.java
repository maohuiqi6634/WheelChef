package com.wheelchef.wheelchef.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.wheelchef.wheelchef.R;
import com.wheelchef.wheelchef.chefinfo.ViewChefInfoDialog;
import com.wheelchef.wheelchef.utils.DirectionsJSONParser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class HomeFragment extends Fragment implements LocationListener {
    private static final String TAG = "HomeFragment";
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LocationManager locationManager;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    private Button resetCameraBtn;
    private SupportMapFragment mapFragment;
    private Marker user;
    private Marker cheflyk, chef2, chef3;

    private ProgressDialog progress = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        FragmentManager fragmentManager = getChildFragmentManager();

        mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map_fragment);
        mMap = mapFragment.getMap();

        return forReturn;
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMap();
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(userLatLng, 15);
        mMap.animateCamera(cameraUpdate);
        setUpMarkers(userLatLng);

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

    private void showProgressDialog(){
        if(progress==null){
            progress = new ProgressDialog(this.getActivity());
            progress.show();
        }
    }

    private void dismissProgressDialog(){
        if(progress!=null){
            progress.dismiss();
        }
    }

    private void setUpMap(){
        mMap.setMyLocationEnabled(true);
        locationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
        } catch (Exception e) {
            Log.d("HomeFragment", "permission error!");
        }
    }

    private void setUpMarkers(LatLng userLatLng){
        //to avoid having multiple user marker
        if(user!=null)
            user.remove();
        user = mMap.addMarker(new MarkerOptions().position(userLatLng).title("User"));
        cheflyk = mMap.addMarker(new MarkerOptions().position(new LatLng(1.355562, 103.760480)).title("lyk"));
        chef2 = mMap.addMarker(new MarkerOptions().position(new LatLng(1.298508, 103.845974)).title("Chef2"));
        chef3 = mMap.addMarker(new MarkerOptions().position(new LatLng(1.339769, 103.712619)).title("Chef3"));

        cheflyk.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_chef_hat_icon));
        chef2.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_chef_hat_icon));
        chef3.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_chef_hat_icon));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(!marker.getTitle().equals(user.getTitle())){
                    showProgressDialog();
                    String url = getDirectionsUrl(marker.getPosition(),user.getPosition());
                    DownloadTask downloadTask = new DownloadTask(marker);

                    // Start downloading json data from Google Directions API
                    downloadTask.execute(url);
                }
                return false;
            }
        });
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d(TAG, "Exception while downloading url "+e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {
        private Marker marker;

        public DownloadTask(Marker marker){
            this.marker = marker;
        }

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask(marker);

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        private Marker marker;

        public ParserTask(Marker marker){
            this.marker = marker;
        }
        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";

            if(result.size()<1){
                Toast.makeText(HomeFragment.this.getActivity().getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    if(j==0){    // Get distance from the list
                        distance = (String)point.get("distance");
                        continue;
                    }else if(j==1){ // Get duration from the list
                        duration = (String)point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(4);

                lineOptions.color(getResources().getColor(android.R.color.background_dark));
            }

//            tvDistanceDuration.setText("Distance:"+distance + ", Duration:"+duration);

            // Drawing polyline in the Google Map for the i-th route
            dismissProgressDialog();
            mMap.clear();
            LatLng userLatLng = new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
            setUpMarkers(userLatLng);
            mMap.addPolyline(lineOptions);
            ViewChefInfoDialog viewChefInfoDialog = new ViewChefInfoDialog(HomeFragment.this.getActivity(),marker.getTitle(),distance,duration);
            viewChefInfoDialog.show();
        }
    }
}

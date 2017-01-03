package com.example.aggarwal.ridealong;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    GoogleMap map;
    Spinner spinnerPlaceType;

    String[] placeType = null;
    String[] placeTypeName = null;
    GoogleApiClient googleApiClient = null;
    Location location;
    double lat = 0;
    double lon = 0;

    HashMap<String, String> mMarkerPlaceLink = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if (!isInternetAvailable()) {
            final AlertDialog.Builder builders = new AlertDialog.Builder(MapActivity.this);
            builders.setMessage("Enable Internet first");
            builders.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            AlertDialog dialog = builders.create();
            dialog.show();
        }
        else if ( !manager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
            buildAlertMessageNoGps();
        }

        // Array of place types
        placeType = getResources().getStringArray(R.array.place_type);

        // Array of place type names
        placeTypeName = getResources().getStringArray(R.array.place_type_name);

        // Creating an array adapter with an array of Place types
        // to populate the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, placeTypeName);

        // Getting reference to the Spinner
        spinnerPlaceType = (Spinner) findViewById(R.id.spr_place_type);

        // Setting adapter on Spinner to set place types
        spinnerPlaceType.setAdapter(adapter);

        Button btnFind;

        // Getting reference to Find Button
        btnFind = (Button) findViewById(R.id.btn_find);


        // Getting reference to the SupportMapFragment
        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);

        // Getting Google Map
        map = fragment.getMap();

        // Enabling MyLocation in Google Map
        map.setMyLocationEnabled(true);

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
                String title = arg0.getTitle();
                if (title != null) {
                    String name = title.substring(0, title.indexOf(":"));
                    String loca = title.substring(title.indexOf(":") + 1, title.indexOf("%"));
                    String rate = title.substring(title.indexOf("%") + 1,title.indexOf("@"));
                    final Double lats=Double.parseDouble(title.substring(title.indexOf("@") + 1, title.indexOf("!")));
                    final Double lngs=Double.parseDouble(title.substring(title.indexOf("!") + 1));
                    if (rate.equalsIgnoreCase("null") || rate.isEmpty() || rate.equalsIgnoreCase("") || rate.length() == 0 || rate.matches(" ")) {
                        rate = "rating not available";
                    }
                    builder.setMessage("Name : " + name + "\nLocation : " + loca);
//                    builder.setPositiveButton("Go to", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Intent intent=new Intent(MapActivity.this,MapsActivity.class);
//                            intent.putExtra("latd",lats);
//                            intent.putExtra("lngd",lngs);
//                            intent.putExtra("lats",lat);
//                            intent.putExtra("lngs",lon);
//                            startActivity(intent);
//                        }
//                    });
                    builder.setNegativeButton("Dismiss",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        // Setting click event lister for the find button
        btnFind.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                int selectedPosition = spinnerPlaceType.getSelectedItemPosition();
                String type = placeType[selectedPosition];

                StringBuilder strBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                strBuilder.append("location=" + lat + "," + lon);
                strBuilder.append("&radius=5000");
                strBuilder.append("&types=" + type);
                strBuilder.append("&sensor=true");
                strBuilder.append("&key=AIzaSyCujnTHGXyVms0rxQY9ceGhFWYQE7fy8Ys");
                // Creating a new non-ui thread task to download Google place json data
                PlacesTask placesTask = new PlacesTask();
                // Invokes the "doInBackground()" method of the class PlaceTask
                System.out.println("" + strBuilder);
                placesTask.execute(strBuilder.toString());
            }
        });
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String dat = "";
        InputStream ifStream = null;
        HttpURLConnection uRLConn = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            uRLConn = (HttpURLConnection) url.openConnection();

            // Connecting to url
            uRLConn.connect();

            // Reading data from url
            ifStream = uRLConn.getInputStream();

            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(ifStream));

            StringBuffer strBuffer = new StringBuffer();

            String lin = "";
            while ((lin = bufferReader.readLine()) != null) {
                strBuffer.append(lin);
            }

            dat = strBuffer.toString();

            bufferReader.close();

        } catch (Exception e) {
            Log.d("Exception while downloading url", e.toString());
        } finally {
            ifStream.close();
            uRLConn.disconnect();
        }
        return dat;
    }

    @Override
    public void onConnected(Bundle bundle) {
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            Toast.makeText(this, "" + lat + " " + lon, Toast.LENGTH_SHORT).show();
            LatLng latlan = new LatLng(lat, lon);
            this.lat = lat;
            this.lon = lon;
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "connection suspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "connection Failed", Toast.LENGTH_SHORT).show();
    }

    /**
     * A class, to download Google Places
     */
    private class PlacesTask extends AsyncTask<String, Integer, String> {

        String dat = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try {
                dat = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return dat;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result) {
            ParserTask parsTask = new ParserTask();

            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            parsTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> allPlaces = null;
            PlaceJSONParser placeJsonParser = new PlaceJSONParser();
            try {
                jObject = new JSONObject(jsonData[0]);

//              Getting parsed data as a List construct
                allPlaces = placeJsonParser.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return allPlaces;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(final List<HashMap<String, String>> list) {

            // Clears all the existing markers
            map.clear();


            for (int i = 0; i < list.size(); i++) {
                try {
                    // Creating a marker
                    MarkerOptions markerOptions = new MarkerOptions();

                    // Getting a place from the places list
                    HashMap<String, String> hmPlace = list.get(i);

                    // Getting latitude of the place
                    double lat = Double.parseDouble(hmPlace.get("lat"));

                    // Getting longitude of the place
                    double lng = Double.parseDouble(hmPlace.get("lng"));

                    // Getting name
                    final String name = hmPlace.get("place_name");

                    // Getting vicinity
                    final String vicinity = hmPlace.get("vicinity");

                    final String rating=hmPlace.get("rating");

                    LatLng latLng = new LatLng(lat, lng);

                    // Setting the position for the marker
                    markerOptions.position(latLng);

                    // Setting the title for the marker.
                    //This will be displayed on taping the marker
                    markerOptions.title(name + " : " + vicinity+" % "+rating+"@"+lat+"!"+lng);
                    // Placing a marker on the touched position
                    Marker m = map.addMarker(markerOptions);

                    // Linking Marker id and place reference
                    mMarkerPlaceLink.put(m.getId(), hmPlace.get("reference"));

                } catch (Exception e) {
                    System.out.println("" + e);
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location loc) {
        lat = loc.getLatitude();
        lon = loc.getLongitude();
        LatLng latLng = new LatLng(lat, lon);

        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(12));
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    //  to check if Internet Available or not
    private boolean isInternetAvailable() {
        boolean isWifiAvailable = false;
        boolean isMobileInternetAvailable = false;
        ConnectivityManager connectManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netwrkInfo = connectManager.getAllNetworkInfo();
        for (NetworkInfo networkInfor : netwrkInfo) {
            if (networkInfor.getTypeName().equalsIgnoreCase("WIFI")) {
                if (networkInfor.isConnected()) {
                    isWifiAvailable = true;
                }
            }
            if (networkInfor.getTypeName().equalsIgnoreCase("MOBILE")) {
                if (networkInfor.isConnected()) {
                    isMobileInternetAvailable = true;
                }
            }
        }
        return isMobileInternetAvailable || isWifiAvailable;
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS first")
                .setCancelable(false)
                .setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        finish();
                    }
                })
                .setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
//                        dialog.cancel();
                        finish();

                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
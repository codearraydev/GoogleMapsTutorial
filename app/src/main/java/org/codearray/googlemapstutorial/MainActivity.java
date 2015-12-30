package org.codearray.googlemapstutorial;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MainActivity extends AppCompatActivity implements LocationListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener {


    private GoogleMap googleMap;            //google maps object
    boolean isZoom = false;             // a boolen varibale to check the zoom level of google maps
    private LocationManager locationManager;        // a locaiton manager object to display the current location on the map


    private  void checkLocationSettings()
    {
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // Build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Location Services Not Active");
            builder.setMessage("Please enable Location Services and GPS");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
    }


    //startup the map
    private void initilizeMap() {
        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager()
                        .findFragmentById(R.id.map)).getMap();
                // check if map is created successfully or not

                /**
                 *
                 * Please see notes in the blog post to under these lines.
                 */
                googleMap.setOnMapLongClickListener(this);      // if you tap on map for 3 seconds you can perform a certain event for the map such as getting latitude longitude
                // location name or anything that is related to place you tap on
                googleMap.setMyLocationEnabled(true);           //this lines gets your current location
                googleMap.getUiSettings().setMyLocationButtonEnabled(true); //this will enable the find current location button on the map
                googleMap.setOnMarkerDragListener(this);        // in case you want to enable the drag evetns for the markers on the google maps
                googleMap.setOnMarkerClickListener(this);       // this event is being used to set the click events for the marker
                if (googleMap == null) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkLocationSettings();

        //setting up the current locaiton on the map......
        try {


            Location location = null;

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 0, 0, this);
            location = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            // drawCurrentLocation(location);
            if (location == null) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 0, 0, this);

                location = locationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                // drawCurrentLocation(location);

            }
            initilizeMap();     //calling your map initlizing method over here

            if (!isZoom) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(location.getLatitude(), location
                                .getLongitude()), 15));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                isZoom = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // long press events for the maps goes here
    @Override
    public void onMapLongClick(LatLng latLng) {

        //by long click on the map you can get latitude longitude
        double latitude = latLng.latitude;
        double longitude = latLng.longitude;

        Toast.makeText(getApplicationContext() , "Laitude : "+latitude +"AND Longitude "+longitude, Toast.LENGTH_LONG).show();



    }


    // if you want a drag events for the marker add thiese here
    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    //during marker being drag event
    @Override
    public void onMarkerDrag(Marker marker) {

    }

    //when the marker drags end
    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    //market click event code be done here
    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }


    //these are location change evetns and you can perform the require events here
    @Override
    public void onLocationChanged(Location location) {
        //for example if you want to update the location every time you it by

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        // now you can do anyhting with them you want to.
        //in the same way you can get other information form Location class;
        //like at what speed you devcice is moving or time.
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
}

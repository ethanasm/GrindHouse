package com.example.beanandleaf;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import database.DatabaseHelper;
import model.Order;
import model.Store;

public class Recommendations extends Fragment implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;

    Location currentLocation;
    //FusedLocationProviderClient fusedLocationProviderClient;
    //private static final int REQUEST_CODE = 101;


    SupportMapFragment mapFragment;
    public Recommendations() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_recommendations, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapRec);
        if(mapFragment == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.mapRec,  mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) mView.findViewById(R.id.mapRec);
        if(mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(34, -115);
        if(currentLocation != null) {
            System.out.println("A non null location");
            latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        }
        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this.getContext(), R.raw.mapstyle));

            if (!success) {
                Log.e("MapActivity", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapActivity", "Can't find style. Error: ", e);
        }

        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        float[] colours = { BitmapDescriptorFactory.HUE_ORANGE, BitmapDescriptorFactory.HUE_RED};

        //Marker for current location
        DatabaseHelper db = new DatabaseHelper(getActivity());
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
        final String userType = pref.getString("userType", null);
        String email = pref.getString("email", null);
        Integer userID = db.getUserId(email, userType);
        ArrayList<Store> stores = db.getStores();
        ArrayList<Store> userStores = db.getStores(userID);
        ArrayList <Order> orders = db.getUserOrders(userID);

        Activity activity = getActivity();
        if(stores == null || stores.size() < 4)
        {
            noRecs(activity);
        }
        else{

            String drinkName = getDrinkName(orders);

            Set<Store> set = new HashSet<Store>();
            for (Store x : userStores)
                set.add(x);
            //Find Stores Users haven't visited
            ArrayList<Store> unvisitedStores = new ArrayList<Store>();
            for (Store s : stores) {
                if (!set.contains(s)) {
                    unvisitedStores.add(s);
                }
            }

            //If the user has visited all stores, we don't have recs
            if (unvisitedStores == null) {
                Toast t = Toast.makeText(activity, "No recommendations at this time! You've visited all the coffee shops!", Toast.LENGTH_LONG);
                t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                t.show();
            }
            Store ofChoice = null;
            //Now we check if their most frequented drink exists at a place they haven't been
            for (Store s : unvisitedStores) {
                boolean exists = db.checkMenuItemNameExists(s.getStoreID(), drinkName);
                if (exists == true) {
                    ofChoice = s;
                    break;
                }
            }
            //we add that choice to the map
            if (ofChoice != null) {
                MarkerOptions mo = new MarkerOptions().position(new LatLng(ofChoice.getLatitude(), ofChoice.getLongitude())).title(ofChoice.getName()).icon(BitmapDescriptorFactory.defaultMarker(colours[new Random().nextInt(colours.length)]));
                Marker m = mGoogleMap.addMarker(mo);
                m.setTag(ofChoice.getStoreID());
                Toast t = Toast.makeText(activity, "Try a " + drinkName + " at " + ofChoice.getName() + "!", Toast.LENGTH_LONG);
                t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                t.show();
            }

        }
        CameraPosition Starbucks = CameraPosition.builder().target(new LatLng(34.0224, -118.2851)).zoom(14).bearing(0).tilt(0).build();
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                int id = userType.contentEquals("Customer") ? R.id.fragment_container_customer : R.id.fragment_container_merchant;
                Fragment mapClickMenuFragment = new MapClickMenu((int) marker.getTag());
                getFragmentManager()
                        .beginTransaction()
                        .replace(id, mapClickMenuFragment)
                        .commit();
            }

        });

        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Starbucks));

        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);
        }

    }



    public static void noRecs(Activity activity)
    {
        Toast t = Toast.makeText(activity, "Sorry, unable to provide recommendations until you've made at least 4 trips!", Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        t.show();
    }

    public static String getDrinkName(ArrayList <Order> orders)
    {
        Map<String, Integer> map = new HashMap<>();
        for (Order o : orders) {
            if (map.containsKey(o.getName())) {
                map.put(o.getName(), map.get(o.getName()) + 1);
            } else {
                map.put(o.getName(), 1);
            }
        }

        Map.Entry<String, Integer> maxEntry = null;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }

        return maxEntry.getKey();
    }



}

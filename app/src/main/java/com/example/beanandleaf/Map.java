package com.example.beanandleaf;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.location.Location;

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
import java.util.Random;

import database.DatabaseHelper;
import model.Store;

public class Map extends Fragment implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;


    SupportMapFragment mapFragment;
    public Map() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if(mapFragment == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map,  mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) mView.findViewById(R.id.map);
        if(mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
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


        DatabaseHelper db = new DatabaseHelper(getActivity());
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
        final String userType = pref.getString("userType", null);
        ArrayList<Store> stores = db.getStores();

        for (Store s : stores) {
            if (s.isVerified()) {
                MarkerOptions mo = new MarkerOptions().position(new LatLng(s.getLatitude(), s.getLongitude())).title(s.getName()).icon(BitmapDescriptorFactory.defaultMarker(colours[new Random().nextInt(colours.length)]));
                Marker m = mGoogleMap.addMarker(mo);
                m.setTag(s.getStoreID());
            }
        }

        /* !!!!!!!!!DUMMY MARKERS ARE HIDDEN FOR NOW! PLEASE DON'T UNCOMMENT AND PUSH. To add a marker on the map, create a merchant account and create a store with a latitude/longitude of one of the stores below. Thanks! -Ethan
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(34.024120, -118.278170)).title("Starbucks").icon(BitmapDescriptorFactory.defaultMarker(colours[new Random().nextInt(colours.length)])));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(34.022090, -118.282460)).title("Starbucks").icon(BitmapDescriptorFactory.defaultMarker(colours[new Random().nextInt(colours.length)])));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(34.026409, -118.277473)).title("Starbucks").icon(BitmapDescriptorFactory.defaultMarker(colours[new Random().nextInt(colours.length)])));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(34.018630, -118.281670)).title("Starbucks").icon(BitmapDescriptorFactory.defaultMarker(colours[new Random().nextInt(colours.length)])));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(34.024630, -118.288490)).title("Starbucks").icon(BitmapDescriptorFactory.defaultMarker(colours[new Random().nextInt(colours.length)])));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(34.024498, -118.284343)).title("Starbucks").icon(BitmapDescriptorFactory.defaultMarker(colours[new Random().nextInt(colours.length)])));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(34.025663, -118.284364)).title("Starbucks").icon(BitmapDescriptorFactory.defaultMarker(colours[new Random().nextInt(colours.length)])));


        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(34.017520, -118.282660)).title("Coffee Bean & Tea Leaf").icon(BitmapDescriptorFactory.defaultMarker(colours[new Random().nextInt(colours.length)])));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(34.020035, -118.283444)).title("Literatea").icon(BitmapDescriptorFactory.defaultMarker(colours[new Random().nextInt(colours.length)])));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(34.025343, -118.285405)).title("Cafe Dulce").icon(BitmapDescriptorFactory.defaultMarker(colours[new Random().nextInt(colours.length)])));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(34.017521, -118.282661)).title("Coffee Bean & Tea Leaf").icon(BitmapDescriptorFactory.defaultMarker(colours[new Random().nextInt(colours.length)])));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(34.018646, -118.284478)).title("USC Law School Cafe").icon(BitmapDescriptorFactory.defaultMarker(colours[new Random().nextInt(colours.length)])));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(34.026550, -118.285301)).title("Cafe Dulce").icon(BitmapDescriptorFactory.defaultMarker(colours[new Random().nextInt(colours.length)])));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(34.031966, -118.284216)).title("DRNK coffee + tea").icon(BitmapDescriptorFactory.defaultMarker(colours[new Random().nextInt(colours.length)])));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(34.034422, -118.283604)).title("Nature's Brew").icon(BitmapDescriptorFactory.defaultMarker(colours[new Random().nextInt(colours.length)])));
        */

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
}
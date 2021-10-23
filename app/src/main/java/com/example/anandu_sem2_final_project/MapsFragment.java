package com.example.anandu_sem2_final_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.anandu_sem2_final_project.Database.AppDatabase;
import com.example.anandu_sem2_final_project.Database.UserData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private AppDatabase db;
    private List<UserData> userDataList, filteredList;
    private UserData selectedUserdata = null;
    private GoogleMap googleMap = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        db = AppDatabase.getDbInstance(getContext());
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (googleMap != null)
            updateMarker(null);
    }

    private void updateMarker(List<UserData> list) {
        Log.i("anandu", "updateMarker");
        googleMap.clear();
        if(list == null) {
            userDataList = db.userDao().getAllData();
            list = userDataList;
        }
        int index = 0;
        for (UserData userData : list) {
            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(userData.getLatitude(), userData.getLongitude()));
            markerOptions.title(userData.getName());
            Marker marker = googleMap.addMarker(markerOptions);
            marker.setTag(index++);
            marker.showInfoWindow();
        }

        googleMap.setOnMarkerClickListener(this);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        updateMarker(null);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        selectedUserdata = userDataList.get((Integer) marker.getTag());
        goToEditPage();
        return false;
    }

    public void goToEditPage() {
        Intent intent = new Intent(getContext(), EditActivity.class);
        intent.putExtra("SelectedItem", selectedUserdata);
        startActivity(intent);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem mSearchMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) mSearchMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i("anandu", "searched in list " + query);

                if (query.isEmpty()) {
                    updateMarker(null);
                } else {
                    Predicate<UserData> bynameOrCountry = userData ->
                            userData.getName().toLowerCase().contains(query.toLowerCase())
                                    || userData.getCountry().toLowerCase().contains(query.toLowerCase());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        filteredList = userDataList.stream().filter(bynameOrCountry).collect(Collectors.toList());
                    }
                    updateMarker(filteredList);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}
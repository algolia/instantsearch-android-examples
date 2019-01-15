package com.algolia.instantsearch.examples.icebnb;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.algolia.instantsearch.core.events.ErrorEvent;
import com.algolia.instantsearch.examples.icebnb.widgets.MapWidget;
import com.algolia.instantsearch.core.helpers.Searcher;
import com.algolia.instantsearch.ui.helpers.InstantSearch;
import com.algolia.search.saas.AbstractQuery;
import com.algolia.search.saas.Query;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class IcebnbActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String ALGOLIA_APP_ID = "latency";
    private static final String ALGOLIA_INDEX_NAME = "airbnb";
    private static final String ALGOLIA_API_KEY = "6be0576ff61c053d5f9a3225e2a90f76";
    private static final int REQUEST_LOCATION = 1;

    private Searcher searcher;

    private FilterResultsFragment filterResultsFragment;
    private SearchView searchView;
    private MapWidget mapWidget;
    private LocationRequest mLocationRequest;
    private Location lastLocation;
    GoogleApiClient mGoogleApiClient;
    private long UPDATE_INTERVAL = 20000;  /* 20 secs */
    private long FASTEST_INTERVAL = 10000; /* 10 secs */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupGoogleAPI();

        setContentView(R.layout.activity_icebnb);
        EventBus.getDefault().register(this);
        // Initialize a Searcher with your credentials and an index name
        searcher = Searcher.create(ALGOLIA_APP_ID, ALGOLIA_API_KEY, ALGOLIA_INDEX_NAME);
        searcher.setQuery(new Query().setRestrictSearchableAttributes("city", "country", "name").setFacets("room_type", "price"));
        // Create the FilterResultsFragment here so it can set the appropriate facets on the Searcher
        filterResultsFragment = FilterResultsFragment.get(searcher)
                .addSeekBar("price", 100);
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        final SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        fragmentTransaction.add(R.id.map_placeholder, mapFragment);
        fragmentTransaction.commit();
        mapWidget = new MapWidget(mapFragment);
        searcher.registerResultListener(mapWidget);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_icebnb, menu);
        new InstantSearch(this, menu, R.id.action_search, searcher) // link the Searcher to the UI
                .search(); //Show results for empty query on startup

        final MenuItem itemSearch = menu.findItem(R.id.action_search);
        searchView = (SearchView) itemSearch.getActionView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            itemSearch.expandActionView(); //open SearchBar on startup
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            final FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager.findFragmentByTag(FilterResultsFragment.TAG) == null) {
                filterResultsFragment.show(fragmentManager, FilterResultsFragment.TAG);
                hideKeyboard();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent event) {
        Toast.makeText(this, "Error searching" + event.query.getQuery() + ":" + event.error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    private void hideKeyboard() {
        searchView.clearFocus();
    }

    private void setupGoogleAPI() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocation();
    }

    private void startLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
            return;
        }

        lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        updateSearch();
        startLocationUpdates();
        if (mapWidget.googleMap != null) {
            mapWidget.googleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        updateSearch();
        if (mapWidget.googleMap != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mapWidget.googleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startLocation();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
    protected void startLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Enable Permissions", Toast.LENGTH_LONG).show();
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected void updateSearch() {
        if (lastLocation != null) {
            final AbstractQuery.LatLng coords =  new AbstractQuery.LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            searcher.getQuery().setAroundLatLng(coords);
            searcher.search();
        }
    }
}

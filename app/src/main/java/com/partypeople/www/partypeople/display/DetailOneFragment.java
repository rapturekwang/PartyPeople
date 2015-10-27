package com.partypeople.www.partypeople.display;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailOneFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailOneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailOneFragment extends Fragment {
    private static final String ARG_NAME = "name";
    private String mName;
    TMapView mapView;
    LocationManager mLM;

    public static DetailOneFragment newInstance(String name) {
        DetailOneFragment fragment = new DetailOneFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public DetailOneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mName = getArguments().getString(ARG_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_one, container, false);

        initData();

        mapView = (TMapView)view.findViewById(R.id.view_map);
        new RegisterTask().execute();

        mLM = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        return view;
    }

    private void initData() {
//        for (int i = 0; i < 5 ; i++) {
//            PartyItemData d = new PartyItemData();
//            d.title = "Come to House Party!";
//            d.date = "5월 7일 / 19:00-21:30";
//            d.partyImg = getResources().getDrawable(R.drawable.demo_img);
//            d.location = "서울시 서초구";
//            d.price = "$25";
//            d.progress = 50;
//            d.progressText = d.progress+"% 모금됨";
//            d.dueDate = "7일 남음";
//            mAdapter.add(d);
//        }
    }

    boolean isInitialized = false;

    class RegisterTask extends AsyncTask<String,Void,Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            mapView.setSKPMapApiKey("8d709b60-6811-3e70-9e0b-e2cb992de402");
            mapView.setLanguage(TMapView.LANGUAGE_KOREAN);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            isInitialized = true;
            setupMap();
        }
    }

    Location cacheLocation;
    LocationListener mListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (isInitialized) {
                moveMap(location.getLatitude(), location.getLongitude());
                setMyLocation(location.getLatitude(), location.getLongitude());
            } else {
                cacheLocation = location;
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    };

    private void moveMap(double lat, double lng) {
        mapView.setCenterPoint(lng, lat);
        mapView.setZoomLevel(17);
    }

    private void setMyLocation(double lat, double lng) {
        mapView.setLocationPoint(lng, lat);
        Bitmap bm = ((BitmapDrawable)getResources().getDrawable(R.drawable.my_icon)).getBitmap();
        mapView.setIcon(bm);
        mapView.setIconVisibility(true);
    }

    private void setupMap() {
        if (cacheLocation != null) {
            moveMap(cacheLocation.getLatitude(), cacheLocation.getLongitude());
            setMyLocation(cacheLocation.getLatitude(), cacheLocation.getLongitude());
            cacheLocation = null;
        }

        mapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
            @Override
            public void onCalloutRightButton(TMapMarkerItem tMapMarkerItem) {
                Toast.makeText(getContext(), "Marker Click", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

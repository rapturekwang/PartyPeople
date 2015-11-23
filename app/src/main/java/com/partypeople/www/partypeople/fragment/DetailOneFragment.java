package com.partypeople.www.partypeople.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.PartyDetailActivity;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailOneFragment extends Fragment {
    private static final String ARG_NAME = "name";
    private String mName;
    TMapView mapView;
    TextView mapLocation;
    LocationManager mLM;
    ArrayAdapter<POIItem> mAdapter;

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
        final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.root_layout);


//        ViewTreeObserver observer = linearLayout.getViewTreeObserver();
//        observer.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                Log.d("DetailOne", Integer.toString(linearLayout.getHeight()));
//                linearLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                PartyDetailActivity activity = (PartyDetailActivity)getActivity();
////                activity.setPagerHeight(linearLayout.getHeight());
//            }
//        });

        mapView = (TMapView)view.findViewById(R.id.view_map);
        mapLocation = (TextView)view.findViewById(R.id.text_location);
        new RegisterTask().execute();

        mLM = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        setMap();

        return view;
    }

    void setMap() {
        String keyword = mapLocation.getText().toString();
        if(!TextUtils.isEmpty(keyword)) {
            TMapData data = new TMapData();
            data.findAllPOI(keyword, new TMapData.FindAllPOIListenerCallback() {
                @Override
                public void onFindAllPOI(final ArrayList<TMapPOIItem> arrayList) {
                    PartyDetailActivity activity = (PartyDetailActivity)getActivity();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(arrayList.size() > 0) {
                                moveMap(arrayList.get(0).getPOIPoint().getLatitude(), arrayList.get(0).getPOIPoint().getLongitude());
                            }
                        }
                    });
                }
            });
        }
    }

    private void addMarkerPOI(ArrayList<TMapPOIItem> list) {
        for(TMapPOIItem poi : list) {
            TMapPoint point = mapView.getCenterPoint();
            TMapMarkerItem item = new TMapMarkerItem();
            item.setTMapPoint(point);
            Bitmap icon = ((BitmapDrawable)getResources().getDrawable(android.R.drawable.ic_dialog_map)).getBitmap();
            item.setIcon(icon);
            item.setPosition(0.5f, 1);
            item.setCalloutTitle(poi.getPOIName());
            item.setCalloutSubTitle(poi.getPOIContent());
            Bitmap left = ((BitmapDrawable)getResources().getDrawable(android.R.drawable.ic_dialog_alert)).getBitmap();
            item.setCalloutLeftImage(left);
            Bitmap right = ((BitmapDrawable)getResources().getDrawable(android.R.drawable.ic_dialog_info)).getBitmap();
            item.setCalloutRightButtonImage(right);
            item.setCanShowCallout(true);

            mapView.addMarkerItem(poi.getPOIID(), item);
        }
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

    @Override
    public void onStart() {
        super.onStart();
        mLM.requestSingleUpdate(LocationManager.GPS_PROVIDER, mListener, null);
    }

    @Override
    public void onStop() {
        super.onStop();
        mLM.removeUpdates(mListener);
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

    class POIItem {
        TMapPOIItem poi;

        @Override
        public String toString() {
            return poi.getPOIName();
        }
    }
}

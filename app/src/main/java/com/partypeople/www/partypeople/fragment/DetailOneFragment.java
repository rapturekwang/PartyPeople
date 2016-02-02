package com.partypeople.www.partypeople.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.PartyDetailActivity;
import com.partypeople.www.partypeople.activity.UserActivity;
//import com.partypeople.www.partypeople.activity.UserActivity;
import com.partypeople.www.partypeople.activity.UserListActivity;
import com.partypeople.www.partypeople.data.Follow;
import com.partypeople.www.partypeople.data.User;
import com.partypeople.www.partypeople.dialog.LoadingDialogFragment;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.manager.PropertyManager;
import com.partypeople.www.partypeople.utils.CircleTransform;
import com.partypeople.www.partypeople.utils.CustomGlideUrl;
import com.partypeople.www.partypeople.utils.DateUtil;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailOneFragment extends Fragment {
    private static final String ARG_NAME = "name";
    private String mName;
    TMapView mapView;
    TextView mapLocation, descriptionView, participantView, hostNameView, followView, groupsView;
    List<ImageView> parti = new ArrayList<ImageView>();
    ImageView imgHostView, imgBtnUserinfo;
    LocationManager mLM;
    User user;
    LinearLayout layout;
    ArrayAdapter<POIItem> mAdapter;
    PartyDetailActivity activity;
    int[] ids = {
            R.id.image_parti1,
            R.id.image_parti2,
            R.id.image_parti3,
            R.id.image_parti4,
            R.id.image_parti_plus,
    };

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

        layout = (LinearLayout)view.findViewById(R.id.root_layout);
        activity = (PartyDetailActivity)getActivity();

        mapView = (TMapView)view.findViewById(R.id.view_map);
        mapLocation = (TextView)view.findViewById(R.id.text_location);
        new RegisterTask().execute();

        descriptionView = (TextView)view.findViewById(R.id.text_des);
        participantView = (TextView)view.findViewById(R.id.text_participant);
        hostNameView = (TextView)view.findViewById(R.id.text_host_name);
        imgHostView = (ImageView)view.findViewById(R.id.image_host);
        followView = (TextView)view.findViewById(R.id.text_follow);
        groupsView = (TextView)view.findViewById(R.id.text_groups);

        ImageView imageDes = (ImageView)view.findViewById(R.id.image_des);
        if(activity.party.has_photo) {
            imageDes.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(NetworkManager.getInstance().URL_SERVER + activity.party.photo)
                    .placeholder(R.color.defaultImage)
                    .error(R.color.defaultImage)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            activity.setPagerHeight(50000);
                            changeHeight();
                            return false;
                        }
                    })
                    .into(imageDes);
        }

        for(int i=0;i<ids.length;i++) {
            final int temp = i;
            parti.add((ImageView)view.findViewById(ids[i]));
            if(i<activity.party.members.size()-1) {
                parti.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!PropertyManager.getInstance().isLogin()) {
                            Toast.makeText(getContext(), "로그인이 필요한 서비스 입니다", Toast.LENGTH_SHORT).show();
                        } else {
                            if (temp == 4) {
                                ArrayList<String> participants = new ArrayList<String>();
                                for (int i = 1; i < activity.party.members.size(); i++) {
                                    participants.add(activity.party.members.get(i).id);
                                }
                                Intent intent = new Intent(getContext(), UserListActivity.class);
                                intent.putStringArrayListExtra("userlist", participants);
                                startActivity(intent);
                            } else {
                                final LoadingDialogFragment dialogFragment = new LoadingDialogFragment();
                                dialogFragment.show(getFragmentManager(), "loading");
                                NetworkManager.getInstance().getUser(getContext(), activity.party.members.get(temp + 1).id, new NetworkManager.OnResultListener<User>() {
                                    @Override
                                    public void onSuccess(User result) {
                                        Intent intent = new Intent(getContext(), UserActivity.class);
                                        intent.putExtra("user", result);
                                        startActivity(intent);
                                        dialogFragment.dismiss();
                                    }

                                    @Override
                                    public void onFail(String response) {
                                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                                        dialogFragment.dismiss();
                                    }
                                });
                            }
                        }
                    }
                });
            }
        }

        imgBtnUserinfo = (ImageView)view.findViewById(R.id.img_btn_userinfo);
        imgBtnUserinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!PropertyManager.getInstance().isLogin()) {
                    Toast.makeText(getContext(), "로그인이 필요한 서비스 입니다", Toast.LENGTH_SHORT).show();
                } else {
                    NetworkManager.getInstance().getUser(getContext(), activity.party.owner.id, new NetworkManager.OnResultListener<User>() {
                        @Override
                        public void onSuccess(final User result) {
                            Intent intent = new Intent(getContext(), UserActivity.class);
                            intent.putExtra("user", result);
                            startActivity(intent);
                        }

                        @Override
                        public void onFail(String response) {
                            Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        mapLocation.setText(activity.party.location);
        descriptionView.setText(activity.party.description);
        participantView.setText("참여자 " + (activity.party.members.size()-1) + "명");
        hostNameView.setText(activity.party.owner.name);

        GlideUrl glideUrl = null;
        if (activity.party.owner.has_photo) {
            CustomGlideUrl customGlideUrl = new CustomGlideUrl();
            glideUrl = customGlideUrl.getGlideUrl(NetworkManager.getInstance().URL_SERVER + activity.party.owner.photo);
        } else if (!activity.party.owner.has_photo && activity.party.owner.provider.equals("facebook")) {
            glideUrl = new GlideUrl(activity.party.owner.photo);
        }
        if(glideUrl!=null) {
            Glide.with(this)
                    .load(glideUrl)
                    .signature(new StringSignature(DateUtil.getInstance().getCurrentDate()))
                    .placeholder(R.drawable.default_profile)
                    .error(R.drawable.default_profile)
                    .transform(new CircleTransform(getContext()))
                    .into(imgHostView);
        }

        NetworkManager.getInstance().getUser(getContext(), activity.party.owner.id, new NetworkManager.OnResultListener<User>() {
            @Override
            public void onSuccess(final User result) {
                user = result;
                int owner = 0, memeber = 0;
                if(user.groups.size()!=0) {
                    for (int i = 0; i < user.groups.size(); i++) {
                        for(int j=0; j<user.groups.get(i).members.size(); j++) {
                            if (user.id.equals(user.groups.get(i).members.get(j).id) && user.groups.get(i).members.get(j).role.equals("OWNER"))
                                owner++;
                            else if (user.id.equals(user.groups.get(i).members.get(j).id) && user.groups.get(i).members.get(j).role.equals("MEMBER"))
                                memeber++;
                        }
                    }
                }
                groupsView.setText("모임 개최 " + owner + " | 모임 참여 " + memeber);
                followView.setText("팔로잉 " + user.following.size() + " | 팔로워 " + user.follower.size());
            }

            @Override
            public void onFail(String response) {
                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
            }
        });

        mLM = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        initData();
        setMap();
        changeHeight();

        return view;
    }

    public void initData() {
        for(int i=1;i<activity.party.members.size();i++) {
            parti.get(i-1).setVisibility(View.VISIBLE);
            if(i==5)
                break;
            else if(i<5){
                GlideUrl glideUrl2 = null;
                if (activity.party.members.get(i).has_photo) {
                    CustomGlideUrl customGlideUrl = new CustomGlideUrl();
                    glideUrl2 = customGlideUrl.getGlideUrl(NetworkManager.getInstance().URL_SERVER + activity.party.members.get(i).member.photo);
                } else if (!activity.party.members.get(i).has_photo && activity.party.members.get(i).member.provider.equals("facebook")) {
                    glideUrl2 = new GlideUrl(activity.party.members.get(i).member.photo);
                }
                if(glideUrl2!=null) {
                    Glide.with(getContext())
                            .load(glideUrl2)
                            .signature(new StringSignature(DateUtil.getInstance().getCurrentDate()))
                            .placeholder(R.drawable.default_profile)
                            .error(R.drawable.default_profile)
                            .transform(new CircleTransform(getContext()))
                            .into(parti.get(i-1));
                }
            }
        }
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
                                addMarkerPOI(arrayList.get(0));
                                moveMap(arrayList.get(0).getPOIPoint().getLatitude(), arrayList.get(0).getPOIPoint().getLongitude());
                            }
                        }
                    });
                }
            });
        }
    }

    private void addMarkerPOI(TMapPOIItem poiItem) {
        TMapPoint point = poiItem.getPOIPoint();
        TMapMarkerItem item = new TMapMarkerItem();
        item.setTMapPoint(point);
        Bitmap icon = ((BitmapDrawable)getResources().getDrawable(R.drawable.mapicon)).getBitmap();
        item.setIcon(icon);
        item.setPosition(0.5f, 1);

        mapView.addMarkerItem(poiItem.getPOIID(), item);
    }

    public void changeHeight() {
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int height = layout.getMeasuredHeight();
                ((PartyDetailActivity)getActivity()).setPagerHeight(height);
            }
        });
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

    class POIItem {
        TMapPOIItem poi;

        @Override
        public String toString() {
            return poi.getPOIName();
        }
    }
}

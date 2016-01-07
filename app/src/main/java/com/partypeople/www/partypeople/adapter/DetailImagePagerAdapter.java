package com.partypeople.www.partypeople.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.manager.NetworkManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-11-02.
 */
public class DetailImagePagerAdapter extends PagerAdapter {

    Context mContext;
    ArrayList<String> photos;
    boolean[] has_photos;
    List<View> scrappedView = new ArrayList<View>();

    public DetailImagePagerAdapter(Context context, ArrayList photos, boolean[] has_photos) {
        mContext = context;
        this.photos = photos;
        this.has_photos = has_photos;
    }

    @Override
    public int getCount() {
        int count=0;
        for(int i=0;i<has_photos.length;i++) {
            if(has_photos[i])
                count++;
        }
        return count;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view;
        ImageView image;
        if(scrappedView.size() > 0) {
            view = scrappedView.remove(0);
        } else {
            view = LayoutInflater.from(container.getContext()).inflate(R.layout.view_pager, container, false);
        }
        image = (ImageView)view.findViewById(R.id.test_image);
        Glide.with(mContext)
                .load(NetworkManager.getInstance().URL_SERVER + photos.get(position))
                .centerCrop()
                .placeholder(R.drawable.profile_img)
                .error(R.drawable.profile_img)
                .into(image);

        container.addView(view);
        return view;
    }

    @Override
    public float getPageWidth(int position) {
        return 1f;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View)object;
        container.removeView(view);
        scrappedView.add(view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}

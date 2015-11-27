package com.partypeople.www.partypeople.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-10-15.
 */
public class MainFragmentAdapter extends BaseAdapter {
    DateUtil dateUtil = DateUtil.getInstance();
    List<Party> items = new ArrayList<Party>();
    Context mContext;

    public MainFragmentAdapter(Context context) {
        mContext = context;
    }

    static class ViewHolder {
        DisplayImageOptions options;
        TextView titleView, dateView, locationView, priceView, progressView, dueDateView;
        CheckBox bookMarkView;
        ImageView imageTheme, imageParty;
        ProgressBar progressBar;
        Party mData;
        DateUtil dateUtil = DateUtil.getInstance();

        public void setItemData(Party data, Context context) {
            mData = data;

            //imageTheme.setImageResource();
            titleView.setText(data.name);
            dateView.setText(dateUtil.changeToViewFormat(data.start_at, data.end_at));
            dueDateView.setText(dateUtil.getDiffDay(dateUtil.getCurrentDate(), data.pay_end_at) + "일 남음");
            String[] array = data.location.split(" ");
            if(array.length==1)
                locationView.setText(array[0]);
            else
                locationView.setText(array[0] + " " + array[1]);
            priceView.setText((int)data.expect_pay + "원");
            int progress = (int)((data.members.size()*data.pay_method.get(0).price)/data.expect_pay*100);
            progressView.setText(progress+"% 모금됨");
            progressBar.setProgress(progress);
            bookMarkView.setChecked(data.bookmark);
            ImageLoader.getInstance().displayImage(NetworkManager.getInstance().URL_SERVER + data.photo, imageParty, options);
        }

        private void init(View view) {
            imageParty = (ImageView)view.findViewById(R.id.image_party);
            imageTheme = (ImageView)view.findViewById(R.id.img_theme);
            titleView = (TextView)view.findViewById(R.id.text_item_title);
            dateView = (TextView)view.findViewById(R.id.text_date);
            locationView = (TextView)view.findViewById(R.id.text_location);
            priceView = (TextView)view.findViewById(R.id.text_price);
            progressView = (TextView)view.findViewById(R.id.text_progress);
            dueDateView = (TextView)view.findViewById(R.id.text_duedate);
            bookMarkView = (CheckBox)view.findViewById(R.id.chbox_bookmark);
            progressBar = (ProgressBar)view.findViewById(R.id.progressBar);

            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_stub)
                    .showImageForEmptyUri(R.drawable.ic_empty)
                    .showImageOnFail(R.drawable.ic_error)
                    .cacheInMemory(true)
                    .cacheOnDisc(true)
                    .considerExifParams(true)
                    .build();
        }
    }

    public void add(Party item) {
        items.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
     public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.view_party_item, null);
            holder = new ViewHolder();
            holder.init(convertView);

            holder.bookMarkView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag();
                    items.get(getPosition).setBookmark(buttonView.isChecked());
                }
            });

            holder.setItemData(items.get(position), mContext);

            convertView.setTag(holder);
            convertView.setTag(R.id.image_party, holder.imageParty);
            convertView.setTag(R.id.img_theme, holder.imageTheme);
            convertView.setTag(R.id.text_item_title, holder.titleView);
            convertView.setTag(R.id.text_date, holder.dateView);
            convertView.setTag(R.id.text_location, holder.locationView);
            convertView.setTag(R.id.text_price, holder.priceView);
            convertView.setTag(R.id.text_progress, holder.progressView);
            convertView.setTag(R.id.text_duedate, holder.dueDateView);
            convertView.setTag(R.id.chbox_bookmark, holder.bookMarkView);
            convertView.setTag(R.id.progressBar, holder.progressBar);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.bookMarkView.setTag(position);

        holder.setItemData(items.get(position), mContext);

        return convertView;
    }
}

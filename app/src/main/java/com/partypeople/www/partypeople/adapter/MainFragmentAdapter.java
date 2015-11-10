package com.partypeople.www.partypeople.adapter;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.utils.DateUtil;
import com.partypeople.www.partypeople.view.PartyItemView;

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
        TextView titleView, dateView, locationView, priceView, progressView, dueDateView;
        ImageView partyImgView;
        CheckBox bookMarkView;
        ProgressBar progressBar;
        Party mData;
        DateUtil dateUtil = DateUtil.getInstance();

        public void setItemData(Party data) {
            mData = data;

            titleView.setText(data.name);
            if(data.date != null) {
                dateView.setText(dateUtil.changeToViewFormat(data.date));
                dueDateView.setText(dateUtil.getDiffDay(dateUtil.getCurrentDate(), data.date) + "일 남음");
            }
            locationView.setText(data.location);
            priceView.setText(data.expect_pay + "원");
//        progressView.setText(data.progressText);
            progressBar.setProgress(50);
            //bookMarkView.setClickable(false);
            bookMarkView.setChecked(data.bookmark);
        }

        public void setVisible(int visible) {
            partyImgView.setVisibility(visible);
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.view_party_item, null);
            holder = new ViewHolder();
            holder.partyImgView = (ImageView)convertView.findViewById(R.id.image_party);
            holder.titleView = (TextView)convertView.findViewById(R.id.text_item_title);
            holder.dateView = (TextView)convertView.findViewById(R.id.text_date);
            holder.locationView = (TextView)convertView.findViewById(R.id.text_location);
            holder.priceView = (TextView)convertView.findViewById(R.id.text_price);
            holder.progressView = (TextView)convertView.findViewById(R.id.text_progress);
            holder.dueDateView = (TextView)convertView.findViewById(R.id.text_duedate);
            holder.bookMarkView = (CheckBox)convertView.findViewById(R.id.chbox_bookmark);
            holder.progressBar = (ProgressBar)convertView.findViewById(R.id.progressBar);

            holder.bookMarkView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (Integer)buttonView.getTag();
                    items.get(getPosition).setBookmark(buttonView.isChecked());
                }
            });

            holder.setItemData(items.get(position));

            convertView.setTag(holder);
            convertView.setTag(R.id.image_party, holder.partyImgView);
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

        holder.titleView.setText(items.get(position).name);
        holder.bookMarkView.setChecked(items.get(position).bookmark);
        holder.partyImgView.setImageResource(R.drawable.demo_img);
        holder.locationView.setText(items.get(position).location);
        holder.priceView.setText(items.get(position).expect_pay + "원");
        holder.progressView.setText("50%");
        holder.progressBar.setProgress(50);
        if(items.get(position).date != null) {
            holder.dueDateView.setText(dateUtil.getDiffDay(dateUtil.getCurrentDate(), items.get(position).date) + "일 남음");
            holder.dateView.setText(dateUtil.changeToViewFormat(items.get(position).date));
        }

        return convertView;
    }
}

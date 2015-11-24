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
        CheckBox bookMarkView;
        ProgressBar progressBar;
        Party mData;
        DateUtil dateUtil = DateUtil.getInstance();

        public void setItemData(Party data) {
            mData = data;

            titleView.setText(data.name);
            dateView.setText(dateUtil.changeToViewFormat(data.end_at));
            dueDateView.setText(dateUtil.getDiffDay(dateUtil.getCurrentDate(), data.end_at) + "일 남음");
            locationView.setText(data.location);
            priceView.setText(data.expect_pay + "원");
//        progressView.setText(data.progressText);
            progressBar.setProgress(50);
            //bookMarkView.setClickable(false);
            bookMarkView.setChecked(data.bookmark);
        }

        private void init(View view) {
            titleView = (TextView)view.findViewById(R.id.text_item_title);
            dateView = (TextView)view.findViewById(R.id.text_date);
            locationView = (TextView)view.findViewById(R.id.text_location);
            priceView = (TextView)view.findViewById(R.id.text_price);
            progressView = (TextView)view.findViewById(R.id.text_progress);
            dueDateView = (TextView)view.findViewById(R.id.text_duedate);
            bookMarkView = (CheckBox)view.findViewById(R.id.chbox_bookmark);
            progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
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
            holder.init(convertView);

            holder.bookMarkView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (Integer)buttonView.getTag();
                    //items.get(getPosition).setBookmark(buttonView.isChecked());
                }
            });

            holder.setItemData(items.get(position));

            convertView.setTag(holder);
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

        holder.setItemData(items.get(position));

        return convertView;
    }
}

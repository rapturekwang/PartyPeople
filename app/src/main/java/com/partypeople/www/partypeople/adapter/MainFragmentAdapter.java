package com.partypeople.www.partypeople.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.data.Like;
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.manager.PropertyManager;
import com.partypeople.www.partypeople.utils.DateUtil;
import com.partypeople.www.partypeople.utils.NumberUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-10-15.
 */
public class MainFragmentAdapter extends BaseAdapter {
    List<Party> items = new ArrayList<Party>();
    Context mContext;

    public MainFragmentAdapter(Context context) {
        mContext = context;
    }

    static class ViewHolder {
        TextView titleView, dateView, locationView, priceView, progressView, dueDateView;
        CheckBox bookMarkView;
        ImageView imageParty;
        ProgressBar progressBar;
        Party mData;
        DateUtil dateUtil = DateUtil.getInstance();

        int[] ids = {0,
                R.drawable.main_theme_1,
                R.drawable.main_theme_2,
                R.drawable.main_theme_3,
                R.drawable.main_theme_4,
                R.drawable.main_theme_5};

        public void setItemData(Party data, Context context) {
            mData = data;

            titleView.setText(data.name);
            titleView.setCompoundDrawablesWithIntrinsicBounds(ids[data.themes[0]], 0, 0, 0);
            dateView.setText(dateUtil.changeToViewFormat(data.start_at));
            int dueDate = dateUtil.getDiffDay(dateUtil.getCurrentDate(), data.amount_end_at);
            int dueHour = dateUtil.getDiffHour(dateUtil.getCurrentDate(), data.amount_end_at);
            dueDateView.setText(dueDate>=1 ? "남은시간 " + dueDate + "일" : dueDate==0 ? "남은시간 " + dueHour + "시간" : "종료");
            if(data.location!=null) {
                String[] array = data.location.split(" ");
                if (array.length == 1)
                    locationView.setText(array[0]);
                else
                    locationView.setText(array[0] + " " + array[1]);
            } else {
                locationView.setText("");
            }
            priceView.setText("모인 금액 " + NumberUtil.getInstance().changeToPriceForm((int)data.amount_total) + "원");
            int progress = (int)(data.amount_total/data.amount_expect * 100);
            progressView.setText(progress + "%");
            progressBar.setProgress(progress);
            bookMarkView.setChecked(data.bookmark);
            if(data.likes!=null) {
                bookMarkView.setText("" + data.likes.size());
                if (PropertyManager.getInstance().isLogin() && data.likes.size() > 0) {
                    for (int i = 0; i < data.likes.size(); i++) {
                        if (data.likes.get(i).user.equals(PropertyManager.getInstance().getUser().id)) {
                            data.setBookmark(true);
                            bookMarkView.setChecked(true);
                            break;
                        }
                    }
                }
            }else {
                bookMarkView.setText("0");
            }
            Glide.with(context)
                    .load(NetworkManager.getInstance().URL_SERVER + data.photos.get(0))
                    .placeholder(R.color.defaultImage)
                    .error(R.color.defaultImage)
                    .into(imageParty);
        }

        private void init(View view) {
            imageParty = (ImageView)view.findViewById(R.id.image_party);
            titleView = (TextView)view.findViewById(R.id.text_item_title);
            dateView = (TextView)view.findViewById(R.id.text_date);
            locationView = (TextView)view.findViewById(R.id.text_location);
            priceView = (TextView)view.findViewById(R.id.text_payment);
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
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.view_party_item, null);
            holder = new ViewHolder();
            holder.init(convertView);

            holder.bookMarkView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(!PropertyManager.getInstance().isLogin()) {
                        Toast.makeText(mContext, "로그인이 필요한 서비스 입니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(buttonView.getTag()==null)
                        return;
                    int getPosition = (Integer) buttonView.getTag();
                    boolean update = false;
                    if(items.get(getPosition).isBookmark() != buttonView.isChecked()) {
                        update = true;
                    }
                    items.get(getPosition).setBookmark(buttonView.isChecked());

                    if(update && isChecked) {
                        NetworkManager.getInstance().takeLike(mContext, holder.mData.id, new NetworkManager.OnResultListener<String>() {
                            @Override
                            public void onSuccess(String result) {
                                Toast.makeText(mContext, "즐겨찾기가 추가되었습니다", Toast.LENGTH_SHORT).show();
                                Like like = new Like();
                                like.user = PropertyManager.getInstance().getUser().id;
                                holder.mData.likes.add(like);
                                holder.setItemData(holder.mData, mContext);
                            }

                            @Override
                            public void onFail(String response) {

                            }
                        });
                    }
                    if(update && !isChecked) {
                        NetworkManager.getInstance().takeUnlike(mContext, holder.mData.id, new NetworkManager.OnResultListener<String>() {
                            @Override
                            public void onSuccess(String result) {
                                Toast.makeText(mContext, "즐겨찾기가 취소되었습니다", Toast.LENGTH_SHORT).show();
                                if(holder.mData.likes.size() > 0) {
                                    for (int i = 0; i < holder.mData.likes.size(); i++) {
                                        if (holder.mData.likes.get(i).user.equals(PropertyManager.getInstance().getUser().id)) {
                                            holder.mData.likes.remove(i);
                                        }
                                    }
                                }
                                holder.setItemData(holder.mData, mContext);
                            }

                            @Override
                            public void onFail(String response) {

                            }
                        });
                    }
                }
            });

            holder.setItemData(items.get(position), mContext);

            convertView.setTag(holder);
            convertView.setTag(R.id.image_party, holder.imageParty);
            convertView.setTag(R.id.text_item_title, holder.titleView);
            convertView.setTag(R.id.text_date, holder.dateView);
            convertView.setTag(R.id.text_location, holder.locationView);
            convertView.setTag(R.id.text_payment, holder.priceView);
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

    public void removeAll() {
        items.clear();
    }
}

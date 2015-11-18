package com.partypeople.www.partypeople.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.FollowActivity;
import com.partypeople.www.partypeople.activity.MessageActivity;
import com.partypeople.www.partypeople.activity.UserActivity;
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.manager.PropertyManager;
import com.partypeople.www.partypeople.view.ItemTabWidget;
import com.partypeople.www.partypeople.view.UserPagePartyItemView;

import java.util.ArrayList;

public class UserAdapter extends BaseAdapter {

    ArrayList<Party> items = new ArrayList<Party>();
    Context mContext;
    private static final int VIEW_TYPE_COUNT = 3;
    private static final int SUMMARY_VIEW = 0;
    private static final int TAB_WIDGET_VIEW = 1;
    private static final int TEXT_VIEW = 2;

    int currentIndex;
    TabHost.OnTabChangeListener mTabListener;

    public UserAdapter(Context context, int index,
                       TabHost.OnTabChangeListener listener) {
        mContext = context;
        currentIndex = index;
        mTabListener = listener;
    }

    public void add(Party data) {
        items.add(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size() + 2;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return SUMMARY_VIEW;
            case 1:
                return TAB_WIDGET_VIEW;
            default:
                return TEXT_VIEW;
        }
    }

    @Override
    public Object getItem(int position) {
        switch (position) {
            case 0:
            case 1:
                return null;
            default:
                return items.get(position - 2);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (position) {
            case 0:
                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.view_user_header, null);
                    ImageView btn = (ImageView)convertView.findViewById(R.id.btn_back);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((UserActivity) mContext).finish();
                        }
                    });
                    btn = (ImageView)convertView.findViewById(R.id.btn_modify);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(mContext, "modify", Toast.LENGTH_SHORT).show();
                        }
                    });
                    TextView textBtn = (TextView)convertView.findViewById(R.id.text_btn_message);
                    textBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mContext.startActivity(new Intent(mContext, MessageActivity.class));
                        }
                    });
                    textBtn = (TextView)convertView.findViewById(R.id.text_btn_follow);
                    textBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mContext.startActivity(new Intent(mContext, FollowActivity.class));
                        }
                    });
                    TextView textView = (TextView)convertView.findViewById(R.id.text_name);
                    textView.setText(PropertyManager.getInstance().getUser().data.name);

//                    textView = (TextView)convertView.findViewById(R.id.text_email);
//                    textView.setText(PropertyManager.getInstance().getUser().data.email);
                }
                return convertView;
            case 1:
                ItemTabWidget itw = (ItemTabWidget)convertView;
                if (itw == null) {
                    itw = new ItemTabWidget(mContext);
                    itw.setOnTabChangeListener(mTabListener);
                }
                itw.setCurrentTab(currentIndex);
                return itw;
            default:
                UserPagePartyItemView view = (UserPagePartyItemView)convertView;
                if (view == null) {
                    view = new UserPagePartyItemView(parent.getContext());
                } else {
                    view = (UserPagePartyItemView)convertView;
                }
                view.setItemData(items.get(position-2));

                return view;
        }
    }

}

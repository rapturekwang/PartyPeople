//package com.partypeople.www.partypeople.adapter;
//
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//
//import com.partypeople.www.partypeople.data.PayMethod;
//import com.partypeople.www.partypeople.view.RewordItemView;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by Tacademy on 2015-11-18.
// */
//public class MakePartyChildAdapter extends BaseAdapter{
//    List<PayMethod> items = new ArrayList<PayMethod>();
//    Context mContext;
//
//    public MakePartyChildAdapter(Context context) {
//        mContext = context;
//    }
//
//    @Override
//    public int getCount() {
//        return items.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return items.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        RewordItemView view;
//        if(convertView == null) {
//            view = new RewordItemView(parent.getContext());
//        } else {
//            view = (RewordItemView)convertView;
//        }
////        view.setItemData();
//        return view;
//    }
//
//    public void add(PayMethod item) {
//        items.add(item);
//        notifyDataSetChanged();
//    }
//}

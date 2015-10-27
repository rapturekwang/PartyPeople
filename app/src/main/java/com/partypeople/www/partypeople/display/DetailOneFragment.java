package com.partypeople.www.partypeople.display;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;

import org.w3c.dom.Text;

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
}

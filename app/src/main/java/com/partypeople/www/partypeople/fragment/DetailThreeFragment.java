package com.partypeople.www.partypeople.fragment;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.PartyDetailActivity;
import com.partypeople.www.partypeople.adapter.CommentAdapter;
import com.partypeople.www.partypeople.adapter.RewordViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailThreeFragment extends Fragment {
    private static final String ARG_NAME = "name";
    private String mName;
    ListView listView;
    CommentAdapter mAdapter;
    LinearLayout layout;

    public static DetailThreeFragment newInstance(String name) {
        DetailThreeFragment fragment = new DetailThreeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public DetailThreeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_detail_three, container, false);

        layout = (LinearLayout)view.findViewById(R.id.root_layout);

        final EditText editText = (EditText)view.findViewById(R.id.edit_comment);
        ImageView imgSendView = (ImageView)view.findViewById(R.id.image_btn_send);
        imgSendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.add(editText.getText().toString());
                PartyDetailActivity activity = (PartyDetailActivity)getActivity();
                activity.party.comments.add(editText.getText().toString());
                changeHeight();
                editText.setText("");
            }
        });

        listView = (ListView)view.findViewById(R.id.listView);
        mAdapter = new CommentAdapter();
        listView.setAdapter(mAdapter);

        return view;
    }

    public void changeHeight() {
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int height = layout.getMeasuredHeight();
                Point size = new Point();
                getActivity().getWindowManager().getDefaultDisplay().getSize(size);
                int screenHeight = size.y - (int)Math.ceil((57 + 25 + 53) * getContext().getResources().getDisplayMetrics().density);
                if(screenHeight > height)
                    height = screenHeight;
                ((PartyDetailActivity)getActivity()).setPagerHeight(height);
            }
        });
    }
}

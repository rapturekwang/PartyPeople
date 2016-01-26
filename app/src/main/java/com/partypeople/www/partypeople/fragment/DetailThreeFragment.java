package com.partypeople.www.partypeople.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.PartyDetailActivity;
import com.partypeople.www.partypeople.adapter.CommentAdapter;
import com.partypeople.www.partypeople.data.Comment;
import com.partypeople.www.partypeople.data.PartyResult;
import com.partypeople.www.partypeople.dialog.DeleteCommentDialog;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.utils.DateUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailThreeFragment extends Fragment {
    private static final String ARG_NAME = "name";
    private String mName;
    ListView beforeListView, afterListView;
    CommentAdapter mBeforeAdapter, mAfterAdapter;
    LinearLayout layout;
    PartyDetailActivity activity;
    TextView textDivider;
    View divider;
    int lineCount=1;

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
        activity = (PartyDetailActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_three, container, false);
        final DetailThreeFragment fragment = this;

        layout = (LinearLayout)view.findViewById(R.id.root_layout);

        final EditText editText = (EditText)view.findViewById(R.id.edit_comment);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editText.getLineCount()!=lineCount) {
                    lineCount = editText.getLineCount();
                    activity.setPagerHeight(1000 + 1000 * activity.party.comments.size());
                    changeHeight();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        TextView imgSendView = (TextView)view.findViewById(R.id.image_btn_send);
        imgSendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().equals("")) {
                    NetworkManager.getInstance().addComment(getContext(), activity.party.id, editText.getText().toString(),
                        new NetworkManager.OnResultListener<String>() {
                            @Override
                            public void onSuccess(String result) {
                                NetworkManager.getInstance().getParty(getContext(), activity.party.id, new NetworkManager.OnResultListener<PartyResult>() {
                                    @Override
                                    public void onSuccess(PartyResult result) {
                                        activity.setParty(result.data);
                                        editText.setText("");
                                        initData();
                                        activity.setPagerHeight(1000 + 1000 * activity.party.comments.size());
                                        changeHeight();
                                        hideKeyboard();
                                    }

                                    @Override
                                    public void onFail(int code) {
                                        Toast.makeText(getContext(), "통신상태가 원활하지 않습니다", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onFail(int code) {
                                Toast.makeText(getContext(), "통신상태가 원활하지 않습니다", Toast.LENGTH_SHORT).show();
                            }
                        });
                }
            }
        });

        beforeListView = (ListView)view.findViewById(R.id.comment_list_before);
        mBeforeAdapter = new CommentAdapter();
        beforeListView.setAdapter(mBeforeAdapter);

        afterListView = (ListView)view.findViewById(R.id.comment_list_after);
        mAfterAdapter = new CommentAdapter();
        afterListView.setAdapter(mAfterAdapter);

        textDivider = (TextView)view.findViewById(R.id.text_divider);
        divider = view.findViewById(R.id.divider);

        beforeListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                DeleteCommentDialog dialog = new DeleteCommentDialog(getContext(), activity, fragment, (Comment)mBeforeAdapter.getItem(position));
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                return false;
            }
        });

        afterListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                DeleteCommentDialog dialog = new DeleteCommentDialog(getContext(), activity, fragment, (Comment)mAfterAdapter.getItem(position));
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                return false;
            }
        });

        initData();

        return view;
    }

    public void changeHeight() {
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int height = layout.getMeasuredHeight();
                activity.setPagerHeight(height);
            }
        });
    }

    public void initData() {
        mBeforeAdapter.removeAll();
        mAfterAdapter.removeAll();

        for(int i=0;i<activity.party.comments.size();i++) {
            for(int j=0;j<activity.party.members.size();j++) {
                if(activity.party.members.get(j).id.equals(activity.party.comments.get(i).from.id)) {
                    activity.party.comments.get(i).participant = true;
                    break;
                }
            }
            if(DateUtil.getInstance().compare(activity.party.comments.get(i).created_at, activity.party.start_at)) {
                mBeforeAdapter.add(activity.party.comments.get(i));
            } else {
                mAfterAdapter.add(activity.party.comments.get(i));
            }
        }
        if(mAfterAdapter.getCount()>0 && mBeforeAdapter.getCount()>0) {
            textDivider.setVisibility(View.VISIBLE);
            divider.setVisibility(View.VISIBLE);
            afterListView.setVisibility(View.VISIBLE);
        } else if(mAfterAdapter.getCount()>0) {
            afterListView.setVisibility(View.VISIBLE);
            beforeListView.setVisibility(View.INVISIBLE);
        } else if(mBeforeAdapter.getCount()>0) {
            afterListView.setVisibility(View.INVISIBLE);
            beforeListView.setVisibility(View.VISIBLE);
        }
    }

    private void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}

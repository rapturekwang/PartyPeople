package com.partypeople.www.partypeople.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.partypeople.www.partypeople.data.Comments;
import com.partypeople.www.partypeople.data.PartyResult;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.manager.PropertyManager;
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

        layout = (LinearLayout)view.findViewById(R.id.root_layout);

        final EditText editText = (EditText)view.findViewById(R.id.edit_comment);
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
                                        activity.setPagerHeight(350 + 300 * activity.party.comments.size());
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
        divider = (View)view.findViewById(R.id.divider);

        beforeListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("댓글을 지우시겠습니까?");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (((Comments) mBeforeAdapter.getItem(position)).from.id.equals(PropertyManager.getInstance().getUser().id)) {
                            NetworkManager.getInstance().removeComment(getContext(), ((Comments) mBeforeAdapter.getItem(position)).id, new NetworkManager.OnResultListener<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    NetworkManager.getInstance().getParty(getContext(), activity.party.id, new NetworkManager.OnResultListener<PartyResult>() {
                                        @Override
                                        public void onSuccess(PartyResult result) {
                                            activity.setParty(result.data);
                                            editText.setText("");
                                            initData();
                                            activity.setPagerHeight(350 + 300 * activity.party.comments.size());
                                            changeHeight();
                                        }

                                        @Override
                                        public void onFail(int code) {
                                            Toast.makeText(getContext(), "통신상태가 원활하지 않습니다", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onFail(int code) {
                                    Toast.makeText(getContext(), "통신이 월활하지 않습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "본인의 댓글이 아닙니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();

                return false;
            }
        });

        afterListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("댓글을 지우시겠습니까?");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(((Comments)mAfterAdapter.getItem(position)).from.id.equals(PropertyManager.getInstance().getUser().id)){
                            NetworkManager.getInstance().removeComment(getContext(), ((Comments) mAfterAdapter.getItem(position)).id, new NetworkManager.OnResultListener<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    NetworkManager.getInstance().getParty(getContext(), activity.party.id, new NetworkManager.OnResultListener<PartyResult>() {
                                        @Override
                                        public void onSuccess(PartyResult result) {
                                            activity.setParty(result.data);
                                            editText.setText("");
                                            initData();
                                            activity.setPagerHeight(350 + 300 * activity.party.comments.size());
                                            changeHeight();
                                        }

                                        @Override
                                        public void onFail(int code) {
                                            Toast.makeText(getContext(), "통신상태가 원활하지 않습니다", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onFail(int code) {
                                    Toast.makeText(getContext(), "통신이 월활하지 않습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "본인의 댓글이 아닙니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();

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

    private void initData() {
        mBeforeAdapter.removeAll();
        mAfterAdapter.removeAll();
//        if(DateUtil.getInstance().compare(activity.party.start_at, DateUtil.getInstance().getCurrentDate())) {
//        }
        for(int i=0;i<activity.party.comments.size();i++) {
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
        }
    }

    private void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}

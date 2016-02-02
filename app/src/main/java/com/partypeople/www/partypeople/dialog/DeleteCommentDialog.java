package com.partypeople.www.partypeople.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.PartyDetailActivity;
import com.partypeople.www.partypeople.data.Comment;
import com.partypeople.www.partypeople.data.PartyResult;
import com.partypeople.www.partypeople.fragment.DetailThreeFragment;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.manager.PropertyManager;

/**
 * Created by kwang on 16. 1. 5..
 */
public class DeleteCommentDialog extends Dialog{
    public DeleteCommentDialog(final Context context, final PartyDetailActivity activity, final DetailThreeFragment fragment, final Comment comment) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_delete_comment);

        ImageView btn = (ImageView)findViewById(R.id.btn_delete);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comment.from.id.equals(PropertyManager.getInstance().getUser().id)) {
                    NetworkManager.getInstance().removeComment(getContext(), comment.id, new NetworkManager.OnResultListener<String>() {
                        @Override
                        public void onSuccess(String result) {
                            NetworkManager.getInstance().getParty(getContext(), activity.party.id, new NetworkManager.OnResultListener<PartyResult>() {
                                @Override
                                public void onSuccess(PartyResult result) {
                                    activity.setParty(result.data);
                                    fragment.initData();
                                    activity.setPagerHeight(1000 + 1000 * activity.party.comments.size());
                                    fragment.changeHeight();
                                    dismiss();
                                }

                                @Override
                                public void onFail(String response) {
                                    Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onFail(String response) {
                            Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "본인의 댓글이 아닙니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn = (ImageView)findViewById(R.id.btn_cancel);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}

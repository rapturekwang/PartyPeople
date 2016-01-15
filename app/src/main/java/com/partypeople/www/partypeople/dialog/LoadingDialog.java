package com.partypeople.www.partypeople.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.partypeople.www.partypeople.R;

import java.io.Serializable;

/**
 * Created by kwang on 15. 12. 23..
 */
public class LoadingDialog extends Dialog implements Serializable{
    public LoadingDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_loading);
    }
}

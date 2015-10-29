package com.partypeople.www.partypeople.view;

import android.content.Context;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;

/**
 * Created by kwang on 15. 10. 9..
 */
public class GridItemView extends FrameLayout implements Checkable{
    boolean isCheck;
    public GridItemView(Context context) {
        super(context);
        init();
    }

    @Override
    public boolean isChecked() {
        return isCheck;
    }

    @Override
    public void setChecked(boolean checked) {
        isCheck = checked;
    }

    @Override
    public void toggle() {
        isCheck = !isCheck;
    }

    TextView textView;

    private void init() {
        inflate(getContext(), R.layout.view_grid_item, this);
        textView = (TextView)findViewById(R.id.text_theme);
    }

    public void setGridItem(String title) {
        textView.setText(title);
    }
}


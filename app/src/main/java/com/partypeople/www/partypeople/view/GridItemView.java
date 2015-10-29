package com.partypeople.www.partypeople.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;

/**
 * Created by kwang on 15. 10. 9..
 */
public class GridItemView extends FrameLayout implements Checkable{
    boolean isCheck = false;

    public GridItemView(Context context) {
        super(context);
        init();
    }

    public GridItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public boolean isChecked() {
        return isCheck;
    }

    @Override
    public void setChecked(boolean checked) {
        if (isCheck != checked) {
            isCheck = checked;
            drawCheck();
        }
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

    private void drawCheck() {
        if (isCheck) {
            textView.setBackgroundColor(getResources().getColor(R.color.checked));
        } else {
            textView.setBackgroundColor(getResources().getColor(R.color.unchecked));
        }
    }

    public void setGridItem(String title) {
        textView.setText(title);
    }
}


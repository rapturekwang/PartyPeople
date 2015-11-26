package com.partypeople.www.partypeople.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;

/**
 * Created by kwang on 15. 10. 9..
 */
public class ThemeItemView extends FrameLayout implements Checkable{
    boolean isCheck = false;

    public ThemeItemView(Context context) {
        super(context);
        init();
    }

    public ThemeItemView(Context context, AttributeSet attrs) {
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

    ImageView imageView;
    ImageView choiceView;

    private void init() {
        inflate(getContext(), R.layout.view_theme_item, this);
        imageView = (ImageView)findViewById(R.id.image_theme);
        choiceView = (ImageView)findViewById(R.id.image_choice);
    }

    private void drawCheck() {
        if (isCheck) {
            choiceView.setVisibility(View.VISIBLE);
        } else {
            choiceView.setVisibility(View.INVISIBLE);
        }
    }

    public void setGridItem(int theme) {
        imageView.setImageResource(theme);
    }
}


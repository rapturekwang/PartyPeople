package com.partypeople.www.partypeople.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwang on 15. 11. 15..
 */
public class MainTabHeaderView extends LinearLayout {
    public MainTabHeaderView(Context context) {
        super(context);
        init();
    }

    public MainTabHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    TextView locationView;
    List<ImageView> listTheme = new ArrayList<ImageView>();

    private void init() {
        inflate(getContext(), R.layout.view_maintab_header, this);
        locationView = (TextView)findViewById(R.id.text_location);
        listTheme.add((ImageView)findViewById(R.id.image_theme1));
        listTheme.add((ImageView)findViewById(R.id.image_theme2));
        listTheme.add((ImageView)findViewById(R.id.image_theme3));
        listTheme.add((ImageView)findViewById(R.id.image_theme4));
    }

    public void setItemData(String location, int[] theme) {
         int[] ids = {R.drawable.main_theme_0,
                R.drawable.main_theme_1,
                R.drawable.main_theme_2,
                R.drawable.main_theme_3,
                R.drawable.main_theme_4,
                R.drawable.main_theme_5};

        locationView.setText(location);
        if(theme != null) {
            if(theme[0]==0) {
                listTheme.get(0).setImageResource(ids[theme[0]]);
                listTheme.get(1).setVisibility(View.INVISIBLE);
                listTheme.get(2).setVisibility(View.INVISIBLE);
                listTheme.get(3).setVisibility(View.INVISIBLE);
                return;
            }
            for(int i=0; i<4; i++) {
                if(i<theme.length) {
                    listTheme.get(i).setImageResource(ids[theme[i]]);
                } else {
                    listTheme.get(i).setVisibility(View.INVISIBLE);
                }
            }
        }
    }
}

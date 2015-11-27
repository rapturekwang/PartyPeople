package com.partypeople.www.partypeople.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
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
         int[] ids = {R.drawable.theme_0,
                R.drawable.theme_1,
                R.drawable.theme_2,
                R.drawable.theme_3,
                R.drawable.theme_4,
                R.drawable.theme_5};

        locationView.setText(location);
        if(theme != null) {
//            Toast.makeText(getContext(), theme[0], Toast.LENGTH_SHORT).show();
            for(int i=0; i<theme.length; i++) {
                Log.d("MainTab", ""+theme[i]);
                listTheme.get(i).setImageResource(ids[theme[i]]);
            }
        }
    }
}

package com.partypeople.www.partypeople.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;

/**
 * Created by kwang on 15. 12. 28..
 */
public class PhotoView extends RelativeLayout{
    ImageView imageView, imageBtn;
    TextView textView;

    public PhotoView(Context context) {
        super(context);
        init();
    }

    public PhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_photo, this);
        imageView = (ImageView)findViewById(R.id.image_view);
        imageBtn = (ImageView)findViewById(R.id.img_btn_photo);
        textView = (TextView)findViewById(R.id.text_view);
    }

    public void setItemData(Bitmap img, int num) {
        imageView.setImageBitmap(img);
        imageBtn.setVisibility(INVISIBLE);
        textView.setVisibility(INVISIBLE);
    }

    public void changeBtnImg() {
        imageBtn.setImageResource(R.drawable.add_photo_btn);
    }
}

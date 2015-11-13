package com.partypeople.www.partypeople.manager;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

/**
 * Created by Tacademy on 2015-10-14.
 */
public class FontManager {
    private static FontManager instance;

    private static Object obj = new Object();

    public static FontManager getInstance() {
        synchronized (obj) {
            if (instance == null) {
                instance = new FontManager();
            }
        }
        return instance;
    }

    public static void setDefaultFont(Context context,
                                      String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(),
                fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }

    protected static void replaceFont(String staticTypefaceFieldName,
                                      final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class
                    .getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    Typeface nanum, noto, roboto;

    public static final String NANUM = "nanu";
    public static final String NOTO = "noto";
    public static final String SPOQA = "spoqa";

    private FontManager() {
    }

    public Typeface getTypeface(Context context, String fontName) {
        if(NANUM.equals(fontName)) {
            if(nanum == null) {
                nanum = Typeface.createFromAsset(context.getAssets(), "nanumgothic.ttf");
            }
            return nanum;
        } else if(NOTO.equals(fontName)) {
            if(noto == null) {
                noto = Typeface.createFromAsset(context.getAssets(), "NotoSansKR-Regular.otf");
            }
            return noto;
        } else if(SPOQA.equals(fontName)) {
            if (roboto == null) {
                roboto = Typeface.createFromAsset(context.getAssets(), "Spoqa_Han_Sans_Bold_win_subset.ttf");
            }
            return roboto;
        }
        return null;
    }
}

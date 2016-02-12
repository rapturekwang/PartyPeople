package com.partypeople.www.partypeople.utils;

import java.text.DecimalFormat;

/**
 * Created by kwang on 16. 2. 11..
 */
public class NumberUtil {
    private static NumberUtil instance;
    public static NumberUtil getInstance() {
        if (instance == null) {
            instance = new NumberUtil();
        }
        return instance;
    }

    public String changeToPriceForm(int num) {
        String result = null;
        DecimalFormat Commas = new DecimalFormat("#,###");
        result = Commas.format(num);

        return result;
    }

    public String changeToPriceForm(String num) {
        String result = null;
        if(num == null || num.equals("")) {
            result = "";
        } else {
            result = changeToPriceForm(Integer.parseInt(num));
        }

        return result;
    }
}

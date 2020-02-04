package com.stack.wotr.utils;

import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Common {

    private static final String TAG = Common.class.getSimpleName();

    public static int getDays(int month, int year) {
        Log.i(TAG, "getDays: month "+month+" year "+year);
        Calendar monthStart = new GregorianCalendar(year, month, 0);
        int daysCount= monthStart.getActualMaximum(Calendar.DAY_OF_MONTH);
        Log.i(TAG, "getDays: Days "+daysCount);
        return daysCount;
    }
}

package com.util;
public class NoDoubleClickListener{

    private static long lastClickTime;
    public static boolean isFastDoubleClick() {

        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 1000) {
            return true;
        }
        lastClickTime = time;
            return false;

        }

}

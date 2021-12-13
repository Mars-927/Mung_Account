package com.util;
import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

public class ToastUtil {
    static Toast toast = null;
    public static void toastShort(Context context, String text) {
        ToastUtil.show(context,text,Toast.LENGTH_SHORT);
    }


    public static void toastLong(Context context, String text) {
        ToastUtil.show(context,text,Toast.LENGTH_LONG);
    }


    public static void show(Context context, String text,int Length) {
        try {
            if(toast!=null){
                toast.setText(text);
            }else{
                toast= Toast.makeText(context, text, Length);
            }
            toast.show();
        } catch (Exception e) {
            //解决在子线程中调用Toast的异常情况处理
            Looper.prepare();
            Toast.makeText(context, text, Length).show();
            Looper.loop();
        }
    }


}
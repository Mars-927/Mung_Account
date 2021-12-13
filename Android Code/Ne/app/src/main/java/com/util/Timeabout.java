package com.util;

import android.util.Log;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

public class Timeabout {
    public static Boolean daysBetween(Date Time1, Date Time2,int Max_day){
        // 本方法用于返回相差天数 |Time1 - Time2| > Max_Day ? Yes : No;
        long T1 = Time1.getTime()/1000;
        long T2 = Time2.getTime()/1000;
        long result = T1>T2?T1-T2:T2-T1;
        long day = result / 86400;
        if(day<Max_day){
            return Boolean.TRUE;
        }
        else{
            return Boolean.FALSE;
        }
    }

    public static Date YMDToDate(int Year, int Month, int Day){
        LocalDate date = LocalDate.of(Year,Month,Day);
        Date MyGet = Date.from(date.atStartOfDay(ZoneOffset.ofHours(0)).toInstant());

        return MyGet;
    }
}

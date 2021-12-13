package com.util;

import com.mungbill.Init.Initial;
import java.util.Random;

import java.util.Date;

public class GenerateOnlyId {
    public static String Get(){
        Random r = new Random();
        String Time = Long.toString(new Date().getTime());
        String UserId = Initial.UserName;
        String Random = Integer.toString(r.nextInt(99999999));
        return Time + UserId + Random ;
    }
}

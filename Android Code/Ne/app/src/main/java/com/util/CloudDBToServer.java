package com.util;

import static com.mungbill.Init.Initial.UserName;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.google.gson.Gson;
import com.mungbill.Account.AccountClass;
import com.mungbill.DataBase.DatabaseHelper;
import com.mungbill.Init.Initial;
import com.mungbill.LoginSignin.LoginSignin;
import com.mungbill.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class CloudDBToServer {
    public static void Start(Context InActivity){
        List<AccountClass> GETAll = new ArrayList() ;
        DatabaseHelper databaseHelper = new DatabaseHelper(InActivity);
        GETAll = databaseHelper.syncGetAll();
        Gson gson = new Gson();
        String jsonBDID = gson.toJson(GETAll);

        RequestBody requestBody = new FormBody.Builder()
                .add("userid" , Initial.UserName)
                .add("Detail" , jsonBDID)
                .build();
        Request request = new Request.Builder()
                .url(InActivity.getString(R.string.synctoserver))
                .post(requestBody) //return method("post", null);
                .build();
        OkHttpClient okhttp = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .build();//连接超时;
        Call call = okhttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 连接不到服务器

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 服务器响应正常
            }
        });
    }
}

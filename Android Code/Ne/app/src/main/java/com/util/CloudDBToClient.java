package com.util;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mungbill.Account.AccountClass;
import com.mungbill.Account.GetPostField;
import com.mungbill.DataBase.DatabaseHelper;
import com.mungbill.Init.Initial;
import com.mungbill.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CloudDBToClient {
    // 从云服务器获取数据 更新到本地数据库
    public static void Start(Context InActivity){
        List<AccountClass> GETAll = new ArrayList() ;
        DatabaseHelper databaseHelper = new DatabaseHelper(InActivity);
        GETAll = databaseHelper.syncGetAll();

        RequestBody requestBody = new FormBody.Builder()
                .add("userid" , Initial.UserName)
                .build();
        Request request = new Request.Builder()
                .url(InActivity.getString(R.string.synctoclient))
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
                // 测试
                List<GetPostField> Result = new ArrayList<>();
                Gson gson = new Gson();
                String Get = response.body().toString();
                Map<String, String> map = gson.fromJson(Get, new TypeToken<Map<String, String>>() {}.getType());
                if(map.get("error").equals("0")){
                    //-------------
                    String Detail = map.get("Detail");
                    Detail = Detail.substring(1, Detail.length());
                    Detail = Detail.substring(1, Detail.length()-1);

                    String Splituse = "},{";
                    String[] strArray = Detail.split(Splituse);
                    for(int i = 0 ;i<strArray.length;i++){
                        Pattern p = Pattern.compile("\\{([^}]*)\\}");
                        Matcher m = p.matcher(strArray[i]);
                        while (m.find()) {
                            GetPostField model = new Gson().fromJson(m.group(1), GetPostField.class);
                            Result.add(model);
                            Log.i("CLD",m.group(1));
                        }

                    }



                    //--------------
                }
            }
        });


    }
}

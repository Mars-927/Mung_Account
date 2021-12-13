package com.mungbill.LoginSignin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mungbill.DataBase.DatabaseHelper;
import com.mungbill.Init.Initial;
import com.mungbill.R;
import com.util.NoDoubleClickListener;
import com.util.ToastUtil;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginSignin extends AppCompatActivity {
    private Button button;          // 登录按钮
    private TextView TV;            // 注册账号
    private EditText Edit_Username; // 账号输入框
    private EditText Edit_Password; // 密码输入框
    private String LoginPost_Url = "http://10.0.2.2:8000/user/login";        // Post Url


    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化DB
        databaseHelper = new DatabaseHelper(LoginSignin.this);
        //去掉顶部标题
        //getSupportActionBar().hide();     # 此代码在转移中出现错误 所以注释掉
        Window window = getWindow();
        //全透明状态栏实现沉浸式
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setStatusBarColor(Color.TRANSPARENT);
        //设置状态栏图标和文字颜色为黑色
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_login_signin);
        //获取组件
        Edit_Username = findViewById(R.id.editText1);
        Edit_Password = findViewById(R.id.editText2);
        button =findViewById(R.id.button);
        TV = findViewById(R.id.Signin);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (NoDoubleClickListener.isFastDoubleClick()) {
                    // 避免重复点击
                    return;
                }
                String EU_Value = Edit_Username.getText().toString();
                String EP_Value = Edit_Password.getText().toString();
                if(EU_Value.length() == 0){
                    Edit_Username.requestFocus();
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    Edit_Username.startAnimation(shake);
                }
                else if(EP_Value.length() == 0){
                    Edit_Password.requestFocus();
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    Edit_Password.startAnimation(shake);
                }
                else {
                    RequestBody requestBody = new FormBody.Builder()
                            .add("userid" , EU_Value)
                            .add("password" , EP_Value)
                            .build();
                    Request request = new Request.Builder()
                            .url(LoginSignin.this.getString(R.string.LoginPost_Url))
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
                            ToastUtil.toastShort(LoginSignin.this,"服务器错误");
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            // 服务器响应正常
                            try{
                                JSONObject GetInfoJson= new JSONObject(response.body().string());
                                if( GetInfoJson.get("error").toString().equals("0") ){
                                    // 登录正常
                                    Initial.IsLogin = Boolean.TRUE;
                                    Initial.EndLogin = new Date();
                                    Initial.UserName = EU_Value;
                                    // 写入缓存
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String dateString = formatter.format(Initial.EndLogin);
                                    SharedPreferences sp = getSharedPreferences("UserInfo", Initial.MODE_PRIVATE);
                                    sp.edit().putString("UserName",Initial.UserName).putBoolean("IsLogin", Initial.IsLogin).putString("Date",dateString).commit();

                                    LoginSignin.this.finish();
                                    ToastUtil.toastLong(LoginSignin.this,"登录成功");
                                }
                                else{
                                    // 登录异常
                                    ToastUtil.toastLong(LoginSignin.this,GetInfoJson.getString("message"));
                                }

                            }
                            catch(JSONException e){
                                e.printStackTrace();
                            }

                        }
                    });

                }
            }
        });
        TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginSignin.this, Signin.class);
                startActivity(intent);

            }
        });

    }
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode== KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            return true;//不执行父类点击事件
        }
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }

}


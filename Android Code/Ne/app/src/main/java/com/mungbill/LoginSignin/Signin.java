package com.mungbill.LoginSignin;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mungbill.Bill.BillMain;
import com.mungbill.Init.Initial;
import com.mungbill.R;
import com.util.NoDoubleClickListener;
import com.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Signin extends AppCompatActivity {
    private TextView arrow; //返回箭头
    private EditText Edit_Username;     // 账号输入框
    private EditText Edit_Password;     // 密码输入框
    private EditText Edit_RPassword;   // 密码复填输入框
    private Button SubmitButton;       // 提交

    private String Post_Url = "http://10.0.2.2:8000/user/signin";        // Post Url
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获取组件
        setContentView(R.layout.activity_signin);
        Edit_Username = findViewById(R.id.editText1);
        Edit_Password = findViewById(R.id.editText2);
        Edit_RPassword = findViewById(R.id.editText3);
        arrow = findViewById(R.id.arrow);
        SubmitButton = findViewById(R.id.button);
        arrow = findViewById(R.id.arrow);

        //引入iconfont
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "iconfont.ttf");
        arrow.setTypeface(iconfont);
        arrow.setText(getResources().getString(R.string.jiantou));
        // 监听
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NoDoubleClickListener.isFastDoubleClick()) {
                    // 避免重复点击
                    return;
                }
                String EU_Value = Edit_Username.getText().toString();
                String EP_Value = Edit_Password.getText().toString();
                String ERP_Value = Edit_RPassword.getText().toString();
                if (EU_Value.length() == 0 ){
                    Edit_Username.requestFocus();
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    Edit_Username.startAnimation(shake);
                }
                else if(EU_Value.length()<6 ){
                    ToastUtil.toastShort(Signin.this,"用户名太短了");
                    Edit_Username.requestFocus();
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    Edit_Username.startAnimation(shake);
                }
                else if(EU_Value.length()>16){
                    ToastUtil.toastShort(Signin.this,"用户名太长了");
                    Edit_Username.requestFocus();
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    Edit_Username.startAnimation(shake);
                }
                else if (EP_Value.length() == 0){
                    Edit_Username.requestFocus();
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    Edit_Password.startAnimation(shake);
                }
                else if(EP_Value.length()<6){
                    ToastUtil.toastShort(Signin.this,"密码太短了");
                    Edit_Username.requestFocus();
                    Edit_Password.setText("");
                    Edit_RPassword.setText("");
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    Edit_Password.startAnimation(shake);
                }
                else if(EP_Value.length()>16){
                    ToastUtil.toastShort(Signin.this,"密码太长了");
                    Edit_Password.requestFocus();
                    Edit_Username.setText("");
                    Edit_RPassword.setText("");
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    Edit_Password.startAnimation(shake);
                }
                else if (ERP_Value.length() == 0){
                    Edit_Username.requestFocus();
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    Edit_RPassword.startAnimation(shake);
                }
                else if(EP_Value.equals(ERP_Value) != ERP_Value.equals(ERP_Value)){
                    ToastUtil.toastShort(Signin.this,"两次输入不符");
                    Edit_Password.requestFocus();
                    Edit_Password.setText("");
                    Edit_RPassword.setText("");
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    Edit_Password.startAnimation(shake);
                }
                else{

                    RequestBody requestBody = new FormBody.Builder()
                            .add("userid" , EU_Value)
                            .add("password" , EP_Value)
                            .add("nickname" , EU_Value)
                            .add("phonenum" , "")
                            .build();
                    Request request = new Request.Builder()
                            .url(Signin.this.getString(R.string.SignPost_Url))
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
                            ToastUtil.toastShort(Signin.this,"服务器错误");
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            // 服务器响应正常
                            try{
                                JSONObject GetInfoJson= new JSONObject(response.body().string());
                                if( GetInfoJson.get("error").toString().equals("0") ){

                                    Signin.this.finish();
                                    ToastUtil.toastLong(Signin.this,"注册成功");
                                }
                                else{
                                    // 异常
                                    ToastUtil.toastLong(Signin.this,GetInfoJson.getString("message"));
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


    }
}

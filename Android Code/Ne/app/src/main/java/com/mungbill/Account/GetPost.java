package com.mungbill.Account;

import com.google.gson.annotations.SerializedName;

public class GetPost {
    // 这是一个解析post请求的实体类
    @SerializedName("model")
    private String model;
    @SerializedName("pk")
    private long pk;
    @SerializedName("fields")
    private GetPostField fields;
}



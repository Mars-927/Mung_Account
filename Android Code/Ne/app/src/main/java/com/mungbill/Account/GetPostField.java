package com.mungbill.Account;

import com.google.gson.annotations.SerializedName;

public class GetPostField{
    @SerializedName("OnlyId")
    private String OnlyId;
    @SerializedName("BillNote")
    private String BillNote;
    @SerializedName("From")
    private String From;
    @SerializedName("BollMoney")
    private Double BollMoney;
    @SerializedName("Position")
    private String Position;
    @SerializedName("BillDate")
    private String BillDate;
    @SerializedName("BillCategory")
    private int BillCategory;
    @SerializedName("BillProperty")
    private String BillProperty;
    @SerializedName("IsDelect")
    private Boolean IsDelect;
    @SerializedName("LastChangeTime")
    private long LastChangeTime;
    @SerializedName("ThisUser")
    private long ThisUser;
}
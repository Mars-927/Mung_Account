
from django.contrib.auth import models
from django.shortcuts import render
from django.http import JsonResponse
from .models import Account as ACDB
from User.models import User as USDB
from django.core import serializers
from django.core.serializers.json import DjangoJSONEncoder
from django.core.exceptions import ObjectDoesNotExist
from django.core.serializers import serialize
import json
import time, datetime
# Create your views here.
def SyncToClient(request):
    # 数据同步至手机
    if request.method == 'GET':
        response = JsonResponse({'error': '-1','message':'Not accept Get'})
        return response
    UserId = request.POST['userid']
    if UserId == None:
        # 签名无效或者没有登录
        response = JsonResponse({'error': '-1','message':'Login Info Error'})
        return response
    else:
        BillDetail = ACDB.objects.filter(ThisUser__username = UserId).all()
        JsonDetail = json.loads(serializers.serialize("json", BillDetail))

        response = JsonResponse(
            {'error': '0',
            'message':'OK',
            'Detail':JsonDetail
            },
            safe=False
            )
        return response

def SynToServer(request):
    # 数据同步至服务器
    if request.method == 'GET':
        response = JsonResponse({'error': '-1','message':'Not accept Get'})
        return response
    UserId = request.POST['userid']
    if UserId == None:
        # 签名无效或者没有登录
        response = JsonResponse({'error': '-1','message':'Login Info Error'})
        return response
    else:
        GetDetail = json.loads(request.POST['Detail'])
        for ADetail in GetDetail:
            Item = DataRelation(ADetail)
            try:
                ServerDB = ACDB.objects.get(OnlyId = Item['OnlyId'])
            except ObjectDoesNotExist:
                # 没查询唯一标识符——需要添加
                try:
                    # 获取Id对应的类型
                    ItemUser = USDB.objects.get(username = UserId)
                except ObjectDoesNotExist:  
                    # 出错
                    response = JsonResponse({'error': '0','message':'数据库写入失败，由于没有该账户名'})
                    continue
                ItemUser.account_set.create(
                    BollMoney = Item['BillMoney'],
                    BillDate = Item['BillDate'],
                    BillCategory = Item['BillCategory'],
                    BillProperty = Item['BillProperty'],
                    LastChangeTime = Item['LastChangeTime'],
                    BillNote = Item['BillNote'],
                    IsDelect = Item['IsDelect'],
                    OnlyId = Item['OnlyId']
                )
                ItemUser.save()
                continue
            # 查询到唯一标识符——直接编辑
            if ServerDB.LastChangeTime < Item['LastChangeTime']:
                # 如果服务器数据陈旧 那么进行更新
                ServerDB.BillNote = Item['BillNote']
                ServerDB.BollMoney = Item['BollMoney']
                ServerDB.BillDate = Item['BillDate']
                ServerDB.BillCategory = Item['BillCategory']
                ServerDB.BillProperty = Item['BillProperty']
                ServerDB.IsDelect = Item['IsDelect']
                ServerDB.OnlyId = Item['OnlyId']
                ServerDB.save()
              
        response = JsonResponse({'error': '0','message':'Ok'})
        return response


def DataRelation(Item):
    # 数据对接使用 前端获取的数据转换成后端可以处理的实体类
    IncomeSelect = ["O","I"]        # 支出为0 对O 收入为1 对于I
    DelectSelect = [True,False]
    ProperSelect = {}
    ProperSelect["餐饮"] = 10
    ProperSelect["购物"] = 11
    ProperSelect["日用"] = 12
    ProperSelect["交通"] = 13
    ProperSelect["水果"] = 14
    ProperSelect["零食"] = 15
    ProperSelect["服饰"] = 16
    ProperSelect["美容"] = 17
    ProperSelect["旅行"] = 18
    ProperSelect["数码"] = 19
    ProperSelect["医疗"] = 20
    ProperSelect["学习"] = 21
    ProperSelect["工资"] = 80
    ProperSelect["兼职"] = 81
    ProperSelect["理财"] = 82
    ProperSelect["礼金"] = 83
    ProperSelect["其他"] = 84

    Result = {}
    Result["OnlyId"] = Item["OnlyId"]
    Result["BillMoney"] = Item["BillMoney"]
    Result["BillNote"] = Item["BillNote"]
    Result["BillDate"] = time.strftime('%Y-%m-%d',time.localtime(((int)(Item["BillDate"]))/1000))
    Result["LastChangeTime"] = (int)(Item["LastChangeTime"])
    Result["BillCategory"] =  ProperSelect[Item["BillProperty"]]    # 注意前端传值这两者是反的
    Result["IsDelect"] = DelectSelect[Item["IsDelect"]]
    Result["BillProperty"] =    IncomeSelect[Item["BillCategory"]]# 注意前端传值这两者是反的





    return Result
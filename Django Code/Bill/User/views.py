from django.http import JsonResponse
from .models import User as modelUser
from django.core.exceptions import ObjectDoesNotExist
from django.contrib import auth
from django.contrib.auth import authenticate
import base64
# Create your views here.
# 以下给出User的操作
def Login(request):
    # 登录
    if request.method == 'GET':
        response = JsonResponse({'error': '-1','message':'不接受GET请求'})
        return response

    Username = request.POST['userid']
    UserPassword = request.POST['password']
    # 判定cookie是否存在
    Info = request.get_signed_cookie('UserId',default = None, salt='TLDL_YYDS',max_age=1209600)      
    if Info == None:
        # 签名无效或者没有登录
        # 判定是否注册
        try:
            modelUser.objects.get(username = Username)                    
        except ObjectDoesNotExist:
            response = JsonResponse({'error': '-1','message':'用户不存在'})
            return response
        # 使用userid password验证用户
        Test_user = authenticate(username=Username, password=UserPassword)
        if Test_user is not None:
            response = JsonResponse({
                'error': '0',
                'message':'Ok'
            })
            response.set_signed_cookie("UserId", request.POST["userid"],salt = 'TLDL_YYDS', max_age=1209600)   # 14天验证
        else:
            response = JsonResponse({'error': '-1','message':'密码错误~'})
        return response
    response = JsonResponse({'error': '0','message':'该用户已经登录'})
    response.set_signed_cookie("UserId", request.POST["userid"],salt = 'TLDL_YYDS', max_age=1209600)   # 14天验证
    return response
    
def Logout(request):
    if request.method == 'GET':
        response = JsonResponse({'error': '-1','message':'Not accept Get'})
        return response
    auth.logout(request)
    response = JsonResponse({'error': '0','message':'OK'})
    response.delete_cookie('UserId')
    return response


def SignIn(request):
    # 注册
    if request.method == 'GET':
        response = JsonResponse({'error': '-1','message':'不接受GET请求'})
        return response
    ## 检验账号密码
    Username = request.POST['userid']
    try:
        Info = modelUser.objects.get(username = Username)
    except ObjectDoesNotExist:
        # 没有该用户 允许注册
        NickName=request.POST['nickname']
        PhoneNum=request.POST['phonenum']
        # 使用Django自带用户系统验证
        DjangoUser = modelUser.objects.create_user(request.POST['userid'], 'None@Django.com', request.POST['password'])
        DjangoUser.NickName = NickName
        DjangoUser.PhoneNum = PhoneNum
        DjangoUser.save()
        # 相关信息保存到数据库
        return Login(request)

    response = JsonResponse({'error': '-1','message':'用于已经存在'})
    return response


def ChangePassWord(request):
    if request.method == 'GET':
        response = JsonResponse({'error': '-1','message':'Not accept Get'})
        return response
    UserId = request.POST['userid']
    OldPassword =  request.POST['oldpassword']
    UserPassword = request.POST['newpassword']
    Test_user = authenticate(username=UserId, password=OldPassword)
    if Test_user is not None:
        u = modelUser.objects.get(username=UserId)
        u.set_password(UserPassword)
        u.save()
        response = JsonResponse({'error': '0','message':'OK'})
    else:
        response = JsonResponse({'error': '-1','message':'Old Password Error'})
    
    return response

def ChangeImg(request):
    if request.method == 'GET':
        response = JsonResponse({'error': '-1','message':'Not accept Get'})
        return response
    Info = request.POST['userid']
    if Info == None:
        response = JsonResponse({'error': '-1','message':'Login Info Error'})
        return response
    else:
        Username = Info
        DBInfo = modelUser.objects.get(username = Username)
        DBInfo.ImageUrl = request.FILES.get('image')
        DBInfo.save()
        response = JsonResponse({'error': '0','message':'OK'})
        return response
    

def ChangePhoneNum(request):
    if request.method == 'GET':
        response = JsonResponse({'error': '-1','message':'Not accept Get'})
        return response
    Info = request.POST['userid']
    if Info == None:
        response = JsonResponse({'error': '-1','message':'Login Info Error'})
        return response
    else:
        Username = Info
        DBInfo = modelUser.objects.get(username = Username)
        DBInfo.PhoneNum = request.POST['phonenum']
        DBInfo.save()
        response = JsonResponse({'error': '0','message':'OK'})
        return response

def ChangeNick(request):
    if request.method == 'GET':
        response = JsonResponse({'error': '-1','message':'Not accept Get'})
        return response
    Info = request.POST['userid']
    if Info == None:
        response = JsonResponse({'error': '-1','message':'Login Info Error'})
        return response
    else:
        Username = Info
        DBInfo = modelUser.objects.get(username = Username)
        DBInfo.NickName = request.POST['nickname']
        DBInfo.save()
        response = JsonResponse({'error': '0','message':'OK'})
        return response

def UpdateInfo(request):
    # 向客户端更新个人信息
    if request.method == 'GET':
        response = JsonResponse({'error': '-1','message':'Not accept Get'})
        return response
    Info = request.POST['userid']
    if Info == None:
        response = JsonResponse({'error': '-1','message':'Login Info Error'})
        return response
    else:
        Username = Info
        DBInfo = modelUser.objects.get(username = Username)
        ImagePath = './'+str(DBInfo.ImageUrl)
        with open(ImagePath,'rb') as f:
            base64data = base64.b64encode(f.read())
        response = JsonResponse({
            'UserId':DBInfo.username,
            'NickName':DBInfo.NickName,
            'SignUpDate':DBInfo.date_joined,
            'PhoneNum':DBInfo.PhoneNum,
            'image' :str(base64data),
            'error': '0',
            'message':'Ok'
        })
        
        return response

        
        
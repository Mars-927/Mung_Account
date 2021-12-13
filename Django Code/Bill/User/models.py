from django.db import models
import random
import time
from django.contrib.auth.models import AbstractUser
# Create your models here.

def user_directory_path(instance, filename):
    # file will be uploaded to MEDIA_ROOT/user_<id>/<filename>
    return 'UserImage/{0}/{1}'.format(time.strftime('%Y-%m-%d'),str(random.randint(0,99999999))+filename)

class User(AbstractUser):
    # 继承Auth user
    NickName = models.CharField(
        # 昵称
        # 不可为null,不可为空、设定字段名为'nickname'、最大长度为256
        null=False,
        blank=False,
        db_column = 'nickname',
        max_length=256
    )      

    ImageUrl = models.ImageField(
        # 头像地址
        # 不可为null、可以为空
        null=False,
        blank=False,
        db_column = 'imageurl',
        upload_to=user_directory_path


    )      

    PhoneNum = models.CharField(
        # 手机号
        # 不可为null、不可为空、独一无二
        null=False,
        blank=False,
        db_column = 'phonenum',
        max_length=256
    )    




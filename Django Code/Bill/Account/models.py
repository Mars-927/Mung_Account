from django.db import models
from User.models import User

# Create your models here.

class Account(models.Model):
    OnlyId = models.CharField(
        # 账单唯一性标识
        # 区分账单唯一性：时间戳 + 用户ID + （0~999）随机数 
        null= False,
        blank=True,
        max_length=256
    )
    BillNote = models.CharField(
        # 账单备注 可以为空 不可为Null 
        null= False,
        blank=True,
        max_length=256
    )

    From = models.CharField(
        # 账单付款方
        null= False,
        blank=False,
        max_length=256
    )

    BollMoney = models.FloatField(
        # 账单金额
        null= False,
        blank=False,
    )


    Position = models.CharField(
        # 账单产生的位置 
        null= False,
        blank=False,
        max_length=256
    )

    BillDate = models.DateField(
        # 账单产生的时间
        null= False,
        blank=False,
    )

    BillCategory = [
        (10,'餐饮'),(11,'购物'),(12,'日用'),(13,'交通'),
        (14,'水果'),(15,'零食'),(16,'服饰'),(17,'美容'),
        (18,'旅行'),(19,'数码'),(20,'医疗'),(21,'学习'),
        
        (80,'工资'),(81,'兼职'),(82,'理财'),(83,'礼金'),
        (84,'其他'),
    ]
    BillCategory = models.IntegerField(
        # 收支类别
        # 编码规则
        # [收入|支出][分类项]
        # [1~7|8~9][0~9]
        null= False,
        blank=False,
        choices = BillCategory
    )

    PropertyList = [
        ('I','Income'),
        ('O','Expend'),
    ]
    BillProperty = models.CharField(
        # 收入 & 支出
        null= False,
        blank=False,
        max_length=1, 
        choices=PropertyList
        )
    
    IsDelect = models.BooleanField(
        # 该项是否已经被删除
        null= False,
        blank=False,
        default= False
    )

    LastChangeTime = models.BigIntegerField(
        # 该项的最后修改时间
        # 这里保存以毫秒为单位的时间戳，使用Asia/Shanghai时区
        null= False,
        blank=False
    )

    ThisUser = models.ForeignKey(
        # 一对多 一个Bill 对 一个User
        # 删除模式为联级删除 删除一个user 删除所有对应的Bill
        User,
        null= False,
        blank=False,
        on_delete = models.CASCADE 
    )
    def __str__(self):
        return self.BillNote
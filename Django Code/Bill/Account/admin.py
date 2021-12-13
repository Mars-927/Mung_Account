from django.contrib import admin
from .models import Account
# Register your models here.


class Accountadmin(admin.ModelAdmin):
    list_display = ('BillNote','BollMoney','BillProperty','BillCategory','BillDate')
admin.site.register(Account,Accountadmin)
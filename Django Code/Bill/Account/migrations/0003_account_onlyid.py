# Generated by Django 3.2.5 on 2021-09-26 12:38

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('Account', '0002_account_thisuser'),
    ]

    operations = [
        migrations.AddField(
            model_name='account',
            name='OnlyId',
            field=models.CharField(blank=True, max_length=256),
        ),
    ]
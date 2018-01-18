#!/usr/bin/python
# -*- coding: UTF-8 -*-

from peewee import *
from basic.mysqlSever import MySQLModel

class User(MySQLModel):
    username = CharField(unique=True)
    password = CharField()
    mobile = CharField()
    invite_code = CharField(unique=True)
    enabled = BooleanField()
    last_password_reset_date = DateTimeField()
    
if __name__ == "__main__":
#     user = User.get(User.id == 1)
#     user.delete_instance()
    for user in User.select():
        print type(user.mobile)
        print user.mobile
    print "haha"
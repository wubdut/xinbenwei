#!/usr/bin/python
# -*- coding: UTF-8 -*-

from peewee import *
from basic.mysqlSever import MySQLModel

class User(MySQLModel):
    username = CharField(unique=True);
    password = CharField();
    subscription = BooleanField();
    enabled = BooleanField();
    inviteCode = CharField(unique=True);
    lastPasswordResetDate = DateTimeField();
    
if __name__ == "__main__":
#     User.create_table()
#     user = User()
#     user.mail = "9811@ksjf.com"
#     user.password = "123"
#     user.subscription = True
#     user.inviteCode = "sdfsfsf"
#     user.save()
#     user = User.get(User.id == 1)
#     print user.mail
#     for user in User.select():
#         print user.mail
    user = User.get(User.id == 1)
    user.delete_instance()
    print "haha"
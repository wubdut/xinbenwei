#!/usr/bin/env python -u
# -*- coding: utf-8 -*-

from peewee import *
from basic.mysqlSever import MySQLModel
import datetime
import time

class ShortSwing(MySQLModel):
    stock_id = CharField()
    stock_name = CharField()
    time_rec = DateTimeField()
    price_rec = FloatField()
    price_real = FloatField()
    increase = FloatField()
    stop_profit = FloatField()
    stop_loss = FloatField()
    status = CharField()
    score = FloatField()
    sale = IntegerField()
    
if __name__ == "__main__":
#     ShortSwing.create_table()


#     shortswing = ShortSwing.get(id = 1)
#     print shortswing.stock_name
    
    for it in ShortSwing.select():
        print it.stock_name
        print it.status == u"止盈"
        print it.sale

#     shortSwing = ShortSwing()
#     shortSwing.stock_id = '600452'
#     shortSwing.stock_name = '涪陵电力'
#     shortSwing.time_rec = datetime.datetime.now()
#     shortSwing.price_rec = 35.98
#     shortSwing.price_real = 35.22
#     shortSwing.increase = -0.01
#     shortSwing.stop_profit = 30.3
#     shortSwing.stop_loss = 40.15
#     shortSwing.status = '进行'
#     shortSwing.score = 0.1234
#     shortSwing.sale = False
#     
#     shortSwing.save()
    
    print "haha"
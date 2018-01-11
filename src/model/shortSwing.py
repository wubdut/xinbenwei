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
    sale = BooleanField()
        
if __name__ == "__main__":
#     ShortSwing.create_table()

#     shortSwing = ShortSwing()
#     shortSwing.stock_id = '600718'
#     shortSwing.stock_name = '东软集团'
#     shortSwing.time_rec = datetime.datetime.now()
#     shortSwing.price_rec = 14.56
#     shortSwing.price_real = 14.89
#     shortSwing.increase = 0.01
#     shortSwing.stop_profit = 15.00
#     shortSwing.stop_loss = 14.00
#     shortSwing.status = '进行'
#     shortSwing.score = 0.1234
#     shortSwing.sale = True
#       
#     shortSwing.save()
    
    print "haha"
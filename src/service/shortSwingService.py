#!/usr/bin/env python -u
# -*- coding: utf-8 -*-

import json
import tushare as ts
from model.shortSwing import ShortSwing
from model.recommend import Recommend
from model.sendMessage import SendMessage
from basic import timeDate

def getNewStock():
    while Recommend.getLlen() > 0:
        getOneStock()
        
def getOneStock():
    print "get stock"
    ShortSwing.db_connect()
    item_str = Recommend.lpop()
    if item_str is None:
        return
    item = json.loads(item_str)
    stockId = item['stockId']
    priceRec = item['priceRec']
    if priceRec < 0.01:
        return
    timeRec = timeDate.timestamp_datetime(item['timeRec'])
    score = item['score']
    try:
        df = ts.get_realtime_quotes(stockId)
    except Exception as e:
        if ShortSwing.db_status():
            ShortSwing.db_close()
        print "tushare connection error"
        return
    if df is None:
        return
    priceReal = float(df.at[0,'price'].encode('utf-8'))
    increase = (priceReal - priceRec)/priceRec
    
    shortSwing = ShortSwing()
    shortSwing.stock_id = stockId
    shortSwing.stock_name = df.at[0,'name']
    shortSwing.time_rec = timeRec
    shortSwing.price_rec = priceRec
    shortSwing.price_real = priceReal
    shortSwing.increase = round(increase, 4)
    shortSwing.stop_profit = round(priceRec*(1+0.02), 2)
    shortSwing.stop_loss = round(priceRec*(1-0.03), 2)
    shortSwing.status = u'进行'
    shortSwing.score = score
    shortSwing.sale = 0
    try:
        shortSwing.save()
    except:
        if ShortSwing.db_status():
            ShortSwing.db_close()
        print "mysql connection error when getOneStock"
        print shortSwing.stock_name 
        return
    text = u"代码：" + stockId + u"\n名称：" + shortSwing.stock_name
    SendMessage.lpush(text)
    ShortSwing.db_close()
        
def updatePrice():
#     print "update price"
    list = []
    ShortSwing.db_connect()
    try:
        list = ShortSwing.select()
    except:
        if ShortSwing.db_status():
            ShortSwing.db_close()
        print "mysql connection error when update price" 
        return
    for item in list:
        if item.status != u'进行':
            continue
        try:
            df = ts.get_realtime_quotes(item.stock_id)
        except Exception as e:
            if ShortSwing.db_status():
                ShortSwing.db_close()
            print "tushare connection error"
            return
        if df is None:
            continue
        item.price_real = float(df.at[0,'price'].encode('utf-8'))
        item.increase = round((item.price_real-item.price_rec)/item.price_rec, 4)
        
        if item.price_real < 0.01:
            continue
        
        if item.sale == 1:
            if item.price_real > item.stop_profit:
                item.status = u'止盈'
#                 item.price_real = 0
                item.increase = round((item.price_real-item.price_rec)/item.price_rec, 4)
            elif item.price_real < item.stop_loss:
                item.status = u'止损'
#                 item.price_real = 0
                item.increase = round((item.price_real-item.price_rec)/item.price_rec, 4)
        try:
            item.save()
        except:
            if ShortSwing.db_status():
                ShortSwing.db_close()
            print "mysql connection error when update price"
    ShortSwing.db_close()

def openMarket():
    if not timeDate.isOpenMarketTime():
        return
    print "open market"
    ShortSwing.db_connect()
    list = []
    try:
        list = ShortSwing.select()
    except:
        if ShortSwing.db_status():
            ShortSwing.db_close()
        print "mysql connection error when open market" 
        return
    for item in list:
        if item.sale == 1:
            continue
        item.sale = 1
        try:
            item.save()
        except:
            print "mysql connection error when open market"
    ShortSwing.db_close()

def closeMarket():
    if not timeDate.isCloseMarketTime():
        return
    print "close market"
    ShortSwing.db_connect()
    list = []
    try:
        list = ShortSwing.select()
    except:
        if ShortSwing.db_status():
            ShortSwing.db_close()
        print "mysql connection error when close market" 
        return
    for item in list:
        if item.status != u'进行':
            continue
        if item.sale == 1:
            if item.increase > 0.0015:
                item.status = u'止盈'
                try:
                    item.save()
                except:
                    if ShortSwing.db_status():
                        ShortSwing.db_close()
                    print "mysql connection error when close market"
    ShortSwing.db_close()

if __name__ == "__main__":
#     getNewStock()
#     getOneStock()
#     getNewStock()
#     updatePrice()
#     openMarket()
    print "haha"


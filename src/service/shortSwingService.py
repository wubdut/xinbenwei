#!/usr/bin/env python -u
# -*- coding: utf-8 -*-

import json
import tushare as ts
from model.shortSwing import ShortSwing
from model.recommend import Recommend
from model.user import User
from basic import timeDate,mail
from model import recommend

def getNewStock():
    while Recommend.getLlen() > 0:
        item_str = Recommend.lpop()
        item = json.loads(item_str)
        stockId = item['stockId']
        priceRec = item['priceRec']
        item['timeRec'] = timeDate.getDate(item['timeRec'])
        
        df = ts.get_realtime_quotes(stockId)
        priceReal = float(df.at[0,'price'].encode('utf-8'))
        
        item['stockName'] = df.at[0,'name']
        inc_f = 0
        if priceRec > 0.01:
            inc_f = (priceReal - priceRec)/priceRec
        item['increase'] = round(inc_f, 4)
        item['priceReal'] = priceReal
        item['stopProfit'] = round(priceRec*(1+0.01), 2)
        item['stopLoss'] = round(priceRec*(1-0.05), 2)
        item['status'] = u'进行'
        item['sale'] = False
        mail.sendToAll(User.getMailList(), item)
        ShortSwing.add(item)
        
def getOneStock():
    if Recommend.getLlen() > 0:
        
        item_str = Recommend.lpop()
        Recommend.lpush(item_str)
        item = json.loads(item_str)
        stockId = item['stockId']
        priceRec = item['priceRec']
        timeRec = timeDate.timestamp_datetime(item['timeRec'])
        score = item['score']
        try:
            df = ts.get_realtime_quotes(stockId)
        except Exception as e:
            print "tushare 网络连接错误"
            return
        
        if df is None:
            return
        
        priceReal = float(df.at[0,'price'].encode('utf-8'))
        
        
        inc_f = 0
        if priceRec > 0.01:
            inc_f = (priceReal - priceRec)/priceRec
        
        shortSwing = ShortSwing()
        print stockId
        shortSwing.stock_id = stockId
        shortSwing.stock_name = df.at[0,'name']
        shortSwing.time_rec = timeRec
        shortSwing.price_rec = priceRec
        shortSwing.price_real = priceReal
        shortSwing.increase = round(inc_f, 4)
        shortSwing.stop_profit = round(priceRec*(1+0.01), 2)
        shortSwing.stop_loss = round(priceRec*(1-0.05), 2)
        shortSwing.status = u'进行'
        shortSwing.score = score
        shortSwing.sale = False
        
        shortSwing.save()
    else:
        return


        
def updatePrice():
    list = ShortSwing.queryAll()
    for index in range(len(list)):
        it = list[index]
        item = json.loads(it)
        if item['status'] != u'进行':
            continue
        try:
            df = ts.get_realtime_quotes(item['stockId'])
        except Exception as e:
            print "tushare 网络连接错误"
            return
        
        if df is None:
            continue
        priceReal = float(df.at[0,'price'].encode('utf-8'))
#         priceHigh = float(df.at[0,'high'].encode('utf-8'))
        item['priceReal'] = priceReal
        setPriceReal(item)
        setStatus(item)
        ShortSwing.alert(index, item)
        
def closeMarket():
    if not timeDate.isCloseMarketTime():
        return
    list = ShortSwing.queryAll()
    for index in range(len(list)):
        it = list[index]
        item = json.loads(it)
        if item['status'] != u'进行':
            continue
#         df = ts.get_realtime_quotes(item['stockId'])
#         priceReal = float(df.at[0,'price'].encode('utf-8'))
#         item['priceReal'] = priceReal
        setCloseStatus(item)
        ShortSwing.alert(index, item)
        
def openMarket():
    if not timeDate.isOpenMarketTime():
        return
    list = ShortSwing.queryAll()
    for index in range(len(list)):
        it = list[index]
        item = json.loads(it)
        if item['sale']:
            continue
        setOpenStatus(item)
        ShortSwing.alert(index, item)
    
def setStatus(item):
    priceRec = item['priceRec']
    priceReal = item['priceReal']
    if not item['sale']:
        return
    if priceReal > item['stopProfit']:
        item['status'] = u'止盈'
        item['priceReal'] = "----"
        if priceRec > 0.01:
            item['increase'] = round((item['stopProfit']-priceRec)/priceRec, 4)
    elif priceReal < item['stopLoss']:
        item['status'] = u'止损'
        item['priceReal'] = "----"
        if priceRec > 0.01:
            item['increase'] = round((item['stopLoss']-priceRec)/priceRec, 4)
        
def setPriceReal(item):
    priceRec = item['priceRec']
    priceReal = item['priceReal']
    if priceRec > 0.01:
        item['increase'] = round((priceReal-priceRec)/priceRec, 4)
    
def setCloseStatus(item):
    if item['sale']:
        if item['increase'] > 0:
            item['status'] = u'止盈'

def setOpenStatus(item):
    item['sale'] = True
    






if __name__ == "__main__":
#     getNewStock()
    getOneStock()
    print "haha"
#     openMarket()
#     print User.getMailList()
    
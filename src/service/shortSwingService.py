#!/usr/bin/env python -u
# -*- coding: utf-8 -*-

import json
import tushare as ts
from model.shortSwing import ShortSwing
from model.recommend import Recommend
from basic import timeDate

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
        ShortSwing(item).add()
        

def updatePrice():
    llen = ShortSwing.getLlen()
    list = ShortSwing.queryAll()
    for index in range(llen):
        item_str = list[index]
        item = json.loads(item_str)
        if item['status'] != u'进行':
            continue
        df = ts.get_realtime_quotes(item['stockId'])
        priceReal = float(df.at[0,'price'].encode('utf-8'))
#         priceHigh = float(df.at[0,'high'].encode('utf-8'))
        item['priceReal'] = priceReal
        setPriceReal(item)
        setStatus(item)
        ShortSwing(item).alert(index)
    
def closeMarket():
    if not timeDate.isCloseMarketTime():
        continue
    llen = ShortSwing.getLlen()
    list = ShortSwing.queryAll()
    for index in range(llen):
        item_str = list[index]
        item = json.loads(item_str)
        if item['status'] != u'进行':
            continue
        df = ts.get_realtime_quotes(item['stockId'])
        priceReal = float(df.at[0,'price'].encode('utf-8'))
        item['priceReal'] = priceReal
        setClostStatus(item)
        ShortSwing(item).alert(index)
    
def setStatus(item):
    priceRec = item['priceRec']
    priceReal = item['priceReal']
    if priceReal > item['stopProfit']:
        item['status'] = u'止盈'
        item['priceReal'] = "----"
        item['increase'] = round((item['stopProfit']-priceRec)/priceRec, 4)
    elif priceReal < item['stopLoss']:
        item['status'] = u'止损'
        item['priceReal'] = "----"
        item['increase'] = round((item['stopLoss']-priceRec)/priceRec, 4)
         
def setPriceReal(item):
    priceRec = item['priceRec']
    priceReal = item['priceReal']
    item['increase'] = round((priceReal-priceRec)/priceRec, 4)
    
def setClostStatus(item):
    if item['sale']:
        if item['priceReal'] > 0:
            item['status'] = u'止盈'
    else:
        item['sale'] = True
    
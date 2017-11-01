'''
Created on 2017年10月31日

@author: wubin
'''
#!/usr/bin/env python
# -*- coding: utf-8 -*-

import tushare as ts
import json
from basic import timeDate

def setStatus(item, priceReal):
    if item['status'] != u'进行':
        return
    priceRec = item['price']
    if priceReal > item['stopProfit']:
        item['status'] = u'止盈'
        item['priceReal'] = "----"
        item['increase'] = round((item['stopProfit']-priceRec)/priceRec, 4)
    elif priceReal < item['stopLoss']:
        item['status'] = u'止损'
        item['priceReal'] = "----"
        item['increase'] = round((item['stopLoss']-priceRec)/priceRec, 4)
        
def setPriceReal(item, priceReal):
    if item['status'] != u'进行':
        return
    item['priceReal'] = priceReal
    priceRec = item['priceRec']
    item['increase'] = round((priceReal-priceRec)/priceRec, 4)
    
def add2ShortSwing(server):
    server.rpush('shortSwing_list', server.lpop('shortSwing_today'))

def add2ShortSwingToday(server):
    item = server.lpop('recommend_list')
    itemJson = json.loads(item)
    stockRec = itemJson['stockid']
    priceRec = itemJson['price']
    timeRec = itemJson['time']
    
    df = ts.get_realtime_quotes(stockRec)
    priceReal = float(df.at[0,'price'].encode('utf-8'))
    
    shortSwingJson = {}
    shortSwingJson['stockId'] = stockRec
    shortSwingJson['stockName'] = df.at[0,'name']
    shortSwingJson['recommendTime'] = timeDate.getDate(itemJson['time'])
    shortSwingJson['priceRec'] = priceRec
    inc_f = 0
    if priceRec > 0.01:
        inc_f = (priceReal - priceRec)/priceRec
    shortSwingJson['increase'] = round(inc_f, 4)
    shortSwingJson['priceReal'] = priceReal
    shortSwingJson['stopProfit'] = round(priceRec*(1+0.01), 2)
    shortSwingJson['stopLoss'] = round(priceRec*(1-0.05), 2)
    shortSwingJson['status'] = u'进行'
    
    return server.rpush('shortSwing_today', json.dumps(shortSwingJson))

def query(server):
    llen = server.llen('shortSwing_list')
    list = server.lrange('shortSwing_list', 0, llen-1)
    return list

def updateShortSwing(server):
    llen = server.llen('shortSwing_list')
    list = server.lrange('shortSwing_list', 0, llen-1)
    for it in list:
        itJson = json.loads(it)
        df = ts.get_realtime_quotes(itJson['stockId'])
        priceReal = float(df.at[0,'price'].encode('utf-8'))
        priceHigh = float(df.at[0,'high'].encode('utf-8'))
        setStatus(itJson, priceReal)
        setPriceReal(itJson, priceReal)
        server.lset('shortSwing_list', json.dump(itJson))
        
# def closeMarket(server):
    
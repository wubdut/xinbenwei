#!/usr/bin/env python
# -*- coding: utf-8 -*-

import tushare as ts
import json
import timeDate

def setStatus(item, priceReal):
    if item['status'] != u'进行':
        return
    if priceReal > item['stopProfit']:
        item['status'] = u'止盈'
        itJson['priceReal'] = "----"
    elif priceReal < item['stopLoss']:
        item['status'] = u'止损'
        itJson['priceReal'] = "----"
        
def setPriceReal(item, priceReal):
    if item['status'] != u'进行':
        return
    item['priceReal'] = priceReal
    priceRec = item['priceRec']
    item['increase'] = round((priceReal-priceRec)/priceRec, 4)
    
def add2shortSwing(server, item):
    server.rpush('shortSwing_list', item)

def add(server, item):
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
    # shortSwingJson['stopLTime'] = '--'
    
    
    return server.rpush('shortSwing_today', json.dumps(shortSwingJson))

# def delete(self, server):
    # server.lpop('shortSwing_list')

def query(server):
    llen = server.llen('shortSwing_list')
    list = server.lrange('shortSwing_list', 0, llen-1)
    return list

def update(server):
    llen = server.llen('shortSwing_list')
    list = server.lrange('shortSwing_list', 0, llen-1)
    listJson = []
    for it in list:
        itJson = json.loads(it)
        df = ts.get_realtime_quotes(itJson['stockId'])
        priceReal = float(df.at[0,'price'].encode('utf-8'))
        setStatus(itJson, priceReal)
        setPriceReal(itJson, priceReal)
        # if itJson['status'] == u"止盈":
            # itJson['increase'] = 0.01
        # if itJson['increase'] > 0:
            # itJson['status'] = u"止盈"
        # if itJson['status'] == u"止盈":
            # itJson['priceReal'] = "----"
        listJson.append(itJson)
    for num in range(0, llen):
        pipe = server.pipeline()
        pipe.multi()
        pipe.lpop('shortSwing_list')
        pipe.rpush('shortSwing_list', json.dumps(listJson[num]))
        pipe.execute()
    # list =[]
    # while (server.llen('shortSwing_list')):
        # list.append(json.loads(server.lpop('shortSwing_list')))
    # for it in list:
        # df = ts.get_realtime_quotes(it['stockId'])
        # priceReal = float(df.at[0,'price'].encode('utf-8'))
        # setStatus(it, priceReal)
        # setPriceReal(it, priceReal)
        # server.rpush('shortSwing_list', json.dumps(it))
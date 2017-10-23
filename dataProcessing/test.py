#!/usr/bin/env python
# -*- coding: utf-8 -*-

import time
import redisServer
import json
import tushare as ts
import timeDate
import getRecommend
import shortSwing

# timestamp = 1462451334
# #转换成localtime
# time_local = time.localtime(timestamp)
# #转换成新的时间格式(2016-05-05 20:28:54)
# dt = time.strftime("%Y-%m-%d %H:%M:%S",time_local)
# print dt

server = redisServer.getServer('39.108.214.220')

# 删除
# print server.delete('shortSwing_list')

# 恢复备份 和 更新
# llen = server.llen('recommend_dump')
# for it in server.lrange('recommend_dump', 0, llen-1):
    # server.rpush('recommend_list', it)
    # print it
# getRecommend.getFromRecommendList(server)

# server.delete('recommend_today')
    
# for it in server.keys():
    # print it
    
# llen = server.llen('shortSwing_today')
# for it in server.lrange('shortSwing_today', 0, llen-1):
    # print it
    
# server.delete('shortSwing_today')
# llen = server.llen('shortSwing_today_dump')
# for it in server.lrange('shortSwing_today_dump', 0, llen-1):
    # server.rpush('shortSwing_today', it)
    # print it
    # # server.rpush('shortSwing_dump', it)
    # it_json = json.loads(it)
    # print it_json['status'] != u'进行'
    
# llen =server.llen('shortSwing_list')
# list = []
# for it in server.lrange('shortSwing_list', 0, llen-1):
    # it_json = json.loads(it)
    # print it_json['stockName']
    # print it
    # if it['increase'] > 0:
        # it['status'] == u"止盈"
    # print it['status'] == u"止盈"
    
# for num in range(0, 5):
    # print server.rpop('shortSwing_today')
    
# server.delete('shortSwing_dump')
# list = server.keys()
# for it in list:
    # print it
# shortSwing.update(server)
getRecommend.getShortSwingToday(server)
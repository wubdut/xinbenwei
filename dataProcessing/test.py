#!/usr/bin/env python
# -*- coding: utf-8 -*-

import time
import redisServer
import json
import tushare as ts
import timeDate
import getRecommend

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
    
# llen = server.llen('recommend_list')
# for it in server.lrange('recommend_list', 0, llen-1):
    # print it
    

# llen = server.llen('shortSwing_list')
# for it in server.lrange('shortSwing_list', 0, llen-1):
    # # server.rpush('shortSwing_dump', it)
    # it_json = json.loads(it)
    # print it_json['status'] != u'进行'
    
llen =server.llen('shortSwing_list')
for it in server.lrange('shortSwing_list', 0, llen-1):
    print server.rpush('shortSwing_list_dump', it)
    
# for num in range(0, 5):
    # print server.rpop('shortSwing_today')
    
# server.delete('shortSwing_dump')
# list = server.keys()
# for it in list:
    # print it
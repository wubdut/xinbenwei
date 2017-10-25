#!/usr/bin/env python
# -*- coding: utf-8 -*-

import time
import redisServer
import json
import tushare as ts
import timeDate
import getRecommend
import shortSwing
import dump
import loadDump
import statistic

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

# loadDump.recommend_list(server)
# loadDump.shortSwing_list(server)

# dump.recommend_list(server)
# dump.shortSwing_list(server)

# getRecommend.getFromRecommendList(server)
# getRecommend.getShortSwingToday(server)

# shortSwing.update(server)

# server.delete('shortSwing_list')
# llen = server.llen('shortSwing_list_dump')
# for it in server.lrange('shortSwing_list_dump', 0, llen-1):
    # print server.rpush('shortSwing_list', it)
    
print statistic.increaseSum(server)

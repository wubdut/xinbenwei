#!/usr/bin/env python
# -*- coding: utf-8 -*-

import time
import redisServer
import shortSwing

while True:
    server = redisServer.getServer('39.108.214.220')
    shortSwing.update(server)
    print 'update stock price'
    getRecommend.getFromRecommendList(server)
    print 'check new recommendation'
    time.sleep(60)
    
# print timeDate.getDate(time.time())
# print timeDate.getDate(time.time()*1000)
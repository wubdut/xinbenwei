#!/usr/bin/env python -u
# -*- coding: utf-8 -*-

import json
import time
from basic.redisServer import server

class Recommend(object):
    
    @staticmethod
    def lpop():
        return server.lpop('Recommend')
    
    @staticmethod
    def lpush(item):
        return server.lpush('Recommend', item)
    
    @staticmethod
    def queryAll():
        llen = server.llen('Recommend')
        return server.lrange('Recommend', 0, llen-1)
    
    @staticmethod
    def getLlen():
        return server.llen('Recommend')
    
    @staticmethod
    def queryAllOut():
        for item in Recommend.queryAll():
            print item
        
if __name__ == "__main__":
#     list = Recommend.queryAll()
#     for index in range(len(list)):
#         it = json.loads(list[index])
#         print it['priceRec']
    item = {}
    item['stockId'] = u'600718'
    item['priceRec'] = 14.08
    item['timeRec'] = time.time()
    item['score'] = -0.8902938402
    item_str = json.dumps(item)
    Recommend.lpush(item_str)
    print "haha"
#!/usr/bin/env python -u
# -*- coding: utf-8 -*-

import json
from basic.redisServer import server

class Recommend(object):
    
    @staticmethod
    def lpop():
        item = json.loads(server.lpop('Recommend'))
        return item
    
    @staticmethod
    def queryAll():
        llen = server.llen('Recommend')
        return server.lrange('Recommend', 0, llen-1)
    
    @staticmethod
    def getLlen(self):
        return server.llen('Recommend')
    
    @staticmethod
    def queryAllOut():
        for item in Recommend.queryAll():
            print item
        
if __name__ == "__main__":
    Recommend.queryAllOut()

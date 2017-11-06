#!/usr/bin/env python -u
# -*- coding: utf-8 -*-

import json
from basic.redisServer import server

class ShortSwing(object):
    @staticmethod
    def add(item):
        server.rpush('ShortSwing', json.dumps(item))
        
    @staticmethod 
    def alert(index, item):
        server.lset('ShortSwing', index, json.dumps(item))
    
    @staticmethod
    def queryAll():
        llen = server.llen('ShortSwing')
        return server.lrange('ShortSwing', 0, llen-1)
    
    @staticmethod
    def getLlen():
        return server.llen('ShortSwing')
    
    @staticmethod
    def queryAllOut():
        for item in ShortSwing.queryAll():
            print item
        
if __name__ == "__main__":
    ShortSwing.queryAllOut()
    
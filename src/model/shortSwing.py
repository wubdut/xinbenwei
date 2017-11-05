#!/usr/bin/env python -u
# -*- coding: utf-8 -*-

import json
from basic.redisServer import server

class ShortSwing(object):
    
    @staticmethod
    def add(self):
        server.rpush('ShortSwing', json.dumps(self.item))
        
    def alert(self, index):
        server.lset(index, self.item)
    
    @staticmethod
    def queryAll():
        llen = server.llen('ShortSwing')
        print llen
        return server.lrange('ShortSwing', 0, llen-1)
    
    @staticmethod
    def getLlen():
        return server.llen('ShortSwing')
    
    @staticmethod
    def queryAllOut():
        for item in ShortSwing.queryAll():
            print item
    
    def __init__(self, item = {}):
        self.item = item
        
if __name__ == "__main__":
    ShortSwing.queryAllOut()
    
#!/usr/bin/env python -u
# -*- coding: utf-8 -*-

from server.redisServer import server
import json

class ShortSwing(object):
    
    @staticmethod
    def add(self):
        server.rpush('ShortSwing', json.dumps(self.item))
        
    def alert(self, index):
        server.lset(index, self.item)
    
    @staticmethod
    def queryAll():
        llen = server.llen('ShortSwing')
        return server.lrange('ShortSwing', 0, llen-1)
    
    @staticmethod
    def getLlen():
        return server.llen('ShortSwing')
    
    def __init__(self, item = {}):
        self.item = item
        
        
    
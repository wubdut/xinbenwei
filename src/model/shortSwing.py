#!/usr/bin/env python -u
# -*- coding: utf-8 -*-

import json
from basic.redisServer import server
from basic import mail

class ShortSwing(object):
    @staticmethod
    def add(item):
        server.rpush('ShortSwing', json.dumps(item))
        
    @staticmethod
    def getById(id):
        it = server.lrange('ShortSwing', id, id)
        return it[0]
        
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
#     user = ['wub-neu@neusoft.com']            # 收件人邮箱账号，我这边发送给自己
    userList = ['981128587@qq.com', '1397677607@qq.com']
    list = ShortSwing.queryAll()
    for index in range(2):
        item = json.loads(list[index])
#         print item['stockId'] + "  " + item['stockName']
        print mail.sendToAll(userList, item)
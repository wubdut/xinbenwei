#!/usr/bin/env python
# coding : utf-8
import redis
import json

class Recommend(object):

    def __init__(self, hostname):
    
        server = redis.Redis(host = hostname, port = 6379, password = 'myRedis')
        recommend_len = server.llen('recommend_list')
        recommend_list = server.lrange('recommend_list', 0, recommend_len-1)
        recommend_json = []
        recommend_set = set()
        for it in recommend_list:
            tmp = json.loads(it)
            recommend_json.append(tmp)
            recommend_set.add(tmp[u'stockid'])
        self.data = recommend_json
        self.set = recommend_set
        
# '39.108.214.220'

class ShortSwing(object):

    def __init__(self, hostname):
    
        server = redis.Redis(host = hostname, port = 6379, password = 'myRedis')
        










# main

redis = Recommend('39.108.214.220')

for it in redis.set:
    print it
#!/usr/bin/env python -u
# -*- coding: utf-8 -*-

from basic.redisServer import server


list = server.keys()

for it in list:
    print it
#      
len = server.llen('Recommend_dump')
# print len
for it in server.lrange('Recommend_dump', 0, len-1):
    server.lpush('Recommend', it)
    print it

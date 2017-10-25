#!/usr/bin/env python
# -*- coding: utf-8 -*-

def recommend_list(server):
    server.delete('recommend_list')
    llen = server.llen('recommend_list_dump')
    for it in server.lrange('recommend_list_dump', 0, llen-1):
        print server.rpush('recommend_list', it)

def shortSwing_list(server):
    server.delete('shortSwing_list')
    llen = server.llen('shortSwing_list_dump')
    for it in server.lrange('shortSwing_list_dump', 0, llen-1):
        print server.rpush('shortSwing_list', it)
        
def shortSwing_today(server):
    server.delete('shortSwing_today')
    llen = server.llen('shortSwing_today_dump')
    for it in server.lrange('shortSwing_today_dump', 0, llen-1):
        print server.rpush('shortSwing_today', it)
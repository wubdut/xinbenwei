#!/usr/bin/env python
# -*- coding: utf-8 -*-

def recommend_list(server):
    server.delete('recommend_list_dump')
    llen = server.llen('recommend_list')
    for it in server.lrange('recommend_list', 0, llen-1):
        print server.rpush('recommend_list_dump', it)
    print server.llen('recommend_list_dump')
        
def shortSwing_list(server):
    server.delete('shortSwing_list_dump')
    llen = server.llen('shortSwing_list')
    for it in server.lrange('shortSwing_list', 0, llen-1):
        print server.rpush('shortSwing_list_dump', it)
        
def shortSwing_today(server):
    server.delete('shortSwing_today_dump')
    llen = server.llen('shortSwing_today')
    for it in server.lrange('shortSwing_today', 0, llen-1):
        print server.rpush('shortSwing_today_dump', it)
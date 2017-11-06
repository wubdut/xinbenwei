#!/usr/bin/env python -u
# -*- coding: utf-8 -*-

import time

def getDate(timestamp):
    if timestamp > 1.5E+12:
        return time.strftime("%Y-%m-%d %H:%M:%S",time.localtime(timestamp/1000.0))
    else:
        return time.strftime("%Y-%m-%d %H:%M:%S",time.localtime(timestamp))
    
def isCloseMarketTime():
    today = time.localtime()
    t = (today.tm_year, today.tm_mon, today.tm_mday, 14, 55, 0, -1, -1, -1)
    start_secs = time.mktime(t)
    end_secs = start_secs + 4*60+30
    t_now = time.time()
    return t_now > start_secs and t_now < end_secs

def isOpenMarketTime():
    today = time.localtime()
    t = (today.tm_year, today.tm_mon, today.tm_mday, 8, 25, 0, -1, -1, -1)
    start_secs = time.mktime(t)
    end_secs = start_secs + 4*60+30
    t_now = time.time()
    return t_now > start_secs and t_now < end_secs
    
if __name__ == "__main__":
    print isCloseMarketTime()
    
    
    
    
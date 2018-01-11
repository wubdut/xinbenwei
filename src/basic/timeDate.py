#!/usr/bin/env python -u
# -*- coding: utf-8 -*-

from datetime import datetime
import time

def timestamp_datetime(ts):
    if isinstance(ts, (int, long, float, str)):
        try:
            ts = long(ts)
        except ValueError:
            raise
        
        if len(str(ts)) == 13:
            ts = int(ts / 1000)
        if len(str(ts)) != 10:
            raise ValueError
    else:
        raise ValueError()
    return datetime.fromtimestamp(ts)


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
    t_local = time.localtime(t_now)
    return t_now > start_secs and t_now < end_secs and t_local.tm_wday < 5

def isOpenMarketTime():
    today = time.localtime()
    t = (today.tm_year, today.tm_mon, today.tm_mday, 9, 30, 5, -1, -1, -1)
    start_secs = time.mktime(t)
    end_secs = start_secs + 4*60+30
    t_now = time.time()
    t_local = time.localtime(t_now)
    return t_now > start_secs and t_now < end_secs and t_local.tm_wday < 5




if __name__ == "__main__":
    try:
#         print(datetime_timestamp('2015-01-01 20:20:00', 's'))
        print(timestamp_datetime(1420114800))
        print(timestamp_datetime(1420114800123))
    except Exception as e:
        print(e.args[0])
#     print isCloseMarketTime()

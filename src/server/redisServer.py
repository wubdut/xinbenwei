'''
Created on 2017年10月31日

@author: wubin
'''
#!/usr/bin/env python
# -*- coding: utf-8 -*-

import redis
import time

def getServer(hostname):
    server = None
    while True:
        try:
            server = redis.Redis(host = hostname, port = 6379, password = 'myRedis')
            break
        except:
            print "cannot reach redis server"
            time.sleep(10)
    return server
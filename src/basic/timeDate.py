'''
Created on 2017年10月31日

@author: wubin
'''
#!/usr/bin/env python
# -*- coding: utf-8 -*-

import time

def getDate(timestamp):
    if timestamp > 1.5E+12:
        return time.strftime("%Y-%m-%d %H:%M:%S",time.localtime(timestamp/1000.0))
    else:
        return time.strftime("%Y-%m-%d %H:%M:%S",time.localtime(timestamp))
'''
Created on 2017年10月31日

@author: wubin
'''
#!/usr/bin/env python
# -*- coding: utf-8 -*-

import shortSwing

def add2ShortSwingToday(server):
    while server.llen('recommend_list') > 0:
        shortSwing.add2ShortSwingToday(server)
        
def add2ShortSwing(server):
    while server.llen('shortSwing_today') > 0:
        shortSwing.add2ShortSwing(server)
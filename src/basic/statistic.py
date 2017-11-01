'''
Created on 2017年10月31日

@author: wubin
'''
#!/usr/bin/env python
# -*- coding: utf-8 -*-

import json

def increaseSum(server):
    sum = 0
    llen = server.llen('shortSwing_list')
    for it in server.lrange('shortSwing_list', 0, llen-1):
        itJson = json.loads(it)
        sum = sum + itJson['increase']
    return sum
#!/usr/bin/env python
# -*- coding: utf-8 -*-

import recommend
import shortSwing

def getFromRecommendList(server):
    while server.llen('recommend_list') > 0:
        shortSwing.add(server, recommend.getOneRec(server))

def getShortSwingToday(server):
    while server.llen('shortSwing_today') > 0:
        shortSwing.add2shortSwing(server, recommend.getShortSwing(server))
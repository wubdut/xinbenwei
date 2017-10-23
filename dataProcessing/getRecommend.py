#!/usr/bin/env python
# -*- coding: utf-8 -*-

import recommend
import shortSwing

def getFromRecommendList(server):
    while server.llen('recommend_list') > 0:
        shortSwing.add(server, recommend.getOneRec(server))
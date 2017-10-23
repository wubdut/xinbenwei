#!/usr/bin/env python
# -*- coding: utf-8 -*-

import redis
import redisServer

def getOneRec(server):
    return server.lpop('recommend_list')
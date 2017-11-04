#!/usr/bin/env python -u
# -*- coding: utf-8 -*-

import threading
import time
from service.shortSwingService import updatePrice, closeMarket
from redis import lock

class UpdatePrice(threading.Thread):
    def run(self):
        global lock
        while (True):
            updatePrice()
    
    def __init__(self):
        threading.Thread.__init__(self)

class ClossMarketServer(threading.Thread):
    def run(self):
        global lock
        while (True):
            if lock.acquire():
                closeMarket()
            time.sleep(5)
    
    def __init__(self):
        threading.Thread.__init__(self)
    
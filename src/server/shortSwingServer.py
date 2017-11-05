#!/usr/bin/env python -u
# -*- coding: utf-8 -*-

import threading
import time
from service.shortSwingService import updatePrice, closeMarket, getNewStock

class GetNewStock(threading.Thread):
    def run(self):
        global lock
        while True:
            getNewStock()
            time.sleep(5)
        
class UpdatePrice(threading.Thread):
    def run(self):
        global lock
        while True:
            updatePrice()
    
    def __init__(self):
        threading.Thread.__init__(self)

class CloseMarket(threading.Thread):
    def run(self):
        global lock
        while True:
            if lock.acquire():
                closeMarket()
            time.sleep(5)
    
    def __init__(self):
        threading.Thread.__init__(self)
    
if __name__ == "__main__":
    lock = threading.Lock()
    
    getNewStock = GetNewStock()
    updatePrice = UpdatePrice()
    closeMarket = CloseMarket()
    
    getNewStock.start()
    updatePrice.start()
    closeMarket.start()
    
    
    
    
#!/usr/bin/env python -u
# -*- coding: utf-8 -*-

import threading
import time
from service import shortSwingService

class GetNewStock(threading.Thread):
    def run(self):
        global lock
        while True:
            shortSwingService.getNewStock()
            time.sleep(5)
        
class UpdatePrice(threading.Thread):
    def run(self):
        global lock
        while True:
            if lock.acquire():
                shortSwingService.updatePrice()
                lock.release()
            time.sleep(30)
    
    def __init__(self):
        threading.Thread.__init__(self)

class CloseMarket(threading.Thread):
    def run(self):
        global lock
        while True:
            if lock.acquire():
                shortSwingService.closeMarket()
                lock.release()
            time.sleep(50)
    
    def __init__(self):
        threading.Thread.__init__(self)
        
class OpenMarket(threading.Thread):
    def run(self):
        global lock
        while True:
            if lock.acquire():
                shortSwingService.openMarket()
                lock.release()
            time.sleep(50)
    
    def __init__(self):
        threading.Thread.__init__(self)
    
if __name__ == "__main__":
    lock = threading.Lock()
    
    getNewStock = GetNewStock()
    updatePrice = UpdatePrice()
    closeMarket = CloseMarket()
    openMarket = OpenMarket()
    
    getNewStock.start()
    updatePrice.start()
    closeMarket.start()
    openMarket.start()
    
    
    
    
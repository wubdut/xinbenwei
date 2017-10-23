#!/usr/bin/env python
# coding : utf-8

from BaseHTTPServer import BaseHTTPRequestHandler
from BaseHTTPServer import HTTPServer
import cgi
import redis
import json
import tushare as ts
import time

class Recommend(object):
	
    def __init__(self, hostname):
        self.json = []
        server = redis.Redis(host = hostname, port = 6379, password = 'myRedis')
        recommend_len = server.llen('recommend_list')
        recommend_list = server.lrange('recommend_list', 0, recommend_len-1)
        for it in recommend_list:
            tmp = json.loads(it)
            self.json.append(tmp)
            
            stockId = tmp[u'stockid']
            df = ts.get_realtime_quotes(stockId)
            
            tmp[u'real_price'] = float(df.at[0,'price'].encode('utf-8'))
            tmp[u'stock_name'] = df.at[0,'name']
            # self.dict[stockId] = tmp
        
# '39.108.214.220'


class TodoHandler(BaseHTTPRequestHandler):
    """A simple TODO server

    which can display and manage todos for you.
    """

    # Global instance to store todos. You should use a database in reality.

    def do_GET(self):
        # return all todos
        if self.path != '/':
            self.send_error(404, "File not found.")
            return

        # Just dump data to json, and return it
        # message = json.dumps(self.TODOS)
        # redis = Recommend('39.108.214.220')
        redis = Recommend('localhost')
        
        message = []
        
        for it in redis.json:
            tmp_dict = {}
            tmp_dict['stockId'] = it[u'stockid']
            tmp_dict['stockName'] = it[u'stock_name']
            
            tmp_dict['recommandTime'] = time.strftime("%Y-%m-%d %H:%M:%S",time.localtime(it[u'time']/1000))
            
            price_1 = it[u'price']
            price_2 = it[u'real_price']
            
            inc_float = 0.0;
            if price_1 > 0.1:
                inc_float = (price_2-price_1)/price_1
            
            tmp_dict['increase'] = round(inc_float, 3)
            tmp_dict['stopProfit'] = 0.01
            tmp_dict['stopLoss'] = -0.05
            message.append(tmp_dict)
        
        self.send_response(200)
        self.send_header('Content-type', 'application/json')
        self.send_header('Access-Control-Allow-Origin', '*')
        self.end_headers()
        self.wfile.write(json.dumps(message))





# main
server = HTTPServer(('', 8888), TodoHandler)
server.serve_forever()
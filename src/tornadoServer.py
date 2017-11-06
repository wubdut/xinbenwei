#!/usr/bin/env python -u
# -*- coding: utf-8 -*-

import tornado.httpserver
import tornado.ioloop
import tornado.options
import tornado.web
import json
from model.shortSwing import ShortSwing

from tornado.options import define, options
define("port", default=8000, help="run on the given port", type=int)

class IndexHandler(tornado.web.RequestHandler):
    #解决js跨域请求问题
    def set_default_headers(self):
        self.set_header('Access-Control-Allow-Origin', '*')
        self.set_header('Access-Control-Allow-Methods', 'POST, GET, OPTIONS')
        self.set_header('Access-Control-Max-Age', 1000)
        self.set_header('Access-Control-Allow-Headers', '*')
        self.set_header('Content-type', 'application/json')
        
    def get(self):
        message = []
        for it in ShortSwing.queryAll():
            message.append(json.loads(it))
        self.write(json.dumps(message))

if __name__ == "__main__":
    tornado.options.parse_command_line()
    app = tornado.web.Application(handlers=[(r"/", IndexHandler)])
    http_server = tornado.httpserver.HTTPServer(app)
    http_server.listen(options.port)
    tornado.ioloop.IOLoop.instance().start()
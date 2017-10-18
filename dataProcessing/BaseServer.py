#!/usr/bin/env python

from BaseHTTPServer import BaseHTTPRequestHandler
import cgi
import json


class TodoHandler(BaseHTTPRequestHandler):
    """A simple TODO server

    which can display and manage todos for you.
    """

    # Global instance to store todos. You should use a database in reality.
    TODOS = []

    def do_GET(self):
        # return all todos

        if self.path != '/':
            self.send_error(404, "File not found.")
            return

        # Just dump data to json, and return it
        message = json.dumps(self.TODOS)

        self.send_response(200)
        self.send_header('Content-type', 'application/json')
        self.send_header('Access-Control-Allow-Origin', '*')
        self.end_headers()
        self.wfile.write(message)

    def do_POST(self):
        """Add a new todo

        Only json data is supported, otherwise send a 415 response back.
        Append new todo to class variable, and it will be displayed
        in following get request
        """
        ctype, pdict = cgi.parse_header(self.headers['content-type'])
        if ctype == 'application/json':
            length = int(self.headers['content-length'])
            post_values = json.loads(self.rfile.read(length))
            self.TODOS.append(post_values)
        else:
            self.send_error(415, "Only json data is supported.")
            return

        self.send_response(200)
        self.send_header('Content-type', 'application/json')
        self.end_headers()

        self.wfile.write(post_values)

if __name__ == '__main__':
    # Start a simple server, and loop forever
    from BaseHTTPServer import HTTPServer
    server = HTTPServer(('localhost', 8888), TodoHandler)
    print("Starting server, use <Ctrl-C> to stop")
    server.serve_forever()
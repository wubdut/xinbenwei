#!/usr/bin/env python -u
# -*- coding: utf-8 -*-

import json
from user import User
from basic.redisServer import server

class SendMessage(object):
    @staticmethod
    def lpush(message):
        dict = {}
        dict['message'] = message
        list = []
        users = []
        try:
            users = User.select()
        except:
            print "mysql user connection error" 
            return
        for user in users:
            list.append(user.mobile)
        dict['mobiles'] = list
        
        try:
            return server.lpush('SendMessage', json.dumps(dict))
        except:
            return 0
    
if __name__ == "__main__":
    text = """
    <table border="1">
<tr>
<td>row 1, cell 1</td>
<td>row 1, cell 2</td>
</tr>
<tr>
<td>row 2, cell 1</td>
<td>row 2, cell 2</td>
</tr>
</table>
    """
    SendMessage.lpush(text)
    print "haha"
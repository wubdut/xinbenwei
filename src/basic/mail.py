#!/usr/bin/python
# -*- coding: UTF-8 -*-

import smtplib
import json
# from model.shortSwing import ShortSwing
from email.mime.text import MIMEText
from email.utils import formataddr

# user = ['981128587@qq.com', '1397677607@qq.com']            # 收件人邮箱账号，我这边发送给自己
def send(user, item):
    sender = '981128587@qq.com'           # 发件人邮箱账号
    pwd = 'bepeywkchvrpbchg'              # 发件人邮箱密码
    ret=True
    try:
        msg=MIMEText(json.dumps(item), 'plain', 'utf-8')
        msg['From'] = formataddr(["信本位", sender])        # 括号里的对应发件人邮箱昵称、发件人邮箱账号
        msg['To'] = formataddr(["订阅用户", user])           # 括号里的对应收件人邮箱昵称、收件人邮箱账号
        msg['Subject'] = item['stockId'] + "  " + item['stockName']                # 邮件的主题，也可以说是标题
        server = smtplib.SMTP_SSL("smtp.qq.com", 465)     # 发件人邮箱中的SMTP服务器，端口是25
        server.login(sender, pwd)                       # 括号中对应的是发件人邮箱账号、邮箱密码
        server.sendmail(sender, [user,], msg.as_string()) # 括号中对应的是发件人邮箱账号、收件人邮箱账号、发送邮件
        server.quit()  # 关闭连接
    except Exception:  # 如果 try 中的语句没有执行，则会执行下面的 ret=False
        ret = False
    return ret

def sendToAll(userList, item):
    cnt = 0;
    for it in userList:
        user = []
        user.append(it)
        print user
        print item
        if send(user, item):
            cnt = cnt+1;
    return cnt == len(userList)
        
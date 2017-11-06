#!/bin/bash
echo "Start tornado and shortSwing."

(nohup tornadoServer.py > log/tornado.log 2>&1 & \
echo $! > log/a.pid) \
& (nohup shortSwingServer.py > log/shortSwing.log 2>&1 & \
echo $! > log/b.pid)

#!/bin/bash
echo "Start tornado and shortSwing."

(nohup python -u tornadoServer.py > log/tornado.log 2>&1 & \
echo $! > log/a.pid) \
& (nohup python -u shortSwingServer.py > log/shortSwing.log 2>&1 & \
echo $! > log/b.pid)

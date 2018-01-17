#!/bin/bash
echo "Start shortSwing."

(nohup python -u shortSwingServer.py > log/shortSwing.log 2>&1 & \
echo $! > log/a.pid)

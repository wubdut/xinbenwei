#!/bin/bash
echo "Stop."
PID=$(cat log/a.pid)
kill -9 $PID

PID=$(cat log/b.pid)
kill -9 $PID

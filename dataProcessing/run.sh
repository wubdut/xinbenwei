#!/bin/bash

nohup python -u tornadoServer.py > tornado.log 2>&1 &
nohup priceUpdateServer.py > priceUpdate.log 2>&1 &
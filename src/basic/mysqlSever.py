#!/usr/bin/env python -u
# -*- coding: utf-8 -*-

from peewee import *

mysql_db = MySQLDatabase("xinbenwei", host = "39.108.214.220", user = "root", passwd = "root", charset = "utf8")

class MySQLModel(Model):
    class Meta:
        database = mysql_db
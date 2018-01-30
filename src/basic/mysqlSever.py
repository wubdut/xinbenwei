#!/usr/bin/env python -u
# -*- coding: utf-8 -*-

from peewee import *

mysql_db = MySQLDatabase("xinbenwei", host = "39.108.214.220", user = "root", passwd = "root", charset = "utf8")



class MySQLModel(Model):
    @staticmethod
    def db_connect():
        mysql_db.connect()
    @staticmethod
    def db_close():
        mysql_db.close()
    @staticmethod
    def db_status():
        return not mysql_db.is_closed()
    class Meta:
        database = mysql_db
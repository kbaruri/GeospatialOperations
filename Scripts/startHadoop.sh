#!/bin/bash
/usr/local/hadoop/sbin/stop-dfs.sh
rm -rf /tmp/*
rm -rf /app/hadoop/tmp/*
/usr/local/hadoop/bin/hadoop namenode -format
/usr/local/hadoop/sbin/start-dfs.sh
jps
sudo netstat -plten | grep java

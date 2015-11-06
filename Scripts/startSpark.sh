#!/bin/bash
/usr/local/spark/sbin/stop-master.sh
/usr/local/spark/sbin/start-master.sh
./usr/local/spark/bin/spark-class org.apache.spark.deploy.worker.Worker spark://192.168.0.26:7077

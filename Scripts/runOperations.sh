#!/bin/bash
cd /home/user22/Desktop/GeospatialOperations/GeospatialOperations/
mvn package
/usr/local/spark/bin/spark-submit --class "DDS.team22.GeospatialOperations.UnionPolygon" --master local[4] /home/user22/Desktop/GeospatialOperations/GeospatialOperations/target/GeospatialOperations-0.0.1-SNAPSHOT.jar
cd ~/

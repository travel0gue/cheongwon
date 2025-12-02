#!/bin/bash

# G1GC (Baseline) - 기본 설정
# 용도: 범용, 균형잡힌 성능

JAR_FILE="hufs_cheongwon-0.0.1-SNAPSHOT.jar"
PROFILE="prod"

nohup java \
  -Xms512m \
  -Xmx1g \
  -XX:MetaspaceSize=128m \
  -XX:MaxMetaspaceSize=256m \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -XX:G1HeapRegionSize=4m \
  -XX:InitiatingHeapOccupancyPercent=45 \
  -XX:+ShowCodeDetailsInExceptionMessages \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=./logs/heapdump.hprof \
  -XX:+PrintGCDetails \
  -XX:+PrintGCDateStamps \
  -Xlog:gc*:file=./logs/gc-g1gc.log:time,uptime,level,tags \
  -Dspring.profiles.active=$PROFILE \
  -Duser.timezone=Asia/Seoul \
  -Dspring.web.resources.add-mappings=true \
  -Dserver.tomcat.additional-tld-skip-patterns="*.jar" \
  -jar $JAR_FILE > ./logs/app-g1gc.log 2>&1 &

echo "G1GC 모드로 실행됨. PID: $!"
echo $! > ./logs/app.pid
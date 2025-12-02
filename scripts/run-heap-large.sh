#!/bin/bash

# Heap Large (2GB)
# 용도: 고부하 환경, 대용량 데이터 처리

JAR_FILE="hufs_cheongwon-0.0.1-SNAPSHOT.jar"
PROFILE="prod"

nohup java \
  -Xms2g \
  -Xmx2g \
  -XX:MetaspaceSize=256m \
  -XX:MaxMetaspaceSize=512m \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -XX:+ShowCodeDetailsInExceptionMessages \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=./logs/heapdump.hprof \
  -Xlog:gc*:file=./logs/gc-heap-large.log:time,uptime,level,tags \
  -Dspring.profiles.active=$PROFILE \
  -Duser.timezone=Asia/Seoul \
  -Dspring.web.resources.add-mappings=true \
  -Dserver.tomcat.additional-tld-skip-patterns="*.jar" \
  -jar $JAR_FILE > ./logs/app-heap-large.log 2>&1 &

echo "Heap Large (2GB) 모드로 실행됨. PID: $!"
echo $! > ./logs/app.pid
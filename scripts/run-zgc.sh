#!/bin/bash

# ZGC - 초저지연 GC
# 용도: 낮은 응답시간이 중요한 경우 (< 1ms pause)

JAR_FILE="hufs_cheongwon-0.0.1-SNAPSHOT.jar"
PROFILE="prod"

nohup java \
  -Xms512m \
  -Xmx1g \
  -XX:MetaspaceSize=128m \
  -XX:MaxMetaspaceSize=256m \
  -XX:+UseZGC \
  -XX:ZCollectionInterval=5 \
  -XX:ZAllocationSpikeTolerance=2 \
  -XX:+ShowCodeDetailsInExceptionMessages \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=./logs/heapdump.hprof \
  -Xlog:gc*:file=./logs/gc-zgc.log:time,uptime,level,tags \
  -Dspring.profiles.active=$PROFILE \
  -Duser.timezone=Asia/Seoul \
  -Dspring.web.resources.add-mappings=true \
  -Dserver.tomcat.additional-tld-skip-patterns="*.jar" \
  -jar $JAR_FILE > ./logs/app-zgc.log 2>&1 &

echo "ZGC 모드로 실행됨. PID: $!"
echo $! > ./logs/app.pid
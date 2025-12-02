#!/bin/bash

# Parallel GC - 높은 처리량 (Throughput) 최적화
# 용도: 배치 작업이 많거나 처리량이 중요한 경우

JAR_FILE="hufs_cheongwon-0.0.1-SNAPSHOT.jar"
PROFILE="prod"

nohup java \
  -Xms512m \
  -Xmx1g \
  -XX:MetaspaceSize=128m \
  -XX:MaxMetaspaceSize=256m \
  -XX:+UseParallelGC \
  -XX:ParallelGCThreads=4 \
  -XX:MaxGCPauseMillis=100 \
  -XX:GCTimeRatio=19 \
  -XX:+ShowCodeDetailsInExceptionMessages \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=./logs/heapdump.hprof \
  -Xlog:gc*:file=./logs/gc-parallel.log:time,uptime,level,tags \
  -Dspring.profiles.active=$PROFILE \
  -Duser.timezone=Asia/Seoul \
  -Dspring.web.resources.add-mappings=true \
  -Dserver.tomcat.additional-tld-skip-patterns="*.jar" \
  -jar $JAR_FILE > ./logs/app-parallel.log 2>&1 &

echo "Parallel GC 모드로 실행됨. PID: $!"
echo $! > ./logs/app.pid
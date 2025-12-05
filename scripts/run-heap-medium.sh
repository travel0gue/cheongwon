#!/bin/bash

# Heap Medium (512MB)
# 용도: 일반적인 운영 환경 (권장 baseline)

JAR_FILE="hufs_cheongwon-0.0.1-SNAPSHOT.jar"
PROFILE="prod"

# Check if port 8080 is in use and kill the process
if lsof -Pi :8080 -sTCP:LISTEN -t >/dev/null ; then
    echo "Port 8080 is already in use. Killing the process..."
    kill $(lsof -t -i:8080)
    sleep 2
    echo "Process killed."
fi

nohup java \
  -Xms512m \
  -Xmx512m \
  -XX:MetaspaceSize=128m \
  -XX:MaxMetaspaceSize=256m \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -XX:+ShowCodeDetailsInExceptionMessages \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=./logs/heapdump.hprof \
  -Xlog:gc*:file=./logs/gc-heap-medium.log:time,uptime,level,tags \
  -Dspring.profiles.active=$PROFILE \
  -Duser.timezone=Asia/Seoul \
  -Dspring.web.resources.add-mappings=true \
  -Dserver.tomcat.additional-tld-skip-patterns="*.jar" \
  -jar $JAR_FILE > ./logs/app-heap-medium.log 2>&1 &

echo "Heap Medium (512MB) 모드로 실행됨. PID: $!"
echo $! > ./logs/app.pid
#!/bin/bash

# Profile을 파라미터로 받아서 실행하는 스크립트
# JVM 옵션은 고정, Spring Profile만 변경하여 yml 설정 비교 실험용
#
# 사용법:
#   ./scripts/run-with-profile.sh <profiles>
#
# 예시:
#   ./scripts/run-with-profile.sh pool-small
#   ./scripts/run-with-profile.sh pool-medium,batch-enabled
#   ./scripts/run-with-profile.sh pool-large,thread-high,batch-enabled

if [ -z "$1" ]; then
    echo "사용법: $0 <profiles>"
    echo ""
    echo "예시:"
    echo "  $0 pool-small"
    echo "  $0 pool-medium,batch-enabled"
    echo "  $0 pool-large,thread-high,batch-enabled"
    echo ""
    echo "사용 가능한 Profile:"
    echo "  Connection Pool: pool-small, pool-medium, pool-large"
    echo "  JPA Batch: batch-disabled, batch-enabled"
    echo "  Thread Pool: thread-low, thread-default, thread-high"
    exit 1
fi

JAR_FILE="hufs_cheongwon-0.0.1-SNAPSHOT.jar"
PROFILES="prod,$1"
LOG_SUFFIX=$(echo "$1" | tr ',' '-')

# Check if port 8080 is in use and kill the process
if lsof -Pi :8080 -sTCP:LISTEN -t >/dev/null ; then
    echo "Port 8080 is already in use. Killing the process..."
    kill $(lsof -t -i:8080)
    sleep 2
    echo "Process killed."
fi

echo "========================================="
echo "Profile 조합 실험 모드"
echo "========================================="
echo "Profiles: $PROFILES"
echo "Log files: app-${LOG_SUFFIX}.log, gc-${LOG_SUFFIX}.log"
echo "========================================="

# JVM 옵션 고정 (G1GC, 512MB-1GB)
# yml 설정 비교 실험용이므로 JVM은 안정적인 기본값 사용
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
  -Xlog:gc*:file=./logs/gc-${LOG_SUFFIX}.log:time,uptime,level,tags \
  -Dspring.profiles.active=$PROFILES \
  -Duser.timezone=Asia/Seoul \
  -Dspring.web.resources.add-mappings=true \
  -Dserver.tomcat.additional-tld-skip-patterns="*.jar" \
  -jar $JAR_FILE > ./logs/app-${LOG_SUFFIX}.log 2>&1 &

PID=$!
echo $PID > ./logs/app.pid

echo ""
echo "✅ 애플리케이션이 시작되었습니다."
echo "   PID: $PID"
echo "   Profiles: $PROFILES"
echo ""
echo "로그 확인:"
echo "   tail -f ./logs/app-${LOG_SUFFIX}.log"
echo ""
echo "종료:"
echo "   ./scripts/stop.sh"
echo ""
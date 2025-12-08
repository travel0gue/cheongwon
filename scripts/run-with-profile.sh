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

PROFILES="prod,$1"
LOG_SUFFIX=$(echo "$1" | tr ',' '-')

# 애플리케이션 디렉토리로 이동
cd /home/$(whoami)/app

# logs 디렉토리 생성
mkdir -p logs

echo "=========================================="
echo "🔍 현재 실행 중인 애플리케이션 확인 중..."
echo "=========================================="

# 8080 포트 사용 중인 프로세스 찾기 (정규식으로 PID 추출)
PID=$(ss -tulnp 2>/dev/null | grep 8080 | grep -o 'pid=[0-9]*' | cut -d'=' -f2)

# ss 명령어로 찾지 못한 경우 netstat 시도
if [ -z "$PID" ]; then
  PID=$(netstat -tulnp 2>/dev/null | grep 8080 | awk '{print $7}' | cut -d'/' -f1)
fi

# 여전히 찾지 못한 경우 lsof 시도
if [ -z "$PID" ]; then
  PID=$(lsof -ti:8080)
fi

echo "=========================================="
echo "💫 기존 애플리케이션 종료 중..."
echo "=========================================="

# 프로세스가 있으면 종료
if [ -n "$PID" ]; then
  echo "🔴 포트 8080을 사용 중인 프로세스 발견: PID $PID"
  kill -15 $PID

  # 3초 대기 후 여전히 실행 중이면 강제 종료
  sleep 3
  if ps -p $PID > /dev/null; then
    echo "⚠️ 정상 종료 실패, 강제 종료 시도 중..."
    kill -9 $PID
  fi

  echo "✅ 프로세스 $PID 종료 완료"
else
  echo "🟢 포트 8080을 사용 중인 프로세스가 없습니다."
fi

echo "=========================================="
echo "🚀 새 애플리케이션 시작 중 (Profile: $PROFILES)..."
echo "=========================================="

# 로그 파일에 타임스탬프 추가
echo "===== $(date) Profile ${PROFILES} 모드로 시작 =====" >> app.log

# JVM 옵션 고정 (G1GC, 512MB-1GB)
# yml 설정 비교 실험용이므로 JVM은 안정적인 기본값 사용
nohup java -Xms512m -Xmx1g \
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
  -jar hufs_cheongwon-0.0.1-SNAPSHOT.jar > app.log 2>&1 &

echo "💫 애플리케이션 시작 명령 실행 완료"

# 애플리케이션 시작 확인을 위해 대기
echo "🕒 애플리케이션 시작 대기 중 (10초)..."
sleep 10

echo "=========================================="
echo "📋 최근 로그 확인"
echo "=========================================="
tail -n 30 app.log

echo "=========================================="
echo "🔍 애플리케이션 실행 상태 확인"
echo "=========================================="

# 프로세스가 실행 중인지 확인
if pgrep -f "hufs_cheongwon-0.0.1-SNAPSHOT.jar" > /dev/null; then
  echo "✅ 애플리케이션이 성공적으로 시작되었습니다! (Profile: $PROFILES)"
  echo "📊 현재 실행 중인 Java 프로세스:"
  ps -ef | grep java | grep -v grep

  # 포트 리스닝 상태 확인
  echo "🌐 포트 8080 리스닝 상태:"
  ss -tulnp | grep 8080 || netstat -tulnp | grep 8080 || echo "포트 정보를 가져올 수 없습니다."
else
  echo "❌ 애플리케이션 시작 실패! 로그를 확인하세요."
  exit 1
fi

echo "=========================================="
echo "✨ 배포 프로세스 완료 (Profile: $PROFILES)"
echo "=========================================="

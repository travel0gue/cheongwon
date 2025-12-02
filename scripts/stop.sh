#!/bin/bash

# 애플리케이션 종료 스크립트

if [ -f ./logs/app.pid ]; then
    PID=$(cat ./logs/app.pid)
    echo "애플리케이션 종료 중... (PID: $PID)"
    kill $PID
    sleep 2

    # 프로세스가 여전히 살아있는지 확인
    if ps -p $PID > /dev/null; then
        echo "강제 종료 중..."
        kill -9 $PID
    fi

    rm ./logs/app.pid
    echo "애플리케이션이 종료되었습니다."
else
    echo "PID 파일을 찾을 수 없습니다. 프로세스를 직접 종료합니다."
    pkill -f hufs_cheongwon
    echo "애플리케이션이 종료되었습니다."
fi
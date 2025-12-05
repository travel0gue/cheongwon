# JVM 성능 튜닝 실험 가이드

## 목차
1. [실험 준비](#실험-준비)
2. [실험 시나리오](#실험-시나리오)
3. [측정 지표](#측정-지표)
4. [실험 실행 방법](#실험-실행-방법)
5. [결과 분석](#결과-분석)

---

## 실험 준비

### 1. 로그 디렉토리 생성
```bash
mkdir -p logs
```

### 2. JAR 빌드
```bash
./gradlew clean bootJar
cp build/libs/hufs_cheongwon-0.0.1-SNAPSHOT.jar .
```

### 3. 스크립트 실행 권한 부여
```bash
chmod +x scripts/*.sh
```

---

## 실험 시나리오

### 시나리오 A: GC 알고리즘 비교
**목적**: 다양한 GC 알고리즘의 pause time, throughput 비교

#### 비교군
1. **G1GC (Baseline)**
   ```bash
   ./scripts/run-g1gc.sh
   ```
   - 범용 목적, 균형잡힌 성능
   - 예상: 중간 정도의 pause time, 중간 throughput

2. **ZGC**
   ```bash
   ./scripts/run-zgc.sh
   ```
   - 초저지연 GC (< 1ms pause)
   - 예상: 매우 낮은 pause time, 약간 낮은 throughput

3. **Parallel GC**
   ```bash
   ./scripts/run-parallel-gc.sh
   ```
   - 높은 throughput 최적화
   - 예상: 높은 throughput, 높은 pause time

#### 측정 지표
- GC pause time (avg, p99, p999)
- GC frequency (Minor GC, Major GC 횟수)
- Throughput (처리량)
- 응답 시간 (p50, p95, p99)

#### 부하 테스트 시나리오
```
# 예시: K6, JMeter, Gatling 등 사용
- 동시 사용자: 100명
- Ramp-up: 30초
- 지속 시간: 5분
- 요청 패턴: 청원 목록 조회 70%, 청원 생성 20%, 청원 동의 10%
```

---

### 시나리오 B: Heap 메모리 크기 비교
**목적**: 적절한 Heap 크기 찾기, OOM 발생 지점 확인

#### 비교군
1. **Small Heap (256MB)**
   ```bash
   ./scripts/run-heap-small.sh
   ```
   - 예상: GC 빈도 높음, OOM 위험

2. **Medium Heap (512MB)**
   ```bash
   ./scripts/run-heap-medium.sh
   ```
   - 예상: 균형잡힌 성능

3. **Large Heap (2GB)**
   ```bash
   ./scripts/run-heap-large.sh
   ```
   - 예상: GC 빈도 낮음, pause time 증가 가능

#### 측정 지표
- Heap 사용률 (%, MB)
- GC 빈도 (회/분)
- OOM 발생 여부
- 메모리 증가율 (Memory leak 확인)

---

### 시나리오 C: HikariCP Connection Pool 크기 비교
**목적**: DB 연결 풀 최적 크기 찾기

#### 비교군
1. **Small Pool (max: 5, min: 2)**
   ```bash
   # run-g1gc.sh 실행 + profile 설정
   -Dspring.profiles.active=prod,pool-small
   ```

2. **Medium Pool (max: 10, min: 5)**
   ```bash
   -Dspring.profiles.active=prod,pool-medium
   ```

3. **Large Pool (max: 20, min: 10)**
   ```bash
   -Dspring.profiles.active=prod,pool-large
   ```

#### 측정 지표
- Connection wait time (ms)
- Active connections (개수)
- Idle connections (개수)
- Connection pool exhaustion 발생 횟수
- DB query response time (ms)

---

### 시나리오 D: JPA Batch 설정 비교
**목적**: Batch INSERT/UPDATE 성능 개선 효과 측정

#### 비교군
1. **Batch Disabled**
   ```bash
   -Dspring.profiles.active=prod,batch-disabled
   ```

2. **Batch Enabled (size: 50)**
   ```bash
   -Dspring.profiles.active=prod,batch-enabled
   ```

#### 측정 지표
- 청원 생성 시 SQL 쿼리 개수
- 청원 목록 조회 시 N+1 문제 발생 여부
- INSERT/UPDATE 응답 시간 (ms)
- DB 부하 (CPU, I/O)

#### 테스트 케이스
```
1. 청원 100개 일괄 생성
2. 청원 목록 조회 (페이지당 20개)
3. 청원 100개 동의 처리
```

---

### 시나리오 E: Tomcat Thread Pool 크기 비교
**목적**: 적절한 스레드 풀 크기 찾기

#### 비교군
1. **Low Concurrency (max: 50, min: 5)**
   ```bash
   -Dspring.profiles.active=prod,thread-low
   ```

2. **Default (max: 200, min: 10)**
   ```bash
   -Dspring.profiles.active=prod,thread-default
   ```

3. **High Concurrency (max: 400, min: 50)**
   ```bash
   -Dspring.profiles.active=prod,thread-high
   ```

#### 측정 지표
- Thread pool utilization (%)
- Active threads (개수)
- Queue size (대기 중인 요청)
- Request rejection 발생 횟수
- 응답 시간 (p95, p99)

---

## 측정 지표

### 1. JVM 메트릭 (GC 로그 분석)
```bash
# GC 로그 파일 위치
logs/gc-*.log
```

분석 도구:
- GCViewer: https://github.com/chewiebug/GCViewer
- GCeasy: https://gceasy.io (온라인)

### 2. 애플리케이션 메트릭
```bash
# 애플리케이션 로그
logs/app-*.log
```

### 3. 시스템 메트릭
```bash
# CPU, 메모리 사용률 모니터링
top -pid <PID>

# 또는
htop -p <PID>
```

### 4. Actuator 메트릭 (추가 설정 필요)
```yaml
# application.yml에 추가
management:
  endpoints:
    web:
      exposure:
        include: health,metrics,prometheus
  metrics:
    export:
      prometheus:
        enabled: true
```

엔드포인트:
- `http://localhost:8080/actuator/metrics/jvm.memory.used`
- `http://localhost:8080/actuator/metrics/hikaricp.connections`
- `http://localhost:8080/actuator/metrics/http.server.requests`

---

## 실험 실행 방법

### 단계별 실험 프로세스

#### 1. 실험 준비
```bash
# 기존 프로세스 종료
pkill -f hufs_cheongwon

# 로그 정리 (선택)
rm -f logs/*.log

# JAR 빌드
./gradlew clean bootJar
cp build/libs/hufs_cheongwon-0.0.1-SNAPSHOT.jar .
```

#### 2. 실험 실행
```bash
# 예: G1GC 테스트
./scripts/run-g1gc.sh

# PID 확인
cat logs/app.pid

# 로그 확인
tail -f logs/app-g1gc.log
```

#### 3. 부하 테스트 실행
```bash
# 예시 (부하테스트 도구 사용)
# 실제 부하테스트 명령어로 교체 필요
k6 run load-test.js
```

#### 4. 메트릭 수집
```bash
# GC 로그 분석
cat logs/gc-g1gc.log

# 애플리케이션 로그 확인
grep -i "error\|exception" logs/app-g1gc.log

# 시스템 리소스 확인
ps aux | grep java
```

#### 5. 프로세스 종료
```bash
kill $(cat logs/app.pid)
# 또는
pkill -f hufs_cheongwon
```

#### 6. 다음 실험으로 이동
```bash
# 예: ZGC 테스트
./scripts/run-zgc.sh
# 부하테스트 다시 실행...
```

---

## 결과 분석

### 1. GC 성능 비교표 (예시)

| GC 알고리즘 | Avg Pause (ms) | P99 Pause (ms) | Minor GC 횟수 | Major GC 횟수 | Throughput (req/s) |
|------------|----------------|----------------|---------------|---------------|--------------------|
| G1GC       | ?              | ?              | ?             | ?             | ?                  |
| ZGC        | ?              | ?              | ?             | ?             | ?                  |
| Parallel   | ?              | ?              | ?             | ?             | ?                  |

### 2. Heap 크기 비교표 (예시)

| Heap 크기 | GC 빈도 (회/분) | Heap 사용률 (%) | OOM 발생 | 평균 응답시간 (ms) |
|-----------|----------------|-----------------|----------|--------------------|
| 256MB     | ?              | ?               | ?        | ?                  |
| 512MB     | ?              | ?               | ?        | ?                  |
| 2GB       | ?              | ?               | ?        | ?                  |

### 3. Connection Pool 비교표 (예시)

| Pool 크기 | Avg Wait (ms) | Max Active | Pool Exhausted | Avg Query (ms) |
|-----------|---------------|------------|----------------|----------------|
| Small     | ?             | ?          | ?              | ?              |
| Medium    | ?             | ?          | ?              | ?              |
| Large     | ?             | ?          | ?              | ?              |

---

## 권장 조합 (실험 후 업데이트)

실험 결과를 바탕으로 최적 조합을 기록:

```bash
# 예시 (실험 후 업데이트 필요)
# Best for Low Latency
-XX:+UseZGC -Xms1g -Xmx1g
-Dspring.profiles.active=prod,pool-medium,thread-default

# Best for High Throughput
-XX:+UseParallelGC -Xms2g -Xmx2g
-Dspring.profiles.active=prod,pool-large,thread-high,batch-enabled

# Best for Balanced
-XX:+UseG1GC -Xms512m -Xmx1g
-Dspring.profiles.active=prod,pool-medium,thread-default,batch-enabled
```

---

## 추가 모니터링 도구

### 1. JMX 모니터링
```bash
# JVisualVM 사용
jvisualvm --openpid $(cat logs/app.pid)
```

### 2. Heap Dump 분석
```bash
# OOM 발생 시 자동 생성됨
# HeapDumpPath=./logs/heapdump.hprof

# Eclipse MAT로 분석
# https://www.eclipse.org/mat/
```

### 3. Thread Dump
```bash
jstack $(cat logs/app.pid) > logs/thread-dump.txt
```

---

## 주의사항

1. **한 번에 하나의 변수만 변경**: 여러 설정을 동시에 바꾸면 어떤 요인이 성능에 영향을 미쳤는지 파악 불가
2. **충분한 Warm-up**: 첫 요청은 JIT 컴파일, 클래스 로딩 등으로 느릴 수 있음
3. **일관된 테스트 환경**: 동일한 데이터셋, 동일한 부하 패턴 유지
4. **여러 번 반복**: 한 번의 결과로 판단하지 말고 최소 3회 이상 반복
5. **DB 상태 고려**: 테스트 전 DB 데이터 일관성 유지 (초기화 또는 동일 상태)

---

## 문제 해결

### 애플리케이션이 시작되지 않음
```bash
# 로그 확인
cat logs/app-*.log

# 포트 충돌 확인
lsof -i :8080

# 기존 프로세스 종료
pkill -f hufs_cheongwon
```

### OOM 발생
```bash
# Heap dump 분석
# logs/heapdump.hprof 파일 생성 확인
ls -lh logs/heapdump.hprof

# Eclipse MAT 또는 VisualVM으로 분석
```

### DB Connection 에러
```bash
# HikariCP 로그 확인
grep -i "hikari" logs/app-*.log

# DB 연결 테스트
mysql -h hufs-cheongwon-db.c9c4ywqgumtc.ap-northeast-2.rds.amazonaws.com -u admin -p
```

---

## 참고 자료

- [HotSpot Virtual Machine Garbage Collection Tuning Guide](https://docs.oracle.com/en/java/javase/21/gctuning/)
- [HikariCP Configuration](https://github.com/brettwooldridge/HikariCP#configuration-knobs-baby)
- [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
- [JVM Performance Tuning](https://www.oracle.com/technical-resources/articles/java/performance-tuning.html)
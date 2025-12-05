# 성능 테스트 스크립트 사용 가이드

## 빠른 시작

### 1. 준비
```bash
# JAR 빌드
./gradlew clean bootJar
cp build/libs/hufs_cheongwon-0.0.1-SNAPSHOT.jar .

# 로그 디렉토리 생성
mkdir -p logs
```

### 2. 실행

#### A. JVM 옵션 비교 (GC 알고리즘, Heap 크기)
```bash
# GC 알고리즘 테스트 (yml 고정)
./scripts/run-g1gc.sh       # G1GC (권장 baseline)
./scripts/run-zgc.sh         # ZGC (저지연)
./scripts/run-parallel-gc.sh # Parallel GC (고처리량)

# Heap 크기 테스트 (yml 고정)
./scripts/run-heap-small.sh  # 256MB
./scripts/run-heap-medium.sh # 512MB
./scripts/run-heap-large.sh  # 2GB
```

#### B. yml 설정 비교 (Connection Pool, JPA Batch, Thread Pool)
```bash
# JVM 옵션 고정 (G1GC + 512MB-1GB), Profile만 변경

# Connection Pool 비교
./scripts/run-with-profile.sh pool-small
./scripts/run-with-profile.sh pool-medium
./scripts/run-with-profile.sh pool-large

# JPA Batch 비교
./scripts/run-with-profile.sh batch-disabled
./scripts/run-with-profile.sh batch-enabled

# Thread Pool 비교
./scripts/run-with-profile.sh thread-low
./scripts/run-with-profile.sh thread-default
./scripts/run-with-profile.sh thread-high

# 조합 테스트
./scripts/run-with-profile.sh pool-large,batch-enabled
./scripts/run-with-profile.sh pool-medium,thread-high,batch-enabled
```

### 3. 종료
```bash
./scripts/stop.sh
```

---

## Profile 조합 사용법

Spring Boot profile을 조합하여 다양한 설정 테스트 가능:

```bash
# 예시: G1GC + Large Connection Pool
nohup java \
  -Xms512m -Xmx1g \
  -XX:+UseG1GC \
  -Dspring.profiles.active=prod,pool-large \
  -jar hufs_cheongwon-0.0.1-SNAPSHOT.jar > logs/app.log 2>&1 &

# 예시: ZGC + Batch Enabled + High Thread Pool
nohup java \
  -Xms1g -Xmx1g \
  -XX:+UseZGC \
  -Dspring.profiles.active=prod,batch-enabled,thread-high \
  -jar hufs_cheongwon-0.0.1-SNAPSHOT.jar > logs/app.log 2>&1 &
```

---

## 사용 가능한 Profile

### Connection Pool
- `pool-small`: max=5, min=2
- `pool-medium`: max=10, min=5 (권장)
- `pool-large`: max=20, min=10

### JPA Batch
- `batch-disabled`: batch_size=1 (baseline)
- `batch-enabled`: batch_size=50

### Thread Pool
- `thread-low`: max=50, min=5
- `thread-default`: max=200, min=10 (권장)
- `thread-high`: max=400, min=50

---

## 로그 위치

```
logs/
# JVM 옵션 비교 스크립트 로그
├── app-g1gc.log                    # G1GC 애플리케이션 로그
├── app-zgc.log                     # ZGC 애플리케이션 로그
├── app-parallel.log                # Parallel GC 애플리케이션 로그
├── app-heap-small.log              # Heap Small 애플리케이션 로그
├── gc-g1gc.log                     # G1GC GC 로그
├── gc-zgc.log                      # ZGC GC 로그

# Profile 비교 스크립트 로그 (run-with-profile.sh)
├── app-pool-small.log              # pool-small profile 로그
├── app-pool-medium.log             # pool-medium profile 로그
├── app-pool-large-batch-enabled.log # 조합 profile 로그
├── gc-pool-small.log               # pool-small GC 로그
├── gc-thread-high.log              # thread-high GC 로그

# 기타
├── heapdump.hprof                  # Heap dump (OOM 발생 시)
└── app.pid                         # 현재 실행 중인 프로세스 PID
```

---

## 상세 가이드

전체 실험 가이드는 `PERFORMANCE_TESTING_GUIDE.md` 참고
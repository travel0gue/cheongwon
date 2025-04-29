# 기본 이미지 선택 (JDK 버전은 프로젝트에 맞게 선택)
FROM openjdk:21-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# JAR 파일 복사
COPY build/libs/*.jar app.jar

# 컨테이너가 사용할 포트 노출
EXPOSE 8080

# 컨테이너 실행 시 수행할 명령어
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
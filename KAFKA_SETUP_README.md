# 🚀 Project - Kafka Setup Guide

이 가이드는 Docker와 Kafka를 설정하고 실행하는 방법을 설명합니다.

## 📋 목차

1. [시스템 요구사항](#시스템-요구사항)
2. [Docker 설치 및 설정](#docker-설치-및-설정)
3. [Kafka 환경 설정](#kafka-환경-설정)
4. [애플리케이션 실행](#애플리케이션-실행)
5. [검증 및 테스트](#검증-및-테스트)
6. [문제 해결](#문제-해결)

## 🖥️ 시스템 요구사항

- **OS**: macOS 10.15+ / Windows 10+ / Ubuntu 18.04+
- **Docker**: Docker Desktop 4.0+
- **Java**: OpenJDK 21+
- **메모리**: 최소 8GB RAM (Kafka 실행을 위해)
- **디스크**: 최소 10GB 여유 공간

## 🐳 Docker 설치 및 설정

### 1. Docker Desktop 설치

#### macOS
```bash
# Homebrew를 통한 설치
brew install --cask docker

# 또는 공식 웹사이트에서 다운로드
# https://www.docker.com/products/docker-desktop
```

#### Windows
```bash
# WSL2 설치 후 Docker Desktop 설치
# https://docs.docker.com/desktop/install/windows-install/
```

#### Ubuntu
```bash
# Docker Engine 설치
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
sudo usermod -aG docker $USER
```

### 2. Docker 설치 확인

```bash
# Docker 버전 확인
docker --version

# Docker Compose 버전 확인
docker-compose --version

# Docker 서비스 상태 확인
docker ps
```

### 3. Docker 권한 문제 해결 (macOS/Linux)

만약 `docker` 명령어가 인식되지 않는 경우:

```bash
# 심볼릭 링크 확인
ls -la /usr/local/bin/docker

# 필요시 심볼릭 링크 재생성
sudo rm /usr/local/bin/docker
sudo ln -s /Applications/Docker.app/Contents/Resources/bin/docker /usr/local/bin/docker
```

## ☕ Java 설치

### macOS
```bash
# Homebrew를 통한 설치
brew install openjdk@21

# 환경변수 설정
echo 'export PATH="/opt/homebrew/opt/openjdk@21/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

### Ubuntu
```bash
sudo apt update
sudo apt install openjdk-21-jdk
```

### 설치 확인
```bash
java --version
```

## 📦 Kafka 환경 설정

### 1. 프로젝트 클론 및 이동

```bash
cd /path/to/hijab/project
```

### 2. Kafka 서비스 시작

```bash
# Kafka 및 관련 서비스 시작
docker-compose -f docker-compose-kafka.yml up -d

# 서비스 상태 확인
docker-compose -f docker-compose-kafka.yml ps
```

### 3. 서비스 시작 대기

```bash
# 모든 서비스가 완전히 시작될 때까지 대기 (약 2-3분)
echo "⏳ Kafka 서비스 시작 대기 중..."
sleep 180
```

### 4. Kafka 토픽 생성

```bash
# 토픽 생성 스크립트 실행
./scripts/create-kafka-topics.sh
```

## 🏃‍♂️ 애플리케이션 실행

### 1. Gradle 빌드

```bash
# 프로젝트 빌드
./gradlew clean build

# 또는 테스트 제외하고 빌드
./gradlew clean build -x test
```

### 2. 애플리케이션 실행

```bash
# Spring Boot 애플리케이션 실행
./gradlew bootRun
```

### 3. 애플리케이션 접속

- **메인 애플리케이션**: http://localhost:8090
- **API 문서 (Swagger)**: http://localhost:8090/api-docs
- **H2 데이터베이스 콘솔**: http://localhost:8090/h2-console
- **Kafka UI**: http://localhost:8080

## 🧪 검증 및 테스트

### 1. Kafka 연결 테스트

```bash
# Kafka 연결 및 메시지 전송/수신 테스트
./scripts/test-kafka.sh
```

### 2. 수동 테스트

#### Kafka 토픽 확인
```bash
# 토픽 목록 확인
docker exec kafka kafka-topics --bootstrap-server localhost:9092 --list

# 토픽 상세 정보 확인
docker exec kafka kafka-topics --bootstrap-server localhost:9092 --describe --topic example-topic
```

#### 메시지 전송 테스트
```bash
# 프로듀서로 메시지 전송
docker exec -it kafka kafka-console-producer \
    --bootstrap-server localhost:9092 \
    --topic example-topic
```

#### 메시지 수신 테스트
```bash
# 컨슈머로 메시지 수신
docker exec -it kafka kafka-console-consumer \
    --bootstrap-server localhost:9092 \
    --topic example-topic \
    --from-beginning
```

### 3. 애플리케이션 API 테스트

#### SSE 연결 테스트
```bash
# SSE 구독 테스트
curl -N "http://localhost:8090/api/sse/subscribe?requestId=test-123"
```

#### 컬러 분석 요청 테스트
```bash
# 컬러 분석 API 호출 (예시)
curl -X POST "http://localhost:8090/api/color-analysis" \
  -H "Content-Type: application/json" \
  -d '{"imageUrl": "test.jpg", "requestId": "test-123"}'
```

## 🔧 문제 해결

### 1. Docker 관련 문제

#### Docker 명령어가 인식되지 않는 경우
```bash
# Docker Desktop이 실행 중인지 확인
ps aux | grep -i docker

# PATH에 Docker 경로 추가
echo 'export PATH="/Applications/Docker.app/Contents/Resources/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

#### 포트 충돌 문제
```bash
# 사용 중인 포트 확인
lsof -i :9092
lsof -i :2181
lsof -i :8080

# 충돌하는 프로세스 종료
sudo kill -9 <PID>
```

### 2. Kafka 관련 문제

#### Kafka 서비스가 시작되지 않는 경우
```bash
# 로그 확인
docker-compose -f docker-compose-kafka.yml logs kafka
docker-compose -f docker-compose-kafka.yml logs zookeeper

# 서비스 재시작
docker-compose -f docker-compose-kafka.yml restart
```

#### 토픽 생성 실패
```bash
# Kafka 서비스 상태 확인
docker exec kafka kafka-topics --bootstrap-server localhost:9092 --list

# 수동으로 토픽 생성
docker exec kafka kafka-topics \
    --bootstrap-server localhost:9092 \
    --create \
    --topic example-topic \
    --partitions 3 \
    --replication-factor 1
```

### 3. 애플리케이션 관련 문제

#### 빌드 실패
```bash
# Gradle 캐시 정리
./gradlew clean

# 의존성 새로고침
./gradlew --refresh-dependencies build
```

#### 연결 오류
```bash
# 애플리케이션 로그 확인
./gradlew bootRun --debug

# Kafka 연결 설정 확인
# application.properties에서 spring.kafka.bootstrap-servers=localhost:9092 확인
```

## 📊 모니터링

### 1. Kafka UI 접속
- URL: http://localhost:8080
- 기능: 토픽, 파티션, 메시지 모니터링

### 2. 컨테이너 상태 확인
```bash
# 모든 컨테이너 상태 확인
docker ps

# 특정 서비스 로그 확인
docker logs kafka
docker logs zookeeper
docker logs kafka-ui
```

### 3. 시스템 리소스 모니터링
```bash
# Docker 리소스 사용량 확인
docker stats

# 디스크 사용량 확인
docker system df
```

## 🧹 정리

### 서비스 중지
```bash
# Kafka 서비스 중지
docker-compose -f docker-compose-kafka.yml down

# 모든 Docker 컨테이너 중지
docker stop $(docker ps -q)
```

### 데이터 정리
```bash
# 볼륨 삭제 (주의: 모든 데이터가 삭제됩니다)
docker-compose -f docker-compose-kafka.yml down -v

# Docker 시스템 정리
docker system prune -a
```

## 📚 추가 리소스

- [Docker 공식 문서](https://docs.docker.com/)
- [Apache Kafka 공식 문서](https://kafka.apache.org/documentation/)
- [Spring Kafka 가이드](https://spring.io/guides/gs/messaging-with-kafka/)
- [Confluent Platform 문서](https://docs.confluent.io/)

## 🤝 지원

문제가 발생하거나 추가 도움이 필요한 경우:

1. 이 README의 [문제 해결](#문제-해결) 섹션 확인
2. Docker 및 Kafka 로그 확인
3. 프로젝트 이슈 트래커에 문제 등록

---

**Happy Coding! 🎉**

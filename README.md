# 🧕 Hijab Project - Personal Color Analysis Backend

퍼스널 컬러 분석을 위한 Spring Boot 백엔드 애플리케이션입니다.

## 📋 프로젝트 구조

1. **클라이언트 요청 시**: `color_analysis_request` 테이블에 `status = PENDING`으로 저장
2. **Kafka에 메시지 전송**: 비동기 처리를 위한 메시지 큐 활용
3. **분석 서버에서 결과 수신 후**: 외부 PCA 서버에서 분석 완료
4. **DB 업데이트**: `status = DONE`, `result = ...`로 업데이트
5. **SSE로 결과 push**: 실시간 결과 전송

## 🚀 빠른 시작

### 1. 환경 설정
```bash
# 전체 설정 검증
./scripts/verify-setup.sh

# Kafka 서비스 시작
docker compose -f docker-compose-kafka.yml up -d

# 토픽 생성
./scripts/create-kafka-topics.sh
```

### 2. 애플리케이션 실행
```bash
# Gradle 빌드
./gradlew clean build

# 애플리케이션 실행
./gradlew bootRun
```

### 3. 접속 정보
- **메인 애플리케이션**: http://localhost:8090
- **API 문서 (Swagger)**: http://localhost:8090/api-docs
- **H2 데이터베이스 콘솔**: http://localhost:8090/h2-console
- **Kafka UI**: http://localhost:8080

## 🐳 Docker 관리

### Kafka 컨테이너 실행
```bash
# 새로운 Kafka Compose 파일 사용
docker compose -f docker-compose-kafka.yml up -d
```

### 컨테이너 상태 확인
```bash
# 모든 컨테이너 상태 확인
docker ps

# Kafka 컨테이너 정상 작동 확인
docker ps | grep kafka
```

### Kafka 컨테이너 중지
```bash
# 서비스 중지
docker compose -f docker-compose-kafka.yml down

# 컨테이너 정리 + 볼륨 삭제까지
docker compose -f docker-compose-kafka.yml down -v
```

### Kafka 관리
```bash
# 현재 토픽 리스트 확인
docker exec -it kafka kafka-topics --bootstrap-server localhost:9092 --list

# 토픽 상세 정보 확인
docker exec -it kafka kafka-topics --bootstrap-server localhost:9092 --describe --topic color-analysis-topic

# Kafka 로그 확인
docker logs kafka

# 메시지 전송 테스트
./scripts/test-kafka.sh
```

## 📁 프로젝트 구조

```
hijab/
├── src/main/java/com/hijab/
│   ├── config/           # 설정 클래스들
│   │   ├── KafkaConfig.java
│   │   ├── ActiveMQConfig.java
│   │   └── ...
│   ├── common/           # 공통 컴포넌트
│   │   ├── kafka/        # Kafka 관련
│   │   ├── activemq/     # ActiveMQ 관련
│   │   └── sse/          # Server-Sent Events
│   ├── controller/       # REST API 컨트롤러
│   ├── service/          # 비즈니스 로직
│   └── repository/       # 데이터 접근 계층
├── scripts/              # 유틸리티 스크립트
│   ├── create-kafka-topics.sh
│   ├── test-kafka.sh
│   └── verify-setup.sh
├── docker-compose-kafka.yml  # Kafka 환경 설정
└── KAFKA_SETUP_README.md     # 상세 설정 가이드
```

## 🔧 주요 기능

### Kafka 메시징
- **토픽**: `color-analysis-topic`
- **메시지 타입**: `COLOR_ANALYSIS`
- **비동기 처리**: 이미지 분석 요청을 비동기로 처리

### Server-Sent Events (SSE)
- **실시간 결과 전송**: 분석 완료 시 클라이언트에 즉시 전송
- **연결 관리**: 자동 재연결 및 타임아웃 처리

### 데이터베이스
- **H2 (개발)**: 인메모리 데이터베이스
- **PostgreSQL (운영)**: 프로덕션 데이터베이스
- **Redis**: 캐싱 및 세션 저장소

## 🧪 테스트

### Kafka 연결 테스트
```bash
# Kafka 연결 및 메시지 전송/수신 테스트
./scripts/test-kafka.sh
```

### 전체 설정 검증
```bash
# 모든 서비스 상태 및 설정 검증
./scripts/verify-setup.sh
```

## 📚 추가 문서

- **[KAFKA_SETUP_README.md](KAFKA_SETUP_README.md)**: Docker부터 Kafka 설치 및 실행 방법
- **[TEST_README.md](TEST_README.md)**: 테스트 관련 가이드
- **[Info.md](Info.md)**: 프로젝트 정보

## 🔒 보안 고려사항

### 이미지 저장
- 얼굴 이미지를 저장하거나 분석 후 삭제 여부를 결정해야 합니다
- AWS S3와 같은 클라우드 스토리지 사용을 추천합니다

### 성능 최적화
- 분석 요청이 많을 경우 비동기 처리 및 캐싱 전략을 고려하세요
- Kafka 파티션 수와 컨슈머 수를 조정하여 처리량을 최적화하세요

### 데이터 보안
- 사용자 데이터는 암호화하여 저장하며, 민감한 정보는 최소한으로 저장합니다
- JWT 토큰을 통한 인증 및 권한 관리

## 🤝 기여하기

1. 이슈를 생성하여 버그나 개선사항을 보고하세요
2. Fork하여 브랜치를 생성하고 변경사항을 커밋하세요
3. Pull Request를 생성하여 변경사항을 제안하세요

---

**Happy Coding! 🎉**# spring-batch

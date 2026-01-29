#!/bin/bash

# Hijab Project 전체 설정 검증 스크립트

echo "🔍 Hijab Project 설정 검증 시작..."
echo "=================================="

# 1. Docker 상태 확인
echo "📊 1. Docker 서비스 상태 확인:"
if docker ps > /dev/null 2>&1; then
    echo "✅ Docker가 정상적으로 실행 중입니다."
else
    echo "❌ Docker가 실행되지 않았습니다."
    exit 1
fi

# 2. 필수 컨테이너 상태 확인
echo ""
echo "🐳 2. 필수 컨테이너 상태 확인:"
required_containers=("zookeeper" "kafka" "redis" "postgres" "kafka-ui")
all_healthy=true

for container in "${required_containers[@]}"; do
    if docker ps --format "table {{.Names}}\t{{.Status}}" | grep -q "$container.*Up"; then
        echo "✅ $container: 실행 중"
    else
        echo "❌ $container: 실행되지 않음"
        all_healthy=false
    fi
done

if [ "$all_healthy" = false ]; then
    echo "❌ 일부 컨테이너가 실행되지 않았습니다."
    exit 1
fi

# 3. Kafka 토픽 확인
echo ""
echo "📝 3. Kafka 토픽 확인:"
if docker exec kafka kafka-topics --bootstrap-server localhost:9092 --list | grep -q "example-topic"; then
    echo "✅ example-topic이 존재합니다."
else
    echo "❌ example-topic이 존재하지 않습니다."
    exit 1
fi

# 4. 포트 접근 확인
echo ""
echo "🌐 4. 포트 접근 확인:"
ports=("2181:Zookeeper" "9092:Kafka" "6379:Redis" "5432:PostgreSQL" "8080:Kafka UI")

for port_info in "${ports[@]}"; do
    port=$(echo $port_info | cut -d: -f1)
    service=$(echo $port_info | cut -d: -f2)
    
    if lsof -i :$port > /dev/null 2>&1; then
        echo "✅ $service (포트 $port): 접근 가능"
    else
        echo "❌ $service (포트 $port): 접근 불가"
    fi
done

# 5. Java 버전 확인
echo ""
echo "☕ 5. Java 버전 확인:"
if command -v java > /dev/null 2>&1; then
    java_version=$(java --version 2>&1 | head -n 1)
    echo "✅ Java: $java_version"
else
    echo "❌ Java가 설치되지 않았습니다."
fi

# 6. Gradle 확인
echo ""
echo "📦 6. Gradle 확인:"
if [ -f "./gradlew" ]; then
    echo "✅ Gradle Wrapper가 존재합니다."
    ./gradlew --version > /dev/null 2>&1
    if [ $? -eq 0 ]; then
        echo "✅ Gradle이 정상적으로 작동합니다."
    else
        echo "❌ Gradle 실행에 문제가 있습니다."
    fi
else
    echo "❌ Gradle Wrapper가 존재하지 않습니다."
fi

# 7. 애플리케이션 설정 확인
echo ""
echo "⚙️ 7. 애플리케이션 설정 확인:"
if [ -f "src/main/resources/application.properties" ]; then
    echo "✅ application.properties 파일이 존재합니다."
    
    # Kafka 설정 확인
    if grep -q "spring.kafka.bootstrap-servers=localhost:9092" src/main/resources/application.properties; then
        echo "✅ Kafka 설정이 올바릅니다."
    else
        echo "❌ Kafka 설정이 올바르지 않습니다."
    fi
    
    # Redis 설정 확인
    if grep -q "spring.data.redis.host=localhost" src/main/resources/application.properties; then
        echo "✅ Redis 설정이 올바릅니다."
    else
        echo "❌ Redis 설정이 올바르지 않습니다."
    fi
else
    echo "❌ application.properties 파일이 존재하지 않습니다."
fi

echo ""
echo "🎉 검증 완료!"
echo "=================================="
echo ""
echo "📋 다음 단계:"
echo "1. 애플리케이션 실행: ./gradlew bootRun"
echo "2. Kafka UI 접속: http://localhost:8080"
echo "3. API 문서 확인: http://localhost:8090/api-docs"
echo "4. H2 데이터베이스: http://localhost:8090/h2-console"
echo ""
echo "🔧 문제가 있다면 KAFKA_SETUP_README.md를 참조하세요."

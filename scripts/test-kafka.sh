#!/bin/bash

# Kafka 연결 및 메시지 테스트 스크립트

echo "🧪 Kafka 연결 테스트 시작..."

# 1. Kafka 서비스 상태 확인
echo "📊 Kafka 서비스 상태 확인:"
docker ps | grep -E "(kafka|zookeeper)"

# 2. 토픽 목록 확인
echo "📋 토픽 목록 확인:"
docker exec kafka kafka-topics --bootstrap-server localhost:9092 --list

# 3. 테스트 메시지 전송
echo "📤 테스트 메시지 전송:"
docker exec kafka kafka-console-producer \
    --bootstrap-server localhost:9092 \
    --topic example-topic \
    --property "parse.key=true" \
    --property "key.separator=:" <<EOF
test-key-1:{"type":"COLOR_ANALYSIS","payload":{"requestId":"test-123","imageUrl":"test.jpg"}}
test-key-2:{"type":"COLOR_ANALYSIS","payload":{"requestId":"test-456","imageUrl":"test2.jpg"}}
EOF

# 4. 메시지 수신 테스트 (5초 동안)
echo "📥 메시지 수신 테스트 (5초):"
if command -v timeout >/dev/null 2>&1; then
    timeout 5s docker exec kafka kafka-console-consumer \
        --bootstrap-server localhost:9092 \
        --topic example-topic \
        --from-beginning \
        --property "print.timestamp=true" \
        --property "print.key=true" \
        --property "print.value=true" || true
else
    # macOS에서는 gtimeout 사용 또는 직접 종료
    echo "timeout 명령어가 없습니다. 메시지 수신 테스트를 건너뜁니다."
    echo "수동으로 테스트하려면: docker exec -it kafka kafka-console-consumer --bootstrap-server localhost:9092 --topic example-topic --from-beginning"
fi

# 5. 컨슈머 그룹 확인
echo "👥 컨슈머 그룹 확인:"
docker exec kafka kafka-consumer-groups --bootstrap-server localhost:9092 --list

# 6. 토픽 상세 정보 확인
echo "📊 example-topic 상세 정보:"
docker exec kafka kafka-topics \
    --bootstrap-server localhost:9092 \
    --describe \
    --topic example-topic

echo "✅ Kafka 테스트 완료!"

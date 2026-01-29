package com.hijab.common.kafka.consumer;

import static com.hijab.common.exception.ExceptionStatus.SERVER_ERROR;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hijab.common.exception.CustomException;
import com.hijab.common.kafka.message.KafkaMessageEnvelope;
import com.hijab.common.kafka.message.MessagePayload;
import com.hijab.common.sse.component.SseEmitters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true", matchIfMissing = false)
public class ExampleConsumer {

    private final ObjectMapper objectMapper;
    private final SseEmitters sseEmitters;

    @KafkaListener(topics = "example-topic", groupId = "example-group")
    public void listen(String envelopeJson) {
        log.info("📥 Received envelope: {}", envelopeJson);
        try {
            KafkaMessageEnvelope envelope = objectMapper.readValue(envelopeJson, KafkaMessageEnvelope.class);

            log.info("📥 Received envelope: {}", envelope);

            MessagePayload message = objectMapper.convertValue(envelope.getPayload(), MessagePayload.class);

            sseEmitters.send(message.getRequestId(),
                    KafkaMessageEnvelope.MessageType.TEMP.name(),
                    new Object());

        } catch (Exception e) {
            log.error("Kafka 메시지 파싱 실패", e);
            throw new CustomException(SERVER_ERROR);
        }
    }
}
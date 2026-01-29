package com.springbatch.common.kafka.producer;

import com.springbatch.common.kafka.message.KafkaMessageEnvelope;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true", matchIfMissing = false)
public class ExampleProducer {

    private final KafkaTemplate<String, KafkaMessageEnvelope> kafkaTemplate;

    public void sendAnalysisRequest(KafkaMessageEnvelope envelope) {
        kafkaTemplate.send("example-topic", envelope);
    }
}
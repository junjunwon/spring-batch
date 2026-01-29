package com.hijab.common.kafka.message;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KafkaMessageEnvelope {
    private MessageType type; // 예: "COLOR_ANALYSIS", "NOTIFICATION"
    private Object payload;

    public KafkaMessageEnvelope(MessageType type, Object payload) {
        this.type = type;
        this.payload = payload;
    }

    public enum MessageType {
        TEMP
        // 향후 추가될 메시지 타입들
    }
}
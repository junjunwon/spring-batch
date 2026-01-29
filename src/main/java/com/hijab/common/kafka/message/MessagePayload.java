package com.hijab.common.kafka.message;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessagePayload {
    private String requestId;

    public MessagePayload(String requestId) {
        this.requestId = requestId;
    }

    public static MessagePayload create(String requestId) {
        return new MessagePayload(requestId);
    }
}
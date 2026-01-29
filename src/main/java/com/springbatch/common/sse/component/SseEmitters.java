package com.springbatch.common.sse.component;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SseEmitters {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void add(String requestId, SseEmitter emitter) {
        emitters.put(requestId, emitter);
    }

    public void remove(String requestId) {
        emitters.remove(requestId);
    }

    public void send(String requestId, String eventName, Object data) {
        SseEmitter emitter = emitters.get(requestId);
        if (emitter == null) {
            return; // emitter가 없으면 아무것도 하지 않음
        }
        try {
            emitter.send(SseEmitter.event()
                    .name(eventName)
                    .data(data));
            emitter.complete();
            emitters.remove(requestId);
        } catch (Exception e) {
            emitter.completeWithError(e);
            emitters.remove(requestId);
        }
    }

    // 🔥 여기 추가
    public SseEmitter get(String requestId) {
        return emitters.get(requestId);
    }

    // 필요하면 전체 목록 조회도 가능
    public Collection<SseEmitter> getAll() {
        return emitters.values();
    }
}

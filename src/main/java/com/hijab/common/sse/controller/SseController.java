package com.hijab.common.sse.controller;

import com.hijab.common.sse.component.SseEmitters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RequestMapping("/api/sse")
@RestController
@RequiredArgsConstructor
public class SseController {

    private final SseEmitters sseEmitters;

    @GetMapping("/subscribe")
    public SseEmitter subscribe(@RequestParam(name = "requestId") String requestId) {
        log.info("📥 SSE 구독 요청: {}", requestId);
        SseEmitter emitter = new SseEmitter(60 * 1000L); // 1분 타임아웃
        sseEmitters.add(requestId, emitter);
        emitter.onTimeout(() -> sseEmitters.remove(requestId));
        return emitter;
    }

    @DeleteMapping("/unsubscribe")
    public void unsubscribe(@RequestParam(name = "requestId") String requestId) {
        sseEmitters.remove(requestId);
    }
}
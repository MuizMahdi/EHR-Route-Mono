package com.project.EhrRoute.Core;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class Node
{
    private SseEmitter emitter;
    private String networkUUID;

    public Node() {}
    public Node(SseEmitter emitter, String networkUUID) {
        this.emitter = emitter;
        this.networkUUID = networkUUID;
    }

    public SseEmitter getEmitter() {
        return emitter;
    }
    public String getNetworkUUID() {
        return networkUUID;
    }
    public void setEmitter(SseEmitter emitter) {
        this.emitter = emitter;
    }
    public void setNetworkUUID(String networkUUID) {
        this.networkUUID = networkUUID;
    }
}

package com.project.EhrRoute.Core;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.Set;

public class Node
{
    private SseEmitter emitter;
    private Set<String> networksUUIDs;

    public Node() {}
    public Node(SseEmitter emitter, Set<String> networksUUIDs) {
        this.emitter = emitter;
        this.networksUUIDs = networksUUIDs;
    }

    public SseEmitter getEmitter() {
        return emitter;
    }
    public Set<String> getNetworksUUIDs() {
        return networksUUIDs;
    }
    public void setEmitter(SseEmitter emitter) {
        this.emitter = emitter;
    }
    public void setNetworkUUID(Set<String> networksUUIDs) {
        this.networksUUIDs = networksUUIDs;
    }
}

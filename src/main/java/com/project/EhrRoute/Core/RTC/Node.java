package com.project.EhrRoute.Core.RTC;
import com.project.EhrRoute.Models.Observer;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * A node represents an online, available client in a network (nodes cluster).
 * A client could have multiple networks, and thus, multiple nodes will be created for such clients.
 */
public class Node implements Observer
{
    private String nodeUUID;
    private SseEmitter emitter;

    public Node() { }
    public Node(String nodeUUID, SseEmitter emitter) {
        this.nodeUUID = nodeUUID;
        this.emitter = emitter;
    }


    @Override
    public void update() { }

    @Override
    public String getUUID() {
        return getNodeUUID();
    }


    public String getNodeUUID() {
        return nodeUUID;
    }
    public SseEmitter getEmitter() {
        return emitter;
    }
    public void setNodeUUID(String nodeUUID) {
        this.nodeUUID = nodeUUID;
    }
    public void setEmitter(SseEmitter emitter) {
        this.emitter = emitter;
    }
}

package com.project.EMRChain.Controllers;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.HashMap;

public class NodeCluster
{
    private HashMap<String, SseEmitter> cluster;

    public NodeCluster()
    {
        this.cluster = new HashMap<>();
    }

    public SseEmitter getNodeEmitter(String uuid) {
        return cluster.get(uuid);
    }

    public void addNode(String uuid, SseEmitter emitter)
    {
        this.cluster.put(uuid, emitter);
    }

    public void removeNode(String uuid)
    {
        this.cluster.remove(uuid);
    }

    public HashMap<String, SseEmitter> getCluster() {
        return cluster;
    }

    public void setCluster(HashMap<String, SseEmitter> cluster) {
        this.cluster = cluster;
    }
}

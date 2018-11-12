package com.project.EMRChain.Core;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.HashMap;

public class NodeCluster
{
    private HashMap<String, Node> cluster;

    public NodeCluster() {
        this.cluster = new HashMap<>();
    }

    public Node getNode(String uuid) {
        return cluster.get(uuid);
    }

    public SseEmitter getNodeEmitter(String uuid) {
        return cluster.get(uuid).getEmitter();
    }

    public void addNode(String uuid, Node node) {
        this.cluster.put(uuid, node);
    }

    public void removeNode(String uuid) {
        this.cluster.remove(uuid);
    }

    public HashMap<String, Node> getCluster() {
        return cluster;
    }

    public void setCluster(HashMap<String, Node> cluster) {
        this.cluster = cluster;
    }
}

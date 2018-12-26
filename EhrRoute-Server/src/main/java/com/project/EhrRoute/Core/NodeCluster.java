package com.project.EhrRoute.Core;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.HashMap;
import java.util.Map;


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
        if (existsInCluster(uuid)) {
            this.cluster.remove(uuid);
        }
    }

    public boolean existsInCluster(String nodeUUID) {
        for (Map.Entry<String, Node> nodeEntry : cluster.entrySet())
        {
            if (nodeEntry.getKey().equals(nodeUUID)) {
                return true;
            }
        }

        return false;
    }

    public HashMap<String, Node> getCluster() {
        return cluster;
    }

    public void setCluster(HashMap<String, Node> cluster) {
        this.cluster = cluster;
    }
}

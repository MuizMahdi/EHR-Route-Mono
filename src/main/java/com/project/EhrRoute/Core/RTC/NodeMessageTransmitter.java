package com.project.EhrRoute.Core.RTC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;

@Component
public class NodeMessageTransmitter
{
    private NodeClustersContainer nodeClustersContainer;

    @Autowired
    public NodeMessageTransmitter(NodeClustersContainer nodeClustersContainer) {
        this.nodeClustersContainer = nodeClustersContainer;
    }

    public void sendMessage(Node node, NodeMessageType messageType, Object data, String id) {

        SseEmitter.SseEventBuilder message = SseEmitter.event().data(data).id(id).name(messageType.toString());

        try {
            node.getEmitter().send(message);
        }
        catch (IOException Ex) {
            nodeClustersContainer.removeNode(node);
        }

    }

    public void sendMessage(Node node, NodeMessageType messageType, Object data) {

        SseEmitter.SseEventBuilder message = SseEmitter.event().data(data).name(messageType.toString());

        try {
            node.getEmitter().send(message);
        }
        catch (IOException Ex) {
            nodeClustersContainer.removeNode(node);
        }

    }
}

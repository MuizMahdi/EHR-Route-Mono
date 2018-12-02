package com.project.EMRChain.Events;
import org.springframework.context.ApplicationEvent;

public class SseKeepAliveEvent extends ApplicationEvent
{
    private String keepAliveData;

    public SseKeepAliveEvent(String keepAliveData) {
        super(keepAliveData);
    }

    public String getKeepAliveData() {
        return keepAliveData;
    }
    public void setKeepAliveData(String keepAliveData) {
        this.keepAliveData = keepAliveData;
    }
}

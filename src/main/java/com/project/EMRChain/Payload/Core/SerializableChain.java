package com.project.EMRChain.Payload.Core;
import java.util.List;

public class SerializableChain
{
    private List<SerializableBlock> chain;

    public SerializableChain() { }
    public SerializableChain(List<SerializableBlock> chain) {
        this.chain = chain;
    }

    public List<SerializableBlock> getChain() {
        return chain;
    }
    public void setChain(List<SerializableBlock> chain) {
        this.chain = chain;
    }
}

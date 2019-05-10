package com.project.EhrRoute.Events;
import com.project.EhrRoute.Payload.Core.SerializableBlock;
import com.project.EhrRoute.Payload.EHR.RecordUpdateData;
import org.springframework.context.ApplicationEvent;


public class GetUserUpdateConsentEvent extends ApplicationEvent
{
    private RecordUpdateData recordUpdateData;
    private SerializableBlock block;
    private String providerUUID;
    private String networkUUID;
    private String userID;


    public GetUserUpdateConsentEvent(Object source, RecordUpdateData recordUpdateData, SerializableBlock block, String providerUUID, String networkUUID,String userID) {
        super(source);
        this.recordUpdateData = recordUpdateData;
        this.providerUUID = providerUUID;
        this.networkUUID = networkUUID;
        this.userID = userID;
        this.block = block;
    }


    public String getUserID() {
        return userID;
    }
    public SerializableBlock getBlock() {
        return block;
    }
    public String getNetworkUUID() {
        return networkUUID;
    }
    public String getProviderUUID() {
        return providerUUID;
    }
    public RecordUpdateData getRecordUpdateData() {
        return recordUpdateData;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    public void setBlock(SerializableBlock block) {
        this.block = block;
    }
    public void setNetworkUUID(String networkUUID) {
        this.networkUUID = networkUUID;
    }
    public void setProviderUUID(String providerUUID) {
        this.providerUUID = providerUUID;
    }
    public void setRecordUpdateData(RecordUpdateData recordUpdateData) {
        this.recordUpdateData = recordUpdateData;
    }

}

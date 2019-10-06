package com.project.EhrRoute.Payload.App;


public class NotificationResponse
{
    private long notificationID;
    private String senderAddress;
    private String recipientAddress;
    private String notificationType;
    private Object reference;


    public NotificationResponse() { }
    public NotificationResponse(long notificationID, String senderAddress, String recipientAddress, String notificationType, Object reference) {
        this.notificationID = notificationID;
        this.senderAddress = senderAddress;
        this.recipientAddress = recipientAddress;
        this.notificationType = notificationType;
        this.reference = reference;
    }


    public Object getReference() {
        return reference;
    }
    public String getSenderAddress() {
        return senderAddress;
    }
    public long getNotificationID() {
        return notificationID;
    }
    public String getRecipientAddress() {
        return recipientAddress;
    }
    public String getNotificationType() {
        return notificationType;
    }
    public void setReference(Object reference) {
        this.reference = reference;
    }
    public void setSenderAddress(String senderName) {
        this.senderAddress = senderAddress;
    }
    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }
    public void setNotificationID(long notificationID) {
        this.notificationID = notificationID;
    }
    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }
}

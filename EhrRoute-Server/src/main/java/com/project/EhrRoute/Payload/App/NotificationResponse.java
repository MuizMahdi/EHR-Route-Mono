package com.project.EhrRoute.Payload.App;


public class NotificationResponse
{
    private String senderName;
    private String recipientName;
    private String notificationType;
    private Object reference;


    public NotificationResponse() { }
    public NotificationResponse(String senderName, String recipientName, String notificationType, Object reference) {
        this.senderName = senderName;
        this.recipientName = recipientName;
        this.notificationType = notificationType;
        this.reference = reference;
    }


    public Object getReference() {
        return reference;
    }
    public String getSenderName() {
        return senderName;
    }
    public String getRecipientName() {
        return recipientName;
    }
    public String getNotificationType() {
        return notificationType;
    }
    public void setReference(Object reference) {
        this.reference = reference;
    }
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }
    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }
}

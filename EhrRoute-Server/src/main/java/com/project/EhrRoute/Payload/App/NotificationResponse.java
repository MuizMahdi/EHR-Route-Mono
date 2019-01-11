package com.project.EhrRoute.Payload.App;


public class NotificationResponse
{
    private long notificationID;
    private String senderName;
    private String recipientName;
    private String notificationType;
    private Object reference;


    public NotificationResponse() { }
    public NotificationResponse(long notificationID, String senderName, String recipientName, String notificationType, Object reference) {
        this.notificationID = notificationID;
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
    public long getNotificationID() {
        return notificationID;
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
    public void setNotificationID(long notificationID) {
        this.notificationID = notificationID;
    }
    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }
}

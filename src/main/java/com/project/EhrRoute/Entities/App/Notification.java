package com.project.EhrRoute.Entities.App;
import com.project.EhrRoute.Audits.DateAudit;
import com.project.EhrRoute.Entities.Auth.User;
import com.project.EhrRoute.Entities.Core.ConsentRequestBlock;
import com.project.EhrRoute.Entities.Core.UpdateConsentRequest;
import com.project.EhrRoute.Models.NotificationType;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Entity
public class Notification extends DateAudit
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    //@Column(columnDefinition = "tinyint(1) default 0") // Set to false as default value
    private boolean isRead;

    @OneToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @OneToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id")
    private User recipient;

    @NotNull
    private NotificationType type;

    @Any(metaColumn = @Column(name = "notification_type_reference"))
    @AnyMetaDef(name= "NotificationReferenceMetaDef", metaType = "string", idType = "long",
        metaValues = {
            @MetaValue(value = "InvitationRequest", targetEntity = NetworkInvitationRequest.class),
            @MetaValue(value = "ConsentRequest", targetEntity = ConsentRequestBlock.class),
            @MetaValue(value = "UpdateConsentRequest", targetEntity = UpdateConsentRequest.class)
        }
    )
    @JoinColumn(name = "reference_id")
    private Object reference;


    public Notification() { }
    public Notification(@NotNull boolean isRead, User sender, User recipient, @NotNull NotificationType type, Object reference) {
        this.isRead = isRead;
        this.isRead = false;
        this.sender = sender;
        this.recipient = recipient;
        this.type = type;
        this.reference = reference;
    }


    public Long getId() {
        return id;
    }
    public boolean isRead() {
        return isRead;
    }
    public User getSender() {
        return sender;
    }
    public User getRecipient() {
        return recipient;
    }
    public NotificationType getType() {
        return type;
    }
    public Object getReference() {
        return reference;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setRead(boolean read) {
        isRead = read;
    }
    public void setSender(User sender) {
        this.sender = sender;
    }
    public void setType(NotificationType type) {
        this.type = type;
    }
    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }
    public void setReference(Object reference) {
        this.reference = reference;
    }
}

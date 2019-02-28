package com.project.EhrRoute.Entities.Core;
import com.project.EhrRoute.Entities.EHR.EhrDetails;
import javax.persistence.*;


@Entity
@Table(name = "UpdateConsentRequestBlock")
public class UpdateConsentRequest
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(targetEntity = ConsentRequestBlock.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "consent_request_id", nullable = false)
    private ConsentRequestBlock consentRequestBlock;

    @OneToOne(targetEntity = EhrDetails.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "ehr_details_id")
    private EhrDetails ehrDetails;


    public UpdateConsentRequest() { }
    public UpdateConsentRequest(ConsentRequestBlock consentRequestBlock, EhrDetails ehrDetails) {
        this.consentRequestBlock = consentRequestBlock;
        this.ehrDetails = ehrDetails;
    }


    public Long getId() {
        return id;
    }
    public EhrDetails getEhrDetails() {
        return ehrDetails;
    }
    public ConsentRequestBlock getConsentRequestBlock() {
        return consentRequestBlock;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setConsentRequestBlock(ConsentRequestBlock consentRequestBlock) {
        this.consentRequestBlock = consentRequestBlock;
    }
    public void setEhrDetails(EhrDetails ehrDetails) {
        this.ehrDetails = ehrDetails;
    }
}

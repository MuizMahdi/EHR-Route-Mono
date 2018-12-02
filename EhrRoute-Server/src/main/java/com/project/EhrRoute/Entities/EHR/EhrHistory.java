package com.project.EMRChain.Entities.EHR;
import com.project.EMRChain.Entities.Core.ConsentRequestBlock;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "consent_ehr_history")
public class EhrHistory
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull @NotBlank private String ehrCondition;
    @NotNull @NotBlank private boolean ehrOccurrence;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "consent_request_id", nullable = false)
    private ConsentRequestBlock consentRequestBlock;


    public EhrHistory() { }
    public EhrHistory(@NotNull @NotBlank String ehrCondition, @NotNull @NotBlank boolean ehrOccurrence) {
        this.ehrCondition = ehrCondition;
        this.ehrOccurrence = ehrOccurrence;
    }


    public Long getId() {
        return id;
    }
    public String getCondition() {
        return ehrCondition;
    }
    public boolean isOccurrence() {
        return ehrOccurrence;
    }
    public ConsentRequestBlock getConsentRequestBlock() {
        return consentRequestBlock;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setCondition(String condition) {
        this.ehrCondition = condition;
    }
    public void setOccurrence(boolean occurrence) {
        this.ehrOccurrence = occurrence;
    }
    public void setConsentRequestBlock(ConsentRequestBlock consentRequestBlock) {
        this.consentRequestBlock = consentRequestBlock;
    }
}

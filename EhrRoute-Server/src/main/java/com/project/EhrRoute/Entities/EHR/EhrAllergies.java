package com.project.EhrRoute.Entities.EHR;
import com.project.EhrRoute.Entities.Core.ConsentRequestBlock;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "consent_ehr_allergies")
public class EhrAllergies
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull @NotBlank private String allergy;

    @ManyToOne(fetch=FetchType.LAZY, optional = false)
    @JoinColumn(name = "consent_request_id", nullable = false)
    private ConsentRequestBlock consentRequestBlock;


    public EhrAllergies() { }
    public EhrAllergies(String allergy) {
        this.allergy = allergy;
    }


    public Long getId() {
        return id;
    }
    public String getAllergy() {
        return allergy;
    }
    public ConsentRequestBlock getConsentRequestBlock() {
        return consentRequestBlock;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }
    public void setConsentRequestBlock(ConsentRequestBlock consentRequestBlock) {
        this.consentRequestBlock = consentRequestBlock;
    }
}
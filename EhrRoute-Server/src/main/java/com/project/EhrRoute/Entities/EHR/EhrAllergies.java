package com.project.EhrRoute.Entities.EHR;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "ehr_allergies")
public class EhrAllergies
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull @NotBlank private String allergy;

    @ManyToOne(fetch=FetchType.LAZY, optional = false)
    @JoinColumn(name = "ehr_details_id", nullable = false)
    private EhrDetails ehrDetails;


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
    public EhrDetails getEhrDetails() {
        return ehrDetails;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }
    public void setEhrDetails(EhrDetails ehrDetails) {
        this.ehrDetails = ehrDetails;
    }
}
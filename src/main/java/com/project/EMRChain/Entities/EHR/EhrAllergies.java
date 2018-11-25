package com.project.EMRChain.Entities.EHR;
import javax.persistence.*;

@Entity
@Table(name = "consent_ehr_allergies")
public class EhrAllergies
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String allergy;

    public EhrAllergies() { }
    public EhrAllergies(String allergy) {
        this.allergy = allergy;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getAllergy() {
        return allergy;
    }
    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }
}

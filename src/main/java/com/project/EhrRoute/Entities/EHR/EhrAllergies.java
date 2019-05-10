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

    public void setId(Long id) {
        this.id = id;
    }
    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }
}
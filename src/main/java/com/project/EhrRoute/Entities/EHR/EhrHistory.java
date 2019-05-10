package com.project.EhrRoute.Entities.EHR;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ehr_history")
public class EhrHistory
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull @NotBlank private String ehrCondition;
    @NotNull @NotBlank private boolean ehrOccurrence;

    @ManyToOne(fetch=FetchType.LAZY, optional = false)
    @JoinColumn(name = "ehr_details_id", nullable = false)
    private EhrDetails ehrDetails;


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
    public EhrDetails getEhrDetails() {
        return ehrDetails;
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
    public void setEhrDetails(EhrDetails ehrDetails) {
        this.ehrDetails = ehrDetails;
    }
}

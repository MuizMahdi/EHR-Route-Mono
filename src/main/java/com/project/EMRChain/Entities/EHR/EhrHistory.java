package com.project.EMRChain.Entities.EHR;
import javax.persistence.*;

@Entity
@Table(name = "consent_ehr_history")
public class EhrHistory
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String condition;

    private boolean occurrence;

    public EhrHistory() { }
    public EhrHistory(String condition, boolean occurrence) {
        this.condition = condition;
        this.occurrence = occurrence;
    }

    public Long getId() {
        return id;
    }
    public String getCondition() {
        return condition;
    }
    public boolean isOccurrence() {
        return occurrence;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setCondition(String condition) {
        this.condition = condition;
    }
    public void setOccurrence(boolean occurrence) {
        this.occurrence = occurrence;
    }
}

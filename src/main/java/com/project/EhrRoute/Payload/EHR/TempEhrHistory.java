package com.project.EhrRoute.Payload.EHR;


public class TempEhrHistory
{
    private String condition;
    private Boolean occurrence;

    public TempEhrHistory() { }
    public TempEhrHistory(String condition, Boolean occurrence) {
        this.condition = condition;
        this.occurrence = occurrence;
    }

    public String getCondition() {
        return condition;
    }
    public void setCondition(String condition) {
        this.condition = condition;
    }
    public Boolean getOccurrence() {
        return occurrence;
    }
    public void setOccurrence(Boolean occurrence) {
        this.occurrence = occurrence;
    }
}

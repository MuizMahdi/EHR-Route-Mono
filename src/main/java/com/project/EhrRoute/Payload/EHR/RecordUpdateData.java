package com.project.EhrRoute.Payload.EHR;
import java.util.List;
import java.util.Map;


public class RecordUpdateData
{
    List<String> conditions;
    List<String> allergies;
    List<TempEhrHistory> history;


    public RecordUpdateData() { }
    public RecordUpdateData(List<String> conditions, List<String> allergies, List<TempEhrHistory> history) {
        this.conditions = conditions;
        this.allergies = allergies;
        this.history = history;
    }


    public List<String> getAllergies() {
        return allergies;
    }
    public List<String> getConditions() {
        return conditions;
    }
    public List<TempEhrHistory> getHistory() {
        return history;
    }
    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }
    public void setHistory(List<TempEhrHistory> history) {
        this.history = history;
    }
    public void setConditions(List<String> conditions) {
        this.conditions = conditions;
    }
}

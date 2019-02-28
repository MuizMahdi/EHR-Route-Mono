package com.project.EhrRoute.Payload.EHR;
import java.util.List;
import java.util.Map;


public class RecordUpdateData
{
    List<String> conditions;
    List<String> allergies;
    Map<String, Boolean> history;


    public RecordUpdateData() { }
    public RecordUpdateData(List<String> conditions, List<String> allergies, Map<String, Boolean> history) {
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
    public Map<String, Boolean> getHistory() {
        return history;
    }
    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }
    public void setHistory(Map<String, Boolean> history) {
        this.history = history;
    }
    public void setConditions(List<String> conditions) {
        this.conditions = conditions;
    }
}

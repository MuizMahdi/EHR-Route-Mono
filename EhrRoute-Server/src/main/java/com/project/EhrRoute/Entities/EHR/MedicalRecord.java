package com.project.EMRChain.Entities.EHR;
import java.util.List;
import java.util.Map;

public class MedicalRecord
{
    private PatientInfo patientInfo;
    private List<String> problems;
    private List<String> allergiesAndReactions;
    private Map<String, Boolean> history;

    public MedicalRecord() { }
    public MedicalRecord(PatientInfo patientInfo, List<String> problems, List<String> allergiesAndReactions, Map<String, Boolean> history) {
        this.patientInfo = patientInfo;
        this.problems = problems;
        this.allergiesAndReactions = allergiesAndReactions;
        this.history = history;
    }


    public PatientInfo getPatientInfo() {
        return patientInfo;
    }
    public void setPatientInfo(PatientInfo patientInfo) {
        this.patientInfo = patientInfo;
    }
    public List<String> getProblems() {
        return problems;
    }
    public void setProblems(List<String> problems) {
        this.problems = problems;
    }
    public List<String> getAllergiesAndReactions() {
        return allergiesAndReactions;
    }
    public void setAllergiesAndReactions(List<String> allergiesAndReactions) {
        this.allergiesAndReactions = allergiesAndReactions;
    }
    public Map<String, Boolean> getHistory() {
        return history;
    }
    public void setHistory(Map<String, Boolean> history) {
        this.history = history;
    }
}

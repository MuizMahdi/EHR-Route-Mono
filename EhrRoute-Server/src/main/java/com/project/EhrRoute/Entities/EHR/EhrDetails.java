package com.project.EhrRoute.Entities.EHR;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;


@Entity
public class EhrDetails
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String address;


    @OneToMany(mappedBy = "ehrDetails", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
    private Set<EhrProblems> problems = new HashSet<>();

    @OneToMany(mappedBy = "ehrDetails", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
    private Set<EhrAllergies> allergies = new HashSet<>();

    @OneToMany(mappedBy = "ehrDetails", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
    private Set<EhrHistory> history = new HashSet<>();


    public EhrDetails() { }
    public EhrDetails(@NotBlank String address) {
        this.address = address;
    }
    public EhrDetails(@NotBlank String address, Set<EhrProblems> problems, Set<EhrAllergies> allergies, Set<EhrHistory> history) {
        this.address = address;
        this.problems = problems;
        this.allergies = allergies;
        this.history = history;
    }


    public void addProblem(EhrProblems problem) {
        problems.add(problem);
    }
    public void removeProblem(EhrProblems problem) {
        problems.remove(problem);
    }

    public void addAllergy(EhrAllergies allergy) {
        allergies.add(allergy);
    }
    public void removeAllergy(EhrAllergies allergy) {
        allergies.remove(allergy);
    }

    public void addHistory(EhrHistory historicalCondition) {
        history.add(historicalCondition);
    }
    public void removeHistory(EhrHistory historicalCondition) {
        history.remove(historicalCondition);
    }


    public Set<EhrHistory> getHistory() {
        return history;
    }
    public Set<EhrProblems> getProblems() {
        return problems;
    }
    public Set<EhrAllergies> getAllergies() {
        return allergies;
    }

    public void setHistory(Set<EhrHistory> history) {
        this.history = history;
    }
    public void setProblems(Set<EhrProblems> problems) {
        this.problems = problems;
    }
    public void setAllergies(Set<EhrAllergies> allergies) {
        this.allergies = allergies;
    }
}

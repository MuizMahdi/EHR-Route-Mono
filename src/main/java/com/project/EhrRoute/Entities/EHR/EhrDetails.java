package com.project.EhrRoute.Entities.EHR;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;


@Entity
public class EhrDetails
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String address;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "ehrDetail_problems",
        joinColumns = @JoinColumn(name = "ehr_details_id"),
        inverseJoinColumns = @JoinColumn(name = "ehr_problem_id")
    )
    private Set<EhrProblems> problems = new HashSet<>();


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "ehrDetail_allergies",
            joinColumns = @JoinColumn(name = "ehr_details_id"),
            inverseJoinColumns = @JoinColumn(name = "ehr_allergy_id")
    )
    private Set<EhrAllergies> allergies = new HashSet<>();


    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "ehrDetail_history",
        joinColumns = @JoinColumn(name = "ehr_details_id"),
        inverseJoinColumns = @JoinColumn(name = "ehr_history_id")
    )
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


    public Long getId() {
        return id;
    }
    public String getAddress() {
        return address;
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

    public void setId(Long id) {
        this.id = id;
    }
    public void setAddress(String address) {
        this.address = address;
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

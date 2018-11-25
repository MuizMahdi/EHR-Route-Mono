package com.project.EMRChain.Entities.EHR;
import javax.persistence.*;

@Entity
@Table(name = "consent_ehr_problems")
public class EhrProblems
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String problem;

    public EhrProblems() { }
    public EhrProblems(String problem) {
        this.problem = problem;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getProblem() {
        return problem;
    }
    public void setProblem(String problem) {
        this.problem = problem;
    }
}

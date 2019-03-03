package com.project.EhrRoute.Entities.EHR;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "ehr_problems")
public class EhrProblems
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull @NotBlank private String problem;

    public EhrProblems() { }
    public EhrProblems(String problem) {
        this.problem = problem;
    }


    public Long getId() {
        return id;
    }
    public String getProblem() {
        return problem;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setProblem(String problem) {
        this.problem = problem;
    }
}

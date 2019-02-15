package com.project.EhrRoute.Entities.EHR;
import com.project.EhrRoute.Entities.Core.ConsentRequestBlock;
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

    @ManyToOne(fetch=FetchType.LAZY, optional = false)
    @JoinColumn(name = "ehr_details_id", nullable = false)
    private EhrDetails ehrDetails;


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
    public EhrDetails getEhrDetails() {
        return ehrDetails;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setProblem(String problem) {
        this.problem = problem;
    }
    public void setEhrDetails(EhrDetails ehrDetails) {
        this.ehrDetails = ehrDetails;
    }
}

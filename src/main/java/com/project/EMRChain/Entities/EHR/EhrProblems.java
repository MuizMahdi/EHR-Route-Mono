package com.project.EMRChain.Entities.EHR;
import com.project.EMRChain.Entities.Core.ConsentRequestBlock;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "consent_ehr_problems")
public class EhrProblems
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull @NotBlank private String problem;

    @ManyToOne(fetch=FetchType.LAZY, optional = false)
    @JoinColumn(name = "consent_request_id", nullable = false)
    private ConsentRequestBlock consentRequestBlock;


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
    public ConsentRequestBlock getConsentRequestBlock() {
        return consentRequestBlock;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setProblem(String problem) {
        this.problem = problem;
    }
    public void setConsentRequestBlock(ConsentRequestBlock consentRequestBlock) {
        this.consentRequestBlock = consentRequestBlock;
    }
}

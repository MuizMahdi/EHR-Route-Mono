package com.project.EMRChain.Controllers;

import com.project.EMRChain.Services.ClustersContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionController
{
    private ClustersContainer clustersContainer;

    @Autowired
    public TransactionController(ClustersContainer clustersContainer) {
        this.clustersContainer = clustersContainer;
    }


}

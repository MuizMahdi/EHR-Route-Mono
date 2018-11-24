package com.project.EMRChain;
import com.project.EMRChain.Entities.Core.ChainRoot;
import com.project.EMRChain.Entities.Core.Network;
import com.project.EMRChain.Repositories.NetworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class EmrChainApplication
{
    @Autowired
    private NetworkRepository networkRepository;

    @PostConstruct
    void onInit() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        /*
        String netUUID = "284ee3d3-236c-46e1-919a-608ad9f34d80";
        String root = "C42F6F09B6CAF73E29EB10320D239EB825A522DF1ED2C5FD19AA4D262062D885";

        Network network = new Network();
        network.setNetworkUUID(netUUID);

        ChainRoot chainRoot = new ChainRoot(root);
        network.setChainRoot(chainRoot);

        networkRepository.save(network);
        */
    }


    public static void main(String[] args) {
        SpringApplication.run(EmrChainApplication.class, args);
    }

}
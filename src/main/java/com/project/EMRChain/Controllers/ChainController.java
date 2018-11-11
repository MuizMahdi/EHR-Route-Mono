package com.project.EMRChain.Controllers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@RestController
@RequestMapping("/chain")
public class ChainController
{
    private ExecutorService executorService = Executors.newCachedThreadPool();

    @GetMapping("/chaingiver/{uuid}")
    public SseEmitter chainGiver(@PathVariable("uuid") String UUID)
    {
        // Todo: Add the client uuid to ChainGivers Cluster

        // Returns notification SSE
        return null;
    }

    @GetMapping("/chaingetter/{uuid}")
    public SseEmitter chainGetter(@PathVariable("uuid") String UUID)
    {
        // Todo: Add the client uuid to ChainGetters Cluster

        // Returns notification SSE
        return null;
    }

    @PostMapping("/chaingive")
    public ResponseEntity chainGive()
    {
        // Returns HttpStatus.OK on success
        return null;
    }

    @GetMapping("/chainget")
    public ResponseEntity ChainGet()
    {
        // Returns HttpStatus.OK on success
        return null;
    }

    @GetMapping("/chainupdate")
    public SseEmitter chainUpdate()
    {
        // Returns chain
        return null;
    }


    // Called when client closes app or onDestroy
    @GetMapping("/close/{uuid}")
    public ResponseEntity closeConnection(@PathVariable("uuid") String UUID)
    {
        // Todo: remove the client from clusters
        // Returns chain
        return null;
    }

}

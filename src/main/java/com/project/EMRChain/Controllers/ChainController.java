package com.project.EMRChain.Controllers;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@RestController
@RequestMapping("/chain")
public class ChainController
{
    private ExecutorService executorService = Executors.newCachedThreadPool();

    

    @GetMapping("/chaingivers")
    public SseEmitter chainGiver()
    {
        // Returns notification SSE
        return null;
    }

    @GetMapping("/chaingetters")
    public SseEmitter chainGetter()
    {
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



    @GetMapping("/stream")
    public SseEmitter stream()
    {
        SseEmitter emitter = new SseEmitter();

        executorService.execute(() -> {
            try
            {
                emitter.send(new Date().getTime(), MediaType.APPLICATION_JSON);
                emitter.complete();
            }
            catch (Exception Ex)
            {
                emitter.completeWithError(Ex);
            }
        });

        return emitter;
    }

}

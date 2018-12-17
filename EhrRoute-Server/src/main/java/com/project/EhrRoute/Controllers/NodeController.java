package com.project.EhrRoute.Controllers;
import com.project.EhrRoute.Core.GenesisBlock;
import com.project.EhrRoute.Payload.Auth.ApiResponse;
import com.project.EhrRoute.Utilities.JsonUtil;
import com.project.EhrRoute.Utilities.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/node")
public class NodeController
{

    @GetMapping("/validate/{nodeuuid}/{netuuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity validateNode(@PathVariable("nodeuuid") String nodeUUID, @PathVariable("netuuid") String networkUUID)
    {
        // Check if UUIDs are empty
        if (nodeUUID.isEmpty() || networkUUID.isEmpty())
        {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Empty Node or Network UUID"),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Check if UUIDs are valid UUIDs
        try
        {
            UUID node_UUID = UUID.fromString(nodeUUID);
            UUID network_UUID = UUID.fromString(networkUUID);
        }
        catch (IllegalArgumentException Ex) {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Invalid Node or Network UUID"),
                    HttpStatus.BAD_REQUEST
            );
        }

        return new ResponseEntity<>(
                new ApiResponse(true, "Valid Provider"),
                HttpStatus.OK
        );
    }
}

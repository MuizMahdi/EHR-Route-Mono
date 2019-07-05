package com.project.EhrRoute.Controllers;
import com.project.EhrRoute.Payload.Auth.ApiResponse;
import com.project.EhrRoute.Payload.Core.BlockFetchResponse;
import com.project.EhrRoute.Security.CurrentUser;
import com.project.EhrRoute.Security.UserPrincipal;
import com.project.EhrRoute.Services.ChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/chain")
@PreAuthorize("hasRole('PROVIDER')")
public class ChainController
{
    private ChainService chainService;

    @Autowired
    public ChainController(ChainService chainService) {
        this.chainService = chainService;
    }


    /**
     * Fetches a range of blocks from the blockchain of another node in the specified network
     * @param consumerUUID      Node UUID of the node that wants to receive (consume) blocks
     * @param networkUUID       Network UUID from which another node is asked for sending blocks
     * @param rangeBegin        The index of the first block to fetch
     * @param rangeEnd          The index of the last block to fetch
     * @return HTTP 200         Returns HTTP OK then the node receives the blocks through SSE
     */
    @GetMapping()
    public ResponseEntity fetchChainBlocks(@RequestParam("consumer-uuid") String consumerUUID, @RequestParam("network-uuid") String networkUUID, @RequestParam("range-begin") Integer rangeBegin, @RequestParam("range-end") Integer rangeEnd, @CurrentUser UserPrincipal currentUser) {
        chainService.requestBlocksFetch(currentUser, consumerUUID, networkUUID, rangeBegin, rangeEnd);
        return ResponseEntity.ok(new ApiResponse(true, "Blocks has been requested"));
    }


    /**
     * Sends a block sent by a provider node to a consumer node in response to a blocks fetch request
     * @param blockFetchResponse    The provider response containing the block and the fetch request info
     * @return HTTP 202             Returns HTTP ACCEPTED when block gets successfully sent to the consumer
     */
    @PostMapping()
    public ResponseEntity sendChainBlock(@RequestBody BlockFetchResponse blockFetchResponse) {
        chainService.sendFetchedBlock(blockFetchResponse);
        return ResponseEntity.ok(new ApiResponse(true, "Block has been sent"));
    }
}

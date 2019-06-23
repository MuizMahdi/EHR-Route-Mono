package com.project.EhrRoute.Services;
import com.project.EhrRoute.Entities.Core.BlocksFetchRequest;
import com.project.EhrRoute.Events.BlocksFetchRequestsCheckEvent;
import com.project.EhrRoute.Exceptions.BadRequestException;
import com.project.EhrRoute.Exceptions.ResourceConflictException;
import com.project.EhrRoute.Models.UuidSourceType;
import com.project.EhrRoute.Repositories.BlocksFetchRequestRepository;
import com.project.EhrRoute.Utilities.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;


@Service
public class BlocksFetchRequestService
{
    private BlocksFetchRequestRepository blocksFetchRequestRepository;
    private UuidUtil uuidUtil;


    @Autowired
    public BlocksFetchRequestService(BlocksFetchRequestRepository blocksFetchRequestRepository, UuidUtil uuidUtil) {
        this.blocksFetchRequestRepository = blocksFetchRequestRepository;
        this.uuidUtil = uuidUtil;
    }


    public void saveBlocksFetchRequest(String consumerUUID, String networkUUID, long rangeBegin, long rangeEnd) {
        // Validate ranges
        if (rangeBegin < 0 || rangeEnd < 0) throw new BadRequestException("Invalid negative blocks range");

        // Validate UUIDs
        uuidUtil.validateResourceUUID(consumerUUID, UuidSourceType.NODE);
        uuidUtil.validateResourceUUID(networkUUID, UuidSourceType.NETWORK);

        // Check if consumer already has a pending request for the network
        if (blocksFetchRequestExists(consumerUUID, networkUUID)) throw new ResourceConflictException("Another request by the consumer for this network is pending");

        blocksFetchRequestRepository.save(
            new BlocksFetchRequest(consumerUUID, networkUUID, rangeBegin, rangeEnd)
        );
    }

    private boolean blocksFetchRequestExists(String consumerUUID, String networkUUID) {
        return blocksFetchRequestRepository.exists(consumerUUID, networkUUID).isPresent();
    }

    @EventListener
    private void sendBlocksFetchRequest(BlocksFetchRequestsCheckEvent event) {

    }
}

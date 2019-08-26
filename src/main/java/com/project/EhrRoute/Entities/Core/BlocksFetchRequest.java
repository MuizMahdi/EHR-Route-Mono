package com.project.EhrRoute.Entities.Core;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

/**
 * Stores blocks fetch requests details, which gets checked regularly on server-side to trigger blocks fetching
 */

@Entity
@Table(name="BlocksFetchRequest")
public class BlocksFetchRequest
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String consumerUUID;

    @NotNull
    @NotBlank
    private String networkUUID;

    @NotNull
    @PositiveOrZero
    private Long blocksRangeBegin;

    @NotNull
    @PositiveOrZero
    private Long blocksRangeEnd;

    // Indicates whether the blocks fetching is currently in process (false) or not (true)
    private Boolean isPending;


    public BlocksFetchRequest() { }
    public BlocksFetchRequest(@NotNull @NotBlank String consumerUUID, @NotNull @NotBlank String networkUUID, @NotNull @PositiveOrZero Long blocksRangeBegin, @NotNull @PositiveOrZero Long blocksRangeEnd) {
        this.consumerUUID = consumerUUID;
        this.networkUUID = networkUUID;
        this.blocksRangeBegin = blocksRangeBegin;
        this.blocksRangeEnd = blocksRangeEnd;
        this.isPending = true; // Waiting for a node to send blocks
    }


    public Long getId() {
        return id;
    }
    public Boolean getPending() {
        return isPending;
    }
    public String getNetworkUUID() {
        return networkUUID;
    }
    public String getConsumerUUID() {
        return consumerUUID;
    }
    public Long getBlocksRangeEnd() {
        return blocksRangeEnd;
    }
    public Long getBlocksRangeBegin() {
        return blocksRangeBegin;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setPending(Boolean pending) {
        isPending = pending;
    }
    public void setNetworkUUID(String networkUUID) {
        this.networkUUID = networkUUID;
    }
    public void setConsumerUUID(String consumerUUID) {
        this.consumerUUID = consumerUUID;
    }
    public void setBlocksRangeEnd(Long blocksRangeEnd) {
        this.blocksRangeEnd = blocksRangeEnd;
    }
    public void setBlocksRangeBegin(Long blocksRangeBegin) {
        this.blocksRangeBegin = blocksRangeBegin;
    }
}

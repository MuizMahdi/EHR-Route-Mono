package com.project.EhrRoute.Payload.Core;
import com.project.EhrRoute.Payload.EHR.RecordUpdateData;


public class UpdatedBlockAddition
{
    private BlockAddition blockAddition;
    private RecordUpdateData recordUpdateData;


    public UpdatedBlockAddition() { }
    public UpdatedBlockAddition(BlockAddition blockAddition, RecordUpdateData recordUpdateData) {
        this.blockAddition = blockAddition;
        this.recordUpdateData = recordUpdateData;
    }


    public BlockAddition getBlockAddition() {
        return blockAddition;
    }
    public RecordUpdateData getRecordUpdateData() {
        return recordUpdateData;
    }
    public void setBlockAddition(BlockAddition blockAddition) {
        this.blockAddition = blockAddition;
    }
    public void setRecordUpdateData(RecordUpdateData recordUpdateData) {
        this.recordUpdateData = recordUpdateData;
    }
}

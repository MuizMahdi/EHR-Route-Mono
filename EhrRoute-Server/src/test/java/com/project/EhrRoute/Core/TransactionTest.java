package com.project.EMRChain.Core;
import com.project.EhrRoute.Core.Address;
import com.project.EhrRoute.Core.Transaction;
import com.project.EhrRoute.Core.Utilities.StringUtil;
import com.project.EhrRoute.Entities.EHR.MedicalRecord;
import com.project.EhrRoute.Utilities.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.security.PublicKey;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionTest
{
    @Mock
    private StringUtil stringUtil;

    @Mock
    private JsonUtil jsonUtil;

    @InjectMocks
    private Transaction transaction = new Transaction();


    @Test
    public void testGetTransactionDataReturnsData()
    {
        PublicKey publicKey = mock(PublicKey.class);
        MedicalRecord medicalRecord = mock(MedicalRecord.class);

        Address senderAddress = mock(Address.class);
        Address recipientAddress = mock(Address.class);
        transaction.setSenderAddress(senderAddress);
        transaction.setRecipientAddress(recipientAddress);
        transaction.setSenderPubKey(publicKey);
        transaction.setRecord(medicalRecord);

        when(jsonUtil.createJson(medicalRecord)).thenReturn("RecordJson");
        when(stringUtil.getStringFromKey(publicKey)).thenReturn("KeyString");

        transaction.getTransactionData();

        assertNotNull(transaction.getTransactionData());
    }
}

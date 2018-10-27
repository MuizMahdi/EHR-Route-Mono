package com.project.EMRChain;
import com.project.EMRChain.Core.Transaction;
import com.project.EMRChain.Core.Utilities.*;
import com.project.EMRChain.EHR.MedicalRecord;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

public class TEST
{
    public static void main(String args[]) throws Exception
    {
        //test01();
        test02();
    }

    public static void test01() throws Exception
    {
        KeyPair senderKeyPair = EcdsaUtil.ecGenerateKeyPair();
        KeyPair recipientKeyPair = EcdsaUtil.ecGenerateKeyPair();

        // Keys for transaction
        PublicKey senderPublicKey = senderKeyPair.getPublic();
        PrivateKey senderPrivateKey = senderKeyPair.getPrivate();
        PublicKey recipientPublicKey = recipientKeyPair.getPublic();

        // Public Keys in bytes
        byte[] senderPubKeyBytes = StringUtil.getStringFromKey(senderPublicKey).getBytes();
        byte[] recipientPubKeyBytes = StringUtil.getStringFromKey(recipientPublicKey).getBytes();

        // Base58 encoded Public Keys
        String senderBase58Key = Base58.encode(senderPubKeyBytes);
        String recipientBase58Key = Base58.encode(recipientPubKeyBytes);

        // Public Keys from Base58 Keys = Addresses
        PublicKey senderAddress = ECKeyUtil.getPublicKeyFromString(senderBase58Key);
        PublicKey recipientAddress = ECKeyUtil.getPublicKeyFromString(recipientBase58Key);


        // Decoded Addresses
        byte[] senderDecodedAddress = Base58.decode(StringUtil.getStringFromKey(senderAddress));
        byte[] recipientDecodedAddress = Base58.decode(StringUtil.getStringFromKey(recipientAddress));
        PublicKey senderDecodedPubKey = ECKeyUtil.getPublicKeyFromString(StringUtil.getStringFromBytes(senderDecodedAddress));
        PublicKey recipientDecodedPubKey = ECKeyUtil.getPublicKeyFromString(StringUtil.getStringFromBytes(recipientDecodedAddress));


        // Transaction sender and recipient addresses assignment
        Transaction transaction = new Transaction();
        transaction.setSender(senderAddress);
        transaction.setRecipient(recipientAddress);
        transaction.setRecord(new MedicalRecord());

        // Transaction signature generation
        byte[] signature = EcdsaUtil.ecSign(senderPrivateKey, transaction);
        System.out.println("Signature Generated: " + StringUtil.getStringFromBytes(signature));
        transaction.setSignature(signature);

        System.out.println(" ");
        System.out.println(" ");

        System.out.println(" -------------------------------------------------------- ");
        System.out.println(" All test below should return >> TRUE <<");
        System.out.println(" -------------------------------------------------------- ");

        System.out.println(" ");

        System.out.println("Tx Verification using original PublicKey:");
        System.out.println(EcdsaUtil.ecVerifyTransactionSignature(senderPublicKey, transaction));

        System.out.println(" ");

        System.out.println("Tx Verification using Address(Base58 PublicKey):");
        System.out.println(EcdsaUtil.ecVerifyTransactionSignature(senderAddress, transaction));

        System.out.println(" ");

        System.out.println("Tx Verification using Decoded Address:");
        System.out.println(EcdsaUtil.ecVerifyTransactionSignature(senderDecodedPubKey, transaction));

        System.out.println(" ");
        System.out.println(" ");

        System.out.println(" -------------------------------------------------------- ");
        System.out.println(" All test below should return >> FALSE <<");
        System.out.println(" -------------------------------------------------------- ");

        System.out.println(" ");

        System.out.println("Tx Verification using WRONG original PublicKey:");
        System.out.println(EcdsaUtil.ecVerifyTransactionSignature(recipientPublicKey, transaction));

        System.out.println(" ");

        System.out.println("Tx Verification using WRONG Address:");
        System.out.println(EcdsaUtil.ecVerifyTransactionSignature(recipientAddress, transaction));

        System.out.println(" ");

        System.out.println("Tx Verification using WRONG Decoded Address:");
        System.out.println(EcdsaUtil.ecVerifyTransactionSignature(recipientDecodedPubKey, transaction));
    }

    static void test02() throws Exception
    {
        KeyPair senderKeyPair = EcdsaUtil.ecGenerateKeyPair();
        PublicKey senderPublicKey = senderKeyPair.getPublic();

        /*
        String senderPubKeyBytes = StringUtil.getStringFromKey(senderPublicKey);
        String senderBase58Key = Base58.encode(senderPubKeyBytes.getBytes());
        System.out.println(senderBase58Key);
        PublicKey senderAddress = ECKeyUtil.getPublicKeyFromString(senderPubKeyBytes);
        */
    }
}
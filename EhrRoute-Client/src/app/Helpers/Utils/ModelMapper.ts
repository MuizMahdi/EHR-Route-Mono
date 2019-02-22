import { EhrPatientInfo } from './../../DataAccess/entities/EHR/EhrPatientInfo';
import { PatientInfo } from './../../Models/Payload/Requests/PatientInfo';
import { Address } from './../../DataAccess/entities/Core/Address';
import { AddressResponse } from './../../Models/Payload/Responses/AddressResponse';
import { EhrAllergyAndReaction } from './../../DataAccess/entities/EHR/EhrAllergyAndReaction';
import { EhrCondition } from './../../DataAccess/entities/EHR/EhrCondition';
import { MedicalRecord } from './../../DataAccess/entities/EHR/MedicalRecord';
import { Block } from './../../DataAccess/entities/Core/Block';
import { BlockResponse } from './../../Models/Payload/Responses/BlockResponse';
import { EhrHistory } from 'src/app/DataAccess/entities/EHR/EhrHistory';


export default class ModelMapper
{

   public static mapBlockResponseToBlock(blockResponse:BlockResponse): Block
   {
      const record = new MedicalRecord();
      record.patientData = blockResponse.transaction.record.patientInfo;
      record.history = [];
      record.conditions = [];
      record.allergies = [];

      
      let conditionsArr:any[] = blockResponse.transaction.record.problems;
      let allergiesArr:any[] = blockResponse.transaction.record.allergiesAndReactions;
      let historyObj:EhrHistory = blockResponse.transaction.record.history;
      let historyArr = Object.entries(historyObj);


      conditionsArr.forEach(condition => {

         let recordCondition = new EhrCondition();

         recordCondition.medicalRecord = record;

         recordCondition.condition = condition;

         record.conditions.push(recordCondition);

      });


      allergiesArr.forEach(allergy => {

         let recordAllergy = new EhrAllergyAndReaction();

         recordAllergy.medicalRecord = record;

         recordAllergy.allergy = allergy;

         record.allergies.push(recordAllergy);

      });


      historyArr.forEach(history => {

         let recordHistory = new EhrHistory();

         recordHistory.medicalRecord = record;

         recordHistory.condition = history[0];
         recordHistory.occurrence = history[1];

         record.history.push(recordHistory);

      });


      const block = new Block();
      block.hash = blockResponse.blockHeader.hash;
      block.previousHash = blockResponse.blockHeader.previousHash;
      block.timeStamp = blockResponse.blockHeader.timeStamp;
      block.merkleLeafHash = blockResponse.blockHeader.merkleLeafHash;
      block.networkUUID = blockResponse.blockHeader.networkUUID;
      block.transactionId = blockResponse.transaction.transactionId;
      block.senderPubKey = blockResponse.transaction.senderPubKey;
      block.senderAddress = blockResponse.transaction.senderAddress;
      block.recipientAddress = blockResponse.transaction.recipientAddress;
      block.signature = blockResponse.transaction.signature;
      block.medicalRecord = record;

      
      return block;
   }


   public static mapAddressResponseToAddress(addressResponse:AddressResponse): Address
   {
      const address = new Address();

      address.address = addressResponse.address;
      address.publicKey = addressResponse.publicKey;
      address.privateKey = addressResponse.privateKey;
      
      return address;
   }


   public static mapPatientInfoToEhrPatientInfo(patientInfo:PatientInfo): EhrPatientInfo
   {
      const ehrPatientInfo = new EhrPatientInfo();

      ehrPatientInfo.name = patientInfo.name;
      ehrPatientInfo.phone = patientInfo.phone;
      ehrPatientInfo.birthDate = patientInfo.birthDate;
      ehrPatientInfo.email = patientInfo.email;
      ehrPatientInfo.gender = patientInfo.gender;
      ehrPatientInfo.city = patientInfo.city;
      ehrPatientInfo.country = patientInfo.country;
      ehrPatientInfo.address = patientInfo.address;

      return ehrPatientInfo;
   }


   public static mapEhrPatientInfoToPatientInfo(ehrPatientInfo:EhrPatientInfo): PatientInfo
   {
      const patientInfo:PatientInfo = {
         name: ehrPatientInfo.name,
         gender: ehrPatientInfo.gender,
         country: ehrPatientInfo.country,
         city: ehrPatientInfo.city,
         address: ehrPatientInfo.address,
         phone: ehrPatientInfo.phone,
         birthDate: ehrPatientInfo.birthDate,
         email: ehrPatientInfo.email
      }

      return patientInfo;
   }

}

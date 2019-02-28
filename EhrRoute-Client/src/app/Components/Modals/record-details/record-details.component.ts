import { TransactionService } from './../../../Services/transaction.service';
import { UpdatedBlockAdditionRequest } from './../../../Models/Payload/Requests/UpdatedBlockAdditionRequest';
import { RecordUpdateData } from './../../../Models/Payload/Requests/RecordUpdateData';
import { AuthService } from './../../../Services/auth.service';
import { BlockAdditionRequest } from './../../../Models/Payload/Requests/BlockAdditionRequest';
import { ChainService } from './../../../Services/chain.service';
import { BlockInfo } from './../../../Models/App/BlockInfo';
import { HealthRecordData } from './../../../Models/App/HealthRecordData';
import { ElectronicHealthRecord } from './../../../Models/App/ElectronicHealthRecord';
import { NzModalService } from 'ng-zorro-antd';
import { Component, OnInit, Input } from '@angular/core';


@Component({
  selector: 'app-record-details',
  templateUrl: './record-details.component.html',
  styleUrls: ['./record-details.component.css']
})


export class RecordDetailsComponent implements OnInit
{
   @Input() EHR: ElectronicHealthRecord;

   patientAge:number;

   recordData: HealthRecordData;
   blockInfo: BlockInfo;

   ehrConditions: string[] = [];
   ehrAllergies: string[] = [];
   ehrHistory: {condition:string; occurrence:boolean;};

   toggleViewedDetails: boolean = true;
   isEditingEhr: boolean = false;

   constructor(
      private modalService:NzModalService, private chainService:ChainService,
      private authService:AuthService, private transactionService:TransactionService
   ) { }


   ngOnInit() {

      if (this.EHR) {
         this.recordData = this.EHR.recordData;
         this.blockInfo = this.EHR.blockInfo;
         this.ehrConditions = this.recordData.conditions;
         this.ehrAllergies = this.recordData.allergies;
         this.calculateAge();
         console.log(this.recordData.patientData.userID);
      }

   }


   private calculateAge(): void {
      let birthDateInMs = this.EHR.recordData.patientData.birthDate;
      let currentTimeInMs = new Date().getTime();
      this.patientAge = Math.floor((currentTimeInMs - birthDateInMs) / (1000*60*60*24*30*12));
   }


   private toggleRecordDetails(): void {
      this.toggleViewedDetails = !this.toggleViewedDetails;
   }


   private addCondition(condition:string): void {
      this.ehrConditions.push(condition);
   }


   private deleteCondition(condition:string): void {

      let conditionIndex = this.ehrConditions.indexOf(condition);

      if (conditionIndex > -1) {
         this.ehrConditions.splice(conditionIndex, 1);
      }

   }


   private addAllergy(allergy:string): void {
      this.ehrAllergies.push(allergy);
   }


   private deleteAllergy(allergy:string): void {

      let allergyIndex = this.ehrConditions.indexOf(allergy);

      if (allergyIndex > -1) {
         this.ehrAllergies.splice(allergyIndex, 1);
      }

   }


   private toggleEhrEditing(): void {
      this.isEditingEhr = !this.isEditingEhr;
   }


   private async requestEhrUpdateConsent() {

      let providerUserId = this.authService.getCurrentUser().id;
      let patientUserId = this.recordData.patientData.userID;
      let providerNetworkUUID = this.blockInfo.networkUUID;

      // Generate a block addition request
      let blockAddition: BlockAdditionRequest = await this.chainService.generateBlockAdditionRequest(providerUserId, patientUserId, providerNetworkUUID);

      // Get the updated/edited EHR data
      let recordUpdateData:RecordUpdateData = {
         conditions: this.ehrConditions,
         allergies: this.ehrAllergies,
         history: {}
      }

      // Construct an UpdatedBlockAdditionRequest
      let updatedBlockRequest:UpdatedBlockAdditionRequest = {
         blockAddition,
         recordUpdateData
      }

      this.transactionService.sendEhrUpdateConsentResponse(updatedBlockRequest).subscribe(

         response => {
            console.log(response);
         },

         error => {
            console.log(error);
         }

      );

      console.log(updatedBlockRequest);
   }
}

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
      private authService:AuthService
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

      let blockAdditionRequest: BlockAdditionRequest = await this.chainService.generateBlockAdditionRequest(providerUserId, patientUserId, providerNetworkUUID);
   
      console.log(blockAdditionRequest);
   }
   

}

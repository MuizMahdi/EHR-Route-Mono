import { NzModalService } from 'ng-zorro-antd';
import { ElectronicHealthRecord } from './../../../Models/App/ElectronicHealthRecord';
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

   ehrConditions: string[] = [];
   ehrAllergies: string[] = [];
   ehrHistory: {condition:string; occurrence:boolean;};

   toggleViewedDetails: boolean = true;
   isEditingEhr: boolean = false;

   constructor(
      private modalService:NzModalService
   ) { }


   ngOnInit() {

      if (this.EHR) {
         this.ehrConditions = this.EHR.conditions;
         this.ehrAllergies = this.EHR.allergies;
         this.calculateAge();
      }

   }


   private calculateAge(): void {
      let birthDateInMs = this.EHR.patientData.birthDate;
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

}

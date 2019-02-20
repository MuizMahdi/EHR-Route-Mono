import { UsersService } from './../../../Services/users.service';
import { NzModalRef } from 'ng-zorro-antd';
import ModelMapper from 'src/app/Helpers/Utils/ModelMapper';
import { DatabaseService } from 'src/app/DataAccess/database.service';
import { AuthService } from './../../../Services/auth.service';
import { CountryResponse } from './../../../Models/Payload/Responses/CountryResponse';
import { LocationService } from './../../../Services/location.service';
import { PatientInfo } from './../../../Models/Payload/Requests/PatientInfo';
import { FormGroup, Validators, FormControl } from '@angular/forms';
import { Component, OnInit } from '@angular/core';


@Component({
  selector: 'app-information-input',
  templateUrl: './information-input.component.html',
  styleUrls: ['./information-input.component.css']
})


export class InformationInputComponent implements OnInit 
{
   userInfoForm: FormGroup;

   selectedGender:string = 'male';
   dateFormat:string = 'yyyy/MM/dd';
   countries:CountryResponse[];
   countryCities:any;

   countryInputValue: string;

   isUserInfoModalLoading:boolean = false;


   constructor(
      private locationService:LocationService, private authSerice:AuthService,
      private databaseService:DatabaseService, private modal:NzModalRef, 
      private userService:UsersService
   ) { }


   ngOnInit() {
      this.buildForm();
      this.getCountries();
   }


   private buildForm(): void
   {
      this.userInfoForm = new FormGroup({
         nameCtrl: new FormControl(null, [Validators.required]),
         birthCtrl: new FormControl(null, [Validators.required]), // Intended
         genderSelectCtrl: new FormControl(null, [Validators.required]),
         phoneCtrl: new FormControl(null, [Validators.required]),
         countryCtrl: new FormControl(null, [Validators.required]),
         cityCtrl: new FormControl(null, [Validators.required]),
         addressCtrl: new FormControl(null, [Validators.required])
      });
   }


   private getCountries(): void
   {
      this.locationService.getCountries().subscribe(countriesData => {
         this.countries = countriesData['_links']['country:items'];
      });
   }


   private onCountrySelect(country:CountryResponse): void
   {
      // Get the country's cities
      this.locationService.getCities(country.name).subscribe(citiesData => {
         this.countryCities = citiesData['_embedded']['city:search-results'];
      });
   }
  

   private async onUserInfoSubmit() {

      // Start the loading animation on the modal's submit button
      this.isUserInfoModalLoading = true;

      // Recalculate the values and validations of form controls
      for (const i in this.userInfoForm.controls) {
        this.userInfoForm.controls[ i ].markAsDirty();
        this.userInfoForm.controls[ i ].updateValueAndValidity();
      }

      // Construct EhrPatientInfo object from form data
      let pateintInfo = this.getPatientInfo();

      // Save patient info on local DB
      await this.savePatientInfo(pateintInfo).then(success => {

         if (success) {
            this.setUserHasSavedInfo();
         }

      });

   }


   private getPatientInfo(): PatientInfo
   {
      // Get current user email
      let userEmail = this.authSerice.getCurrentUser().email;

      let country:string = this.userInfoForm.get("countryCtrl").value.name;
      let city:string = this.userInfoForm.get("cityCtrl").value.matching_full_name;

      // Construct a PatientInfo object using form data
      let userInfo:PatientInfo = {
         name: this.userInfoForm.get("nameCtrl").value,
         gender: this.userInfoForm.get("genderSelectCtrl").value,
         country: country,
         city: city,
         address: this.userInfoForm.get("addressCtrl").value,
         phone: this.userInfoForm.get("phoneCtrl").value,
         birthDate: this.userInfoForm.get("birthCtrl").value.getTime(),
         email: userEmail
      }

      return userInfo;
   }


   private async savePatientInfo(patientInfo:PatientInfo): Promise<boolean>
   {
      let success:boolean;

      // Get current user ID
      let userID = this.authSerice.getCurrentUser().id;

      // Create connection to pateint info DB
      await this.databaseService.createPatientInfoDbConnection(userID);

      // Map to a ehr patient info entity
      let ehrPatientInfo = ModelMapper.mapPatientInfoToEhrPatientInfo(patientInfo);

      // Save on patient info DB
      await this.databaseService.getPatientInfoDbConnection(userID).manager.save(ehrPatientInfo).then(
         response => {
            success = true;
         },

         error => {
            success = false;
         }
      );

      return success;
   }


   private setUserHasSavedInfo(): void
   {
      // Update the user info addition status boolean to true
      this.userService.updateUserInfoAdditionStatus().subscribe(

         response => {
            // Once successfully updated, close the modal
            this.isUserInfoModalLoading = false;
            this.modal.destroy();
         },

         error => {
            console.log(error);
         }

      );
   }

}

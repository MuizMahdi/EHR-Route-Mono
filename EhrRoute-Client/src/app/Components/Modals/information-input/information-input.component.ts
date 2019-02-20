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


   constructor(private locationService:LocationService) 
   { }


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
  

   private onUserInfoSubmit(): void {

      this.isUserInfoModalLoading = true;

      for (const i in this.userInfoForm.controls) {
        this.userInfoForm.controls[ i ].markAsDirty();
        this.userInfoForm.controls[ i ].updateValueAndValidity();
      }

      let userInfo:PatientInfo = {
         name: this.userInfoForm.get("nameCtrl").value,
         gender: this.userInfoForm.get("genderSelectCtrl").value,
         country: this.userInfoForm.get("countryCtrl").value,
         city: this.userInfoForm.get("cityCtrl").value,
         address: this.userInfoForm.get("addressCtrl").value,
         phone: this.userInfoForm.get("phoneCtrl").value,
         birthDate: this.userInfoForm.get("birthCtrl").value.getTime(),
         email: ""
      }

      console.log(userInfo);

      // TODO: Get form data and save it on local DB
      // TODO: Once successfully saved, send a post request that sets the user's hasAddedInfo boolean to true
      // TODO: Once successfully set, close the modal

      window.setTimeout(() => {
         this.isUserInfoModalLoading = false;
      }, 3000);
   }


}

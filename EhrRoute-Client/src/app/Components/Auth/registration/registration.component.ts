import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';


@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})


export class RegistrationComponent 
{
   registrationFormGroup:FormGroup;

   registrationName:string;
   registrationEmail:string;
   registrationUsername:string;
   registrationPassword:string;

   constructor(private router:Router) {
      this.buildForm();
   }

   buildForm(): void
   {
      this.registrationFormGroup = new FormGroup({
         nameCtrl: new FormControl(null, [Validators.required]),
         usernameCtrl: new FormControl(null, [Validators.required]),
         emailCtrl: new FormControl(null, [Validators.required]),
         passwordCtrl: new FormControl(null, [Validators.required])
      });
   }

   onRegistration() 
   {

   }

}

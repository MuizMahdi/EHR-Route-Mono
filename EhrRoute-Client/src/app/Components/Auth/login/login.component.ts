import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})


export class LoginComponent 
{
   loginFormGroup: FormGroup;

   loginUsernameOrEmail: string;
   loginPassword: string;

  constructor(private router:Router) { 
      this.buildForm();
  }

  buildForm(): void 
  {
      this.loginFormGroup = new FormGroup({
         usernameOrEmailCtrl: new FormControl(null, [Validators.required]),
         passwordCtrl: new FormControl(null, Validators.required)
      });
  }

  onLogin() 
  {

  }

}

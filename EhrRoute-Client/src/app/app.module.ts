import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { LoginComponent } from './Components/Auth/login/login.component';
import { RegistrationComponent } from './Components/Auth/registration/registration.component';
import { AuthMainComponent } from './Components/Auth/auth-main/auth-main.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegistrationComponent,
    AuthMainComponent
  ],

  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule
  ],

  providers: [

  ],

  bootstrap: [AppComponent]
})

export class AppModule { }

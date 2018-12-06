import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule } from '@angular/common/http';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

// Interceptors
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { JwtInterceptor } from './Helpers/Interceptors/JwtInterceptor';
import { ErrorInterceptor } from './Helpers/Interceptors/ErrorInterceptor';

// Components
import { AppComponent } from './app.component';
import { LoginComponent } from './Components/Auth/login/login.component';
import { AuthMainComponent } from './Components/Auth/auth-main/auth-main.component';
import { RegistrationComponent } from './Components/Auth/registration/registration.component';
import { MainComponent } from './Components/main/main.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import {NgbModule} from '@ng-bootstrap/ng-bootstrap';



@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegistrationComponent,
    AuthMainComponent,
    MainComponent
  ],

  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    NgbModule
  ],

  providers: [
   { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
   { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }
  ],

  bootstrap: [AppComponent]
})



export class AppModule { }

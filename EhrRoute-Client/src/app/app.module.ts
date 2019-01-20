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
import { SideBarComponent } from './Components/MainLayout/side-bar/side-bar.component';
import { NavBarComponent } from './Components/MainLayout/nav-bar/nav-bar.component';
import { NavUserMenuComponent } from './Components/MainLayout/nav-user-menu/nav-user-menu.component';
import { NavSearchComponent } from './Components/MainLayout/nav-search/nav-search.component';

// External Modules
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ClickOutsideModule } from 'ng-click-outside';
import { NgxElectronModule } from 'ngx-electron';
import { NetworkManagerComponent } from './Components/network-manager/network-manager.component';
import { NgZorroAntdModule, NZ_I18N, en_US } from 'ng-zorro-antd';
import { registerLocaleData } from '@angular/common';
import en from '@angular/common/locales/en';
import { NavUserNotificationsComponent } from './Components/MainLayout/nav-user-notifications/nav-user-notifications.component';
import { AdminPanelComponent } from './Components/admin-panel/admin-panel.component';
import { NetworkInvitationComponent } from './Components/Notifications/network-invitation/network-invitation.component';
import { HealthRecordsManagerComponent } from './Components/health-records-manager/health-records-manager.component';

registerLocaleData(en);



@NgModule({

   entryComponents: [ 
      NetworkInvitationComponent 
   ],

   declarations: [
      AppComponent,
      LoginComponent,
      RegistrationComponent,
      AuthMainComponent,
      MainComponent,
      SideBarComponent,
      NavBarComponent,
      NavUserMenuComponent,
      NavSearchComponent,
      NetworkManagerComponent,
      NavUserNotificationsComponent,
      AdminPanelComponent,
      NetworkInvitationComponent,
      HealthRecordsManagerComponent
   ],

   imports: [
      BrowserModule,
      AppRoutingModule,
      FormsModule,
      ReactiveFormsModule,
      HttpClientModule,
      BrowserAnimationsModule,
      NgbModule,
      ClickOutsideModule,
      NgxElectronModule,
      NgZorroAntdModule
   ],

   providers: [
      { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
      { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
      { provide: NZ_I18N, useValue: en_US }
   ],

   bootstrap: [AppComponent]
})



export class AppModule { }

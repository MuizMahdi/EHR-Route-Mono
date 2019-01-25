import { NgModule } from '@angular/core';
import en from '@angular/common/locales/en';
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
import { MainComponent } from './Components/SideTabs/main/main.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AuthMainComponent } from './Components/Auth/auth-main/auth-main.component';
import { NavBarComponent } from './Components/MainLayout/nav-bar/nav-bar.component';
import { SideBarComponent } from './Components/MainLayout/side-bar/side-bar.component';
import { NavSearchComponent } from './Components/MainLayout/nav-search/nav-search.component';
import { RegistrationComponent } from './Components/Auth/registration/registration.component';
import { AdminPanelComponent } from './Components/SideTabs/admin-panel/admin-panel.component';
import { UserProfileComponent } from './Components/Searches/user-profile/user-profile.component';
import { NetworkManagerComponent } from './Components/SideTabs/network-manager/network-manager.component';
import { NavUserMenuComponent } from './Components/MainLayout/nav-user-menu/nav-user-menu.component';
import { HealthRecordsManagerComponent } from './Components/SideTabs/health-records-manager/health-records-manager.component';
import { NetworkInvitationComponent } from './Components/Notifications/network-invitation/network-invitation.component';
import { NavUserNotificationsComponent } from './Components/MainLayout/nav-user-notifications/nav-user-notifications.component';

// External Modules
import { NgxElectronModule } from 'ngx-electron';
import { registerLocaleData } from '@angular/common';
import { ClickOutsideModule } from 'ng-click-outside';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgZorroAntdModule, NZ_I18N, en_US } from 'ng-zorro-antd';

registerLocaleData(en);



@NgModule({

   entryComponents: [ 
      NetworkInvitationComponent,
      UserProfileComponent
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
      HealthRecordsManagerComponent,
      UserProfileComponent
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

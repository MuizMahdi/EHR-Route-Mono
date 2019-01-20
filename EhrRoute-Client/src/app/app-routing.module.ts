import { HealthRecordsManagerComponent } from './Components/health-records-manager/health-records-manager.component';
import { AdminPanelComponent } from './Components/admin-panel/admin-panel.component';
import { MainComponent } from './Components/main/main.component';
import { AuthGuard } from './Guards/AuthGuard';
import { AuthMainComponent } from './Components/Auth/auth-main/auth-main.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { NetworkManagerComponent } from './Components/network-manager/network-manager.component';


const routes: Routes = [
   {path:'', redirectTo:'main', pathMatch:'full' ,canActivate:[AuthGuard]},
   {path:"main", component:MainComponent, canActivate:[AuthGuard]},
   {path:'login', component:AuthMainComponent},
   {path:'network', component:NetworkManagerComponent, canActivate:[AuthGuard]},
   {path:'panel', component:AdminPanelComponent, canActivate:[AuthGuard]},
   {path:'ehrs', component:HealthRecordsManagerComponent, canActivate:[AuthGuard]}
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})


export class AppRoutingModule { }

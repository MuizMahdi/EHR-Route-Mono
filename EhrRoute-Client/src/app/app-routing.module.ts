import { NgModule } from '@angular/core';
import { AuthGuard } from './Guards/AuthGuard';
import { Routes, RouterModule } from '@angular/router';
import { MainComponent } from './Components/SideTabs/main/main.component';
import { AuthMainComponent } from './Components/Auth/auth-main/auth-main.component';
import { AdminPanelComponent } from './Components/SideTabs/admin-panel/admin-panel.component';
import { NetworkManagerComponent } from './Components/network-manager/network-manager.component';
import { HealthRecordsManagerComponent } from './Components/health-records-manager/health-records-manager.component';


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

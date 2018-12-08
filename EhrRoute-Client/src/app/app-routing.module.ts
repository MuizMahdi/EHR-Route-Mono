import { AuthGuard } from './Guards/AuthGuard';
import { MainComponent } from './Components/MainLayout/main/main.component';
import { AuthMainComponent } from './Components/Auth/auth-main/auth-main.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';


const routes: Routes = [
   {path:"", component:MainComponent, canActivate:[AuthGuard]},
   {path:'login', component:AuthMainComponent},
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule { }

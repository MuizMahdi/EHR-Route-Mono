import { AuthGuard } from './Guards/AuthGuard';
import { MainComponent } from './Components/main/main.component';
import { AuthMainComponent } from './Components/Auth/auth-main/auth-main.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';


const routes: Routes = [
   {path:'login', component:AuthMainComponent},
   {path:'main', component:MainComponent, canActivate:[AuthGuard]}
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule { }

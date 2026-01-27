import { Routes } from '@angular/router';
import { Dashboard } from './components/dashboard/dashboard';
import { Login } from './components/login/login'
import { PedagogicalScheduleComponent } from './components/pedagogical-schedule/pedagogical-schedule';
import {UserManager} from './components/user-manager/user-manager';

export const routes: Routes = [
   /*{
        path:'',
        redirectTo:'login',
        pathMatch:'full',

    },
    {
      path:'login',
      component: Login
    },*/
    {
        path:'',
        component: Dashboard
    },
    {
        path:'syllabus',
        component: Dashboard
    },
    {
        path:'educational-model',
        component: PedagogicalScheduleComponent
    },
    {
        path:'service-sheet',
        component: Dashboard
    },
    {
        path:'training-manager',
        component: Dashboard
    },
    {
      path:'user-manager',
      component: UserManager
    }
];

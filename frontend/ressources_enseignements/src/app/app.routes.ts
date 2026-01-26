import { Routes } from '@angular/router';
import { Dashboard } from './components/dashboard/dashboard';
import { Login } from './components/login/login'
import { PedagogicalScheduleComponent } from './components/pedagogical-schedule/pedagogical-schedule';
import { ServiceSheet } from './components/service-sheet/service-sheet';
export const routes: Routes = [
    {
        path:'',
        redirectTo:'login',
        pathMatch:'full',

    },
    {
      path:'login',
      component: Login
    },
    {
        path:'dashboard',
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
      path: 'service-sheet',
      component: ServiceSheet
    },
];

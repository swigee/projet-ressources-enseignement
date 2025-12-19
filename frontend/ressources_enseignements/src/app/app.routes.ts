import { Routes } from '@angular/router';
import { Dashboard } from './components/dashboard/dashboard';
import { TrainingManager } from './components/training-manager/training-manager';
import { Login } from './components/login/login'
import { PedagogicalScheduleComponent } from './components/pedagogical-schedule/pedagogical-schedule';
import { EducationManagerCreation } from './components/education-manager-creation/education-manager-creation';

export const routes: Routes = [
   /* {
        path:'',
        redirectTo:'login',
        pathMatch:'full',

    },
    {
      path:'login',
      component: Login
    },*/
    {
      path: '',
      component: Dashboard,

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
        path:'service-sheet',
        component: Dashboard
    },
    {
        path:'education-manager',
        component: TrainingManager
    },
    {
        path:'education-manager/create',
        component: EducationManagerCreation
    }
];

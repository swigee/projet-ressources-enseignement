import { Routes } from '@angular/router';
import { Dashboard } from './dashboard/dashboard';
import { PedagogicalScheduleComponent } from './components/pedagogical-schedule/pedagogical-schedule';

export const routes: Routes = [
    {
        path:'',
        component: Dashboard
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
        path:'training-manager',
        component: Dashboard
    }
    
];

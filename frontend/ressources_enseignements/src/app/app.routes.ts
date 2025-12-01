import { Routes } from '@angular/router';
import { Dashboard } from './dashboard/dashboard';
import { TrainingManager } from './training-manager/training-manager';

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
        component: Dashboard
    },
    {
        path:'service-sheet',
        component: Dashboard
    },
    {
        path:'training-manager',
        component: TrainingManager
    }
];

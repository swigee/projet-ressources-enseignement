import { Routes } from '@angular/router';
import { Dashboard } from './components/dashboard/dashboard';
import { EducationalManager } from './components/educational-manager/educational-manager';
import { Login } from './components/login/login'
import { EducationManagerCreation } from './components/education-manager-creation/education-manager-creation';
import {TeacherAssignmentComponent} from './components/teacher-assignment/teacher-assignment';
import {UserManager} from './components/user-manager/user-manager';
import { Ressource } from './components/ressource/ressource';

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
        component: Ressource
    },
    {
        path:'service-sheet',
        component: Dashboard
    },
    {
      path:'teacher-assignment',
      component: TeacherAssignmentComponent
    },
    {
        path:'education-manager',
        component: EducationalManager
    },
    {
        path:'education-manager/create',
        component: EducationManagerCreation
    },
    {
      path:'user-manager',
      component: UserManager
    },
    {
      path: 'ressource',
      component: Ressource
    }

];

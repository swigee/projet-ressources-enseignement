import { Routes } from '@angular/router';
import { Dashboard } from './components/dashboard/dashboard';
import { EducationalManager } from './components/educational-manager/educational-manager';
import { Login } from './components/login/login'
import { PedagogicalScheduleComponent } from './components/pedagogical-schedule/pedagogical-schedule';
import { EducationManagerCreation } from './components/education-manager-creation/education-manager-creation';
import {TeacherAssignmentComponent} from './components/teacher-assignment/teacher-assignment';
import {UserManager} from './components/user-manager/user-manager';

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
    }

];

import { Routes } from '@angular/router';
import { Dashboard } from './components/dashboard/dashboard';
import { EducationalManager } from './components/educational-manager/educational-manager';
import { Login } from './components/login/login'
import { TeacherAssignmentComponent } from './components/teacher-assignment/teacher-assignment';
import { UserManager } from './components/user-manager/user-manager';
import { Ressource } from './components/ressource/ressource';
import { GroupTracking } from './components/group-tracking/group-tracking';
import { authGuard } from './services/auth/auth.guard';
import { roleGuard } from './services/auth/role.guard';

export const routes: Routes = [
  {
    path:'',
    redirectTo:'login',
    pathMatch:'full'
  },
  {
    path: 'login',
    component: Login
  },
  {
    path: 'dashboard',
    component: Dashboard,
    canActivate: [authGuard]
  },
  {
    path: 'syllabus',
    component: Dashboard,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['STUDENT', 'TEACHER'] }
  },
  {
    path: 'educational-model',
    component: Ressource,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['TEACHER', 'ADMIN'] }
  },
  {
    path: 'teacher-assignment',
    component: TeacherAssignmentComponent,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] }
  },
  {
    path:'education-manager',
    component: EducationalManager,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN', 'TEACHER'] }
  },
  {
    path: 'user-manager',
    component: UserManager,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] }
  },
  {
    path: 'ressource',
    component: Ressource,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['TEACHER'] }
  },
  {
    path: 'ticket-manager',
    loadComponent: () => import('./components/ticket-manager/ticket-manager').then(m => m.TicketManager),
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] }
  },
  {
    path: 'group-tracking',
    component: GroupTracking,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] }
  }

];

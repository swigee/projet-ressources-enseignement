import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Auth } from './auth';

export const roleGuard: CanActivateFn = (route, state) => {
    const authService = inject(Auth);
    const router = inject(Router);

    const expectedRoles = route.data['roles'] as string[];

    if (authService.hasRole(expectedRoles)) {
        return true;
    } else {
        if (authService.isAuthenticated()) {
            console.warn('RoleGuard: User does not have required role. Redirecting to dashboard.');
            return router.parseUrl('/dashboard');
        }
        return router.parseUrl('/login');
    }
};

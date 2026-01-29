import { Injectable,signal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class PageTitle {
  title = signal('');
}



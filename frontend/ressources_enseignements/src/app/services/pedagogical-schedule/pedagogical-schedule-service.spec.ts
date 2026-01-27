import { TestBed } from '@angular/core/testing';

import { PedagogicalScheduleService } from './pedagogical-schedule-service';

describe('PedagogicalScheduleService', () => {
  let service: PedagogicalScheduleService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PedagogicalScheduleService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

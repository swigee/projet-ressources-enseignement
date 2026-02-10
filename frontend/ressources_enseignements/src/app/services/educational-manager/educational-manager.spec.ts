import { TestBed } from '@angular/core/testing';

import { EducationalManagerService } from './educational-manager';

describe('EducationalManager', () => {
  let service: EducationalManagerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EducationalManagerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

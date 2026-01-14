import { TestBed } from '@angular/core/testing';

import { EducationalManager } from './educational-manager';

describe('EducationalManager', () => {
  let service: EducationalManager;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EducationalManager);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

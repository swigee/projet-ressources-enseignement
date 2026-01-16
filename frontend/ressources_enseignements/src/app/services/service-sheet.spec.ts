import { TestBed } from '@angular/core/testing';

import { ServiceSheet } from './service-sheet';

describe('ServiceSheet', () => {
  let service: ServiceSheet;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServiceSheet);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

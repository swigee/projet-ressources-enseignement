import { TestBed } from '@angular/core/testing';

import { ServiceSheetService } from './service-sheet.service';

describe('ServiceSheetService', () => {
  let service: ServiceSheetService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServiceSheetService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

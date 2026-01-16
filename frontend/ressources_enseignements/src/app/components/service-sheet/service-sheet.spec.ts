import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ServiceSheet } from './service-sheet';

describe('ServiceSheet', () => {
  let component: ServiceSheet;
  let fixture: ComponentFixture<ServiceSheet>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ServiceSheet]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ServiceSheet);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

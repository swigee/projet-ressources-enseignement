import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EducationalManager } from './educational-manager';

describe('TrainingManager', () => {
  let component: EducationalManager;
  let fixture: ComponentFixture<EducationalManager>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EducationalManager]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EducationalManager);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

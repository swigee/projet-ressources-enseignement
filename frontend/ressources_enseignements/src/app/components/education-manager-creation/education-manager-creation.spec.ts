import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EducationManagerCreation } from './education-manager-creation';

describe('EducationManagerCreation', () => {
  let component: EducationManagerCreation;
  let fixture: ComponentFixture<EducationManagerCreation>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EducationManagerCreation]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EducationManagerCreation);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

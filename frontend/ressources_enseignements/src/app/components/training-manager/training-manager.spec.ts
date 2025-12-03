import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TrainingManager } from './training-manager';

describe('TrainingManager', () => {
  let component: TrainingManager;
  let fixture: ComponentFixture<TrainingManager>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TrainingManager]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TrainingManager);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

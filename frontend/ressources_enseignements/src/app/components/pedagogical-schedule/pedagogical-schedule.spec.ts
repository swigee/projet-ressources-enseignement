import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PedagogicalScheduleComponent } from './pedagogical-schedule';

describe('PedagogicalSchedule', () => {
  let component: PedagogicalScheduleComponent;
  let fixture: ComponentFixture<PedagogicalScheduleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PedagogicalScheduleComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PedagogicalScheduleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

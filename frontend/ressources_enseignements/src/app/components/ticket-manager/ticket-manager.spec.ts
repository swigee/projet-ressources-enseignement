import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TicketManager } from './ticket-manager';

describe('TicketManager', () => {
  let component: TicketManager;
  let fixture: ComponentFixture<TicketManager>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TicketManager]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TicketManager);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

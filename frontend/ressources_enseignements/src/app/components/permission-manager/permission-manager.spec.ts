import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PermissionManager } from './permission-manager';

describe('PermissionManager', () => {
  let component: PermissionManager;
  let fixture: ComponentFixture<PermissionManager>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PermissionManager]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PermissionManager);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { TestBed } from '@angular/core/testing';

import { TeacherAssignment } from './teacher-assignment';

describe('TeacherAssignment', () => {
  let service: TeacherAssignment;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TeacherAssignment);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

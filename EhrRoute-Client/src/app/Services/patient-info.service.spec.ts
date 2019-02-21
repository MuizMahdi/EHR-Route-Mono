import { TestBed } from '@angular/core/testing';

import { PatientInfoService } from './patient-info.service';

describe('PatientInfoService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: PatientInfoService = TestBed.get(PatientInfoService);
    expect(service).toBeTruthy();
  });
});

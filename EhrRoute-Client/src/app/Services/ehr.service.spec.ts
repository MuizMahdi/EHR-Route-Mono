import { TestBed } from '@angular/core/testing';

import { EhrService } from './ehr.service';

describe('EhrService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: EhrService = TestBed.get(EhrService);
    expect(service).toBeTruthy();
  });
});

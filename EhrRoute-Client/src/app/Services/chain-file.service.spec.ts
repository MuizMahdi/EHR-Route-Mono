import { TestBed } from '@angular/core/testing';

import { ChainFileService } from './chain-file.service';

describe('ChainFileService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ChainFileService = TestBed.get(ChainFileService);
    expect(service).toBeTruthy();
  });
});

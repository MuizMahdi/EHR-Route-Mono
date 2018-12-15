import { TestBed } from '@angular/core/testing';

import { NodeNetworkService } from './node-network.service';

describe('NodeNetworkService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: NodeNetworkService = TestBed.get(NodeNetworkService);
    expect(service).toBeTruthy();
  });
});

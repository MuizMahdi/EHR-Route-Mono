import { TestBed } from '@angular/core/testing';

import { NodeClustersService } from './node-clusters.service';

describe('NodeClustersService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: NodeClustersService = TestBed.get(NodeClustersService);
    expect(service).toBeTruthy();
  });
});

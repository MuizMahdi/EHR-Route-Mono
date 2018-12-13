import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NetworkManagerComponent } from './network-manager.component';

describe('NetworkManagerComponent', () => {
  let component: NetworkManagerComponent;
  let fixture: ComponentFixture<NetworkManagerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NetworkManagerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NetworkManagerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NetworkInvitationComponent } from './network-invitation.component';

describe('NetworkInvitationComponent', () => {
  let component: NetworkInvitationComponent;
  let fixture: ComponentFixture<NetworkInvitationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NetworkInvitationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NetworkInvitationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

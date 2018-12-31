import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NavUserNotificationsComponent } from './nav-user-notifications.component';

describe('NavUserNotificationsComponent', () => {
  let component: NavUserNotificationsComponent;
  let fixture: ComponentFixture<NavUserNotificationsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NavUserNotificationsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NavUserNotificationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

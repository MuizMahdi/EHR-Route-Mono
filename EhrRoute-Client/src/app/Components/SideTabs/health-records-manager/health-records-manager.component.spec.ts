import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HealthRecordsManagerComponent } from './health-records-manager.component';

describe('HealthRecordsManagerComponent', () => {
  let component: HealthRecordsManagerComponent;
  let fixture: ComponentFixture<HealthRecordsManagerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HealthRecordsManagerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HealthRecordsManagerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

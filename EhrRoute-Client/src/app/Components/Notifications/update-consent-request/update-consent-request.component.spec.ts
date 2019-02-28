import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateConsentRequestComponent } from './update-consent-request.component';

describe('UpdateConsentRequestComponent', () => {
  let component: UpdateConsentRequestComponent;
  let fixture: ComponentFixture<UpdateConsentRequestComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UpdateConsentRequestComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UpdateConsentRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

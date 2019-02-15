import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsentRequestComponent } from './consent-request.component';

describe('ConsentRequestComponent', () => {
  let component: ConsentRequestComponent;
  let fixture: ComponentFixture<ConsentRequestComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConsentRequestComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConsentRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

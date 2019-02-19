import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InformationInputComponent } from './information-input.component';

describe('InformationInputComponent', () => {
  let component: InformationInputComponent;
  let fixture: ComponentFixture<InformationInputComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InformationInputComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InformationInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

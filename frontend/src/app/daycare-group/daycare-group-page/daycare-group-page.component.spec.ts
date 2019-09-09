import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DaycareGroupPageComponent } from './daycare-group-page.component';

describe('DaycareGroupPageComponent', () => {
  let component: DaycareGroupPageComponent;
  let fixture: ComponentFixture<DaycareGroupPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DaycareGroupPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DaycareGroupPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

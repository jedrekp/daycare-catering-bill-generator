import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DaycareGroupListComponent } from './daycare-group-list.component';

describe('DaycareGroupListComponent', () => {
  let component: DaycareGroupListComponent;
  let fixture: ComponentFixture<DaycareGroupListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DaycareGroupListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DaycareGroupListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

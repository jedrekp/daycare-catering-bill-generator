import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DaycareGroupCreateEditComponent } from './daycare-group-create-edit.component';

describe('DaycareGroupCreateEditComponent', () => {
  let component: DaycareGroupCreateEditComponent;
  let fixture: ComponentFixture<DaycareGroupCreateEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DaycareGroupCreateEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DaycareGroupCreateEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

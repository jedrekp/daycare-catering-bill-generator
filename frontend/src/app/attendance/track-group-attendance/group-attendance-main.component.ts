import { Component, OnInit, QueryList, ViewChild, ViewChildren } from '@angular/core';
import { MatExpansionPanel, MatExpansionPanelHeader } from '@angular/material/expansion';
import { SelectedDateAndGroup } from '../select-date-and-group/select-date-and-group.component';

@Component({
  selector: 'app-group-attendance-main',
  templateUrl: './group-attendance-main.component.html',
  styleUrls: ['./group-attendance-main.component.scss']
})
export class GroupAttendanceMainComponent implements OnInit {

  @ViewChildren(MatExpansionPanel) panels: QueryList<MatExpansionPanel>

  selectedDateAndGroup: SelectedDateAndGroup

  constructor(
  ) { }

  ngOnInit(): void {
  }

  dataSelected(data: SelectedDateAndGroup) {
    this.selectedDateAndGroup = data
    this.panels.first.close()
    this.panels.last.open()
    if (document.activeElement instanceof HTMLElement)
      document.activeElement.blur()
  }

}

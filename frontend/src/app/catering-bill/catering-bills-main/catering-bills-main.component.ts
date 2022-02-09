import { Component, OnInit, QueryList, ViewChildren } from '@angular/core';
import { MatExpansionPanel } from '@angular/material/expansion';
import { SelectedMonthAndGroup } from '../select-month-and-group/select-month-and-group.component';

@Component({
  selector: 'app-catering-bills-main',
  templateUrl: './catering-bills-main.component.html',
  styleUrls: ['./catering-bills-main.component.scss']
})
export class CateringBillsMainComponent implements OnInit {

  @ViewChildren(MatExpansionPanel) panels: QueryList<MatExpansionPanel>

  selectedMonthAndGroup: SelectedMonthAndGroup

  constructor() { }

  ngOnInit(): void {
  }

  dataSelected(data: SelectedMonthAndGroup) {
    this.selectedMonthAndGroup = data
    this.panels.first.close()
    this.panels.last.open()
    if (document.activeElement instanceof HTMLElement)
      document.activeElement.blur()
  }

}


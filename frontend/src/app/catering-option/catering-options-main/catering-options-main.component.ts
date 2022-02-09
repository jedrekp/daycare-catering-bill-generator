import { Component, OnInit, QueryList, ViewChild, ViewChildren } from '@angular/core';
import { MatExpansionPanel } from '@angular/material/expansion';
import { CateringOptionsListComponent } from '../catering-options-list/catering-options-list.component';

@Component({
  selector: 'app-catering-options-main',
  templateUrl: './catering-options-main.component.html',
  styleUrls: ['./catering-options-main.component.scss']
})

export class CateringOptionsMainComponent implements OnInit {

  @ViewChild(CateringOptionsListComponent) existingOptions: CateringOptionsListComponent;
  @ViewChildren(MatExpansionPanel) panels: QueryList<MatExpansionPanel>

  constructor() { }

  ngOnInit(): void {
  }

  onOptionCreated() {
    this.existingOptions.retrieveCateringOptions()
    this.panels.first.close()
    this.panels.last.open()
    this.existingOptions.tabs.selectedIndex = 0
    if (document.activeElement instanceof HTMLElement)
    document.activeElement.blur()
  }

}

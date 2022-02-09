import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTabGroup } from '@angular/material/tabs';
import { CateringOptionDataService } from 'src/app/service/data/catering-option-data.service';
import { ActiveCateringOptionsComponent } from '../active-catering-options/active-catering-options.component';
import { CateringOption } from '../catering-option';
import { DisabledCateringOptionsComponent } from '../disabled-catering-options/disabled-catering-options.component';

@Component({
  selector: 'app-catering-options-list',
  templateUrl: './catering-options-list.component.html',
  styleUrls: ['./catering-options-list.component.scss']
})
export class CateringOptionsListComponent implements OnInit {

  @ViewChild(ActiveCateringOptionsComponent) active: ActiveCateringOptionsComponent
  @ViewChild(DisabledCateringOptionsComponent) disabled: DisabledCateringOptionsComponent
  @ViewChild(MatTabGroup) tabs: MatTabGroup

  activeOptions: CateringOption[]
  disabledOptions: CateringOption[]

  cateringOptionFilter = (option: CateringOption, filter: string) => {
    const optionName = option.optionName.toLowerCase()
    return optionName.startsWith(filter)
  }

  constructor(
    private cateringOptionDataService: CateringOptionDataService
  ) { }

  ngOnInit(): void {
    this.retrieveCateringOptions()
  }

  retrieveCateringOptions() {
    this.cateringOptionDataService.getAll().subscribe(
      options => {
        this.activeOptions = options.filter(option => !option.disabled)
        this.disabledOptions = options.filter(option => option.disabled)
      })
  }

  applyFilters(query: string) {
    this.active.applyFilter(query)
    this.disabled.applyFilter(query)
  }

}
import { Component, OnInit } from '@angular/core';
import { CateringOption } from '../CateringOption';
import { CateringOptionDataService } from '../catering-option-data.service';

@Component({
  selector: 'app-catering-options-list',
  templateUrl: './catering-options-list.component.html',
  styleUrls: ['./catering-options-list.component.css']
})
export class CateringOptionsListComponent implements OnInit {

  private activeCateringOptions: CateringOption[]
  private disabledCateringOptions: CateringOption[]
  private header: string
  private showDisabled: boolean

  constructor(private cateringOptionDataService: CateringOptionDataService) { }

  ngOnInit() {
    this.switchToActive()
    this.retrieveCateringOptions()
  }

  retrieveCateringOptions() {
    this.cateringOptionDataService.retriveCateringOptionsByDisabled(false).subscribe(
      cateringOptions => {
        this.activeCateringOptions = cateringOptions
      })
    this.cateringOptionDataService.retriveCateringOptionsByDisabled(true).subscribe(
      cateringOptions => {
        this.disabledCateringOptions = cateringOptions
      })
  }

  switchToDisabled() {
    this.showDisabled = true
    this.header = 'Disabled catering options'
  }

  switchToActive() {
    this.showDisabled = false
    this.header = 'Active catering options'
  }

}




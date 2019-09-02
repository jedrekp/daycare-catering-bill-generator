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

  constructor(private cateringOptionDataService: CateringOptionDataService) { }

  ngOnInit() {
    this.retrieveCateringOptions()
  }

  retrieveCateringOptions() {
    this.cateringOptionDataService.retriveCateringOptionsByDisabled('false').subscribe(
      cateringOptions => {
        this.activeCateringOptions = cateringOptions
      })
    this.cateringOptionDataService.retriveCateringOptionsByDisabled('true').subscribe(
      cateringOptions => {
        this.disabledCateringOptions = cateringOptions
      })
  }

}




import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CateringOption } from 'src/app/catering-option/catering-option';
import { UnprocessableEntityError } from 'src/app/common/error/unprocessable-entity-error';
import { CateringOptionDataService } from 'src/app/service/data/catering-option-data.service';
import { ChildDataService } from 'src/app/service/data/child-data.service';
import { DateHelperService } from 'src/app/service/date-helper.service';

@Component({
  selector: 'app-child-assign-option',
  templateUrl: './child-assign-option-modal.component.html',
  styleUrls: ['./child-assign-option-modal.component.scss']
})
export class ChildAssignOptionModalComponent implements OnInit {

  minDate: Date
  effectiveDate: Date
  cateringOptions: CateringOption[] = []
  selectedOptionId: number

  constructor(
    public dialogRef: MatDialogRef<ChildAssignOptionModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private cateringOptionDataService: CateringOptionDataService,
    private childDataService: ChildDataService,
    private dateHelperService: DateHelperService,
    private snackbar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.minDate = new Date(2020, 0, 1)
    this.effectiveDate = this.dateHelperService.getCurrentDateOrMondayIfWeekend()
    this.retrieveCateringOptions()
  }

  retrieveCateringOptions() {
    this.cateringOptionDataService.getAllByDisabled(false).subscribe(
      options => this.cateringOptions = options,
      err => {
        this.dialogRef.close()
        throw err
      })
  }

  assignOption() {
    const stringDate = this.dateHelperService.getDateAsString(this.effectiveDate)
    this.childDataService.assignNewOptionToChild(this.data.childId, this.selectedOptionId, stringDate).subscribe(
      assignedOption => {
        this.dialogRef.close(this.data.childId)
        this.snackbar.open(`New catering option has been assigned (effective from: ${assignedOption.effectiveDate}).`)
      },
      err => {
        if (err instanceof UnprocessableEntityError)
          this.snackbar.open(err.originalError?.error, null, { panelClass: 'dcbg-error-snackbar' })
        else {
          this.dialogRef.close()
          throw err
        }
      })
  }

}

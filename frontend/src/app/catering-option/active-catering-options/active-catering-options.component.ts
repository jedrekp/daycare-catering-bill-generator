import { Component, EventEmitter, Input, OnChanges, Output, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { CateringOptionDataService } from 'src/app/service/data/catering-option-data.service';
import { UserDialogComponent } from 'src/app/user-dialog/user-dialog.component';
import { CateringOption } from '../catering-option';
import { EditCateringOptionModalComponent } from '../edit-catering-option-modal/edit-catering-option-modal.component';

@Component({
  selector: 'app-active-catering-options',
  templateUrl: './active-catering-options.component.html',
  styleUrls: ['./active-catering-options.component.scss']
})
export class ActiveCateringOptionsComponent implements OnChanges {

  @Input('options') options: CateringOption[]
  @Input('filter') filter: (option: CateringOption, filter: string) => boolean
  @Output('optionChanged') optionChanged = new EventEmitter<number>()


  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator
  tableDataSource: MatTableDataSource<CateringOption>
  displayedColumns: string[] = ['optionId', 'optionName', 'dailyCost', 'editOption', 'disableOption']

  constructor(
    private cateringOptionDataService: CateringOptionDataService,
    private snackbar: MatSnackBar,
    private dialog: MatDialog
  ) { }

  ngOnChanges(): void {
    this.setUpDataTable()
  }

  setUpDataTable() {
    this.tableDataSource = new MatTableDataSource(this.options)
    this.tableDataSource.paginator = this.paginator
    this.tableDataSource.filterPredicate = this.filter
  }

  applyFilter(query: string) {
    this.tableDataSource.filter = query.trim().toLowerCase()

    if (this.tableDataSource.paginator)
      this.tableDataSource.paginator.firstPage()
  }

  openEditCateringOptionDialog(option: CateringOption) {
    const dialogRef = this.dialog.open(EditCateringOptionModalComponent, {
      disableClose: true,
      panelClass: 'dcbg-dialog',
      data:
      {
        cateringOption: option
      }
    })
    dialogRef.afterClosed().subscribe(
      optionId => {
        if (optionId)
          this.optionChanged.emit(optionId)
      })
  }

  disableOption(option: CateringOption) {
    const dialogRef = this.dialog.open(UserDialogComponent, {
      disableClose: true,
      panelClass: 'dcbg-dialog',
      data:
      {
        message: `<p>Disabled catering option can no longer be assigned to children.</p>
        <p>Please note, that it will not be removed from children, to whom it is already assigned.</p>`
      }
    })

    dialogRef.afterClosed().subscribe(
      onClose => {
        if (onClose) {
          option.disabled = true
          this.cateringOptionDataService.update(option, option.id).subscribe(
            cateringOption => {
              this.snackbar.open(`Catering option #${cateringOption.id} is now disabled.`)
              this.optionChanged.emit(cateringOption.id)
            })
        }
      })
  }

}
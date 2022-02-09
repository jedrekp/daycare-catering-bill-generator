import { Component, EventEmitter, Input, OnChanges, OnInit, Output, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { CateringOptionDataService } from 'src/app/service/data/catering-option-data.service';
import { CateringOption } from '../catering-option';
import { EditCateringOptionModalComponent } from '../edit-catering-option-modal/edit-catering-option-modal.component';

@Component({
  selector: 'app-disabled-catering-options',
  templateUrl: './disabled-catering-options.component.html',
  styleUrls: ['./disabled-catering-options.component.scss']
})
export class DisabledCateringOptionsComponent implements OnChanges {

  @Input('options') options: CateringOption[]
  @Input('filter') filter: (option: CateringOption, filter: string) => boolean
  @Output('optionChanged') optionChanged = new EventEmitter<number>()

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator


  tableDataSource: MatTableDataSource<CateringOption>
  displayedColumns: string[] = ['optionId', 'optionName', 'dailyCost', 'editOption', 'enableOption']

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

  enableOption(option: CateringOption) {
    option.disabled = false
    this.cateringOptionDataService.update(option, option.id).subscribe(
      cateringOption => {
        this.snackbar.open(`Catering option #${cateringOption.id} is no longer disabled.`)
        this.optionChanged.emit(cateringOption.id)
      })
  }

}
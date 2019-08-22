import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Child } from '../child';
import { ChildDataService } from '../child-data.service';

@Component({
  selector: 'app-child-create-edit',
  templateUrl: './child-create-edit.component.html',
  styleUrls: ['./child-create-edit.component.css']
})
export class ChildCreateEditComponent implements OnInit {

  private childId: number
  private header: string
  private child: Child

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private dataService: ChildDataService
  ) { }

  ngOnInit() {
    this.childId = this.route.snapshot.params['childId'];
    this.child = new Child(this.childId, '', '')
    if (this.childId != -1) {
      this.retriveChild(this.childId)
    }
    else {
      this.header = 'Add new child'
    }
  }

  retriveChild(childId: number) {
    this.dataService.retrieveChild(childId).subscribe(
      child => {
        this.child = child
        this.header = `Edit child - ${this.child.firstName} ${this.child.lastName}`
      })
  }

  saveChild() {
    if (this.childId == -1) {
      this.dataService.createChild(this.child).subscribe(
        child => {
          this.router.navigate([''])
        })
      }
    }
  }

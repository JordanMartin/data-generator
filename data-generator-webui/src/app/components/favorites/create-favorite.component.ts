import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-create-favorite',
  templateUrl: './create-favorite.component.html',
  styleUrls: ['./create-favorite.component.scss']
})
export class CreateFavoriteComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<CreateFavoriteComponent>) { }

  ngOnInit(): void {
  }

}

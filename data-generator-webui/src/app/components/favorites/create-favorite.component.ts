import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { StorageService } from "../../services/storage-service";

@Component({
  selector: 'app-create-favorite',
  templateUrl: './create-favorite.component.html',
  styleUrls: ['./create-favorite.component.scss']
})
export class CreateFavoriteComponent implements OnInit {

  favorites: string[] = [];

  constructor(public storage: StorageService, public dialogRef: MatDialogRef<CreateFavoriteComponent>) { }

  ngOnInit(): void {
    this.favorites = this.storage.loadFavorites().map(f => f.name);
  }
}

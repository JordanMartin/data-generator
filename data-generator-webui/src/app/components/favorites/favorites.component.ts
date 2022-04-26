import { Component } from '@angular/core';
import { StorageService } from '../../services/storage-service';
import { Favorite } from '../../services/favorite';
import { MatDialog } from '@angular/material/dialog';
import { ManageFavoritesComponent } from '../manage-favorites/manage-favorites.component';
import { CreateFavoriteComponent } from './create-favorite.component';

@Component({
  selector: 'app-config-store',
  templateUrl: './favorites.component.html',
  styleUrls: ['./favorites.component.scss']
})
export class FavoritesComponent {

  favorite_name: string = '';
  favorites!: Favorite[];

  constructor(public storage: StorageService, private dialog: MatDialog) {
    this.loadFavorites();
    this.storage.favorite_name.subscribe(name => this.favorite_name = name);
  }

  loadFavorites() {
    this.favorites = this.storage.loadFavorites();
  }

  manageFavorites() {
    this.dialog.open(ManageFavoritesComponent, {
      width: '800px'
    }).afterClosed().subscribe(() => this.loadFavorites());
  }

  createNewFavorite() {
    this.dialog.open(CreateFavoriteComponent, {
      width: '300px',
    }).afterClosed().subscribe((name) => {
      if (!name) {
        return;
      }
      this.storage.saveCurrent(name);
      this.storage.loadFavorite(name);
      this.loadFavorites();
    });
  }
}

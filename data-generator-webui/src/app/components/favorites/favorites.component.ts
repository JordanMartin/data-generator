import {Component, HostListener} from '@angular/core';
import {StorageService} from '../../services/storage-service';
import {Favorite} from '../../services/favorite';
import {MatDialog} from '@angular/material/dialog';
import {ManageFavoritesComponent} from '../manage-favorites/manage-favorites.component';
import {CreateFavoriteComponent} from './create-favorite.component';

@Component({
  selector: 'app-config-store',
  templateUrl: './favorites.component.html',
  styleUrls: ['./favorites.component.scss']
})
export class FavoritesComponent {

  favorite_name: string = '';
  favorites!: Favorite[];
  loaded_favorite!: Favorite;

  constructor(public storage: StorageService, private dialog: MatDialog) {
    this.loadFavorites();
    this.storage.loaded_favorite.subscribe(favorite => this.loaded_favorite = favorite);
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
      width: '400px',
    }).afterClosed().subscribe((name) => {
      if (!name) {
        return;
      }
      this.storage.saveCurrent(name);
      this.storage.loadFavorite(name);
      this.loadFavorites();
    });
  }

  @HostListener('window:keydown', ['$event'])
  onKeyDown(event: KeyboardEvent) {
    if ((event.metaKey || event.ctrlKey) && event.shiftKey && event.key === 'S') {
      this.createNewFavorite();
      event.preventDefault();
    } else if ((event.metaKey || event.ctrlKey) && event.key === 's') {
      if (!this.loaded_favorite) {
        this.createNewFavorite();
      } else {
        this.storage.saveCurrent(this.loaded_favorite.name)
      }
      event.preventDefault();
    }
  }
}

import {Component} from '@angular/core';
import {StorageService} from '../../services/storage-service';
import {Favorite} from '../../services/favorite';
import {MatDialog} from "@angular/material/dialog";
import {ManageFavoritesComponent} from "../manage-favorites/manage-favorites.component";

@Component({
  selector: 'app-config-store',
  templateUrl: './favorites.component.html',
  styleUrls: ['./favorites.component.scss']
})
export class FavoritesComponent {

  favorite_name: string = '';
  favorites!: Favorite[];

  constructor(private storage: StorageService, private dialog: MatDialog) {
    this.loadFavorites();
  }

  loadFavorites() {
    this.favorites = this.storage.loadFavorites();
  }

  saveCurrent(name: string) {
    if (!name) {
      return;
    }
    this.favorites = this.storage.saveFavorite({
      name,
      definition: this.storage.definition.getValue(),
      output_config: this.storage.output_config.getValue()
    } as Favorite);
  }

  load(name: string) {
    const favorite = this.favorites.filter(f => f.name === name)[0];
    this.favorite_name = name;
    if (!favorite) {
      return;
    }
    this.storage.output_config_load.next(favorite.output_config);
    this.storage.definition_load.next(favorite.definition);
  }

  manageFavorites() {
    this.dialog.open(ManageFavoritesComponent, {
      width: '600px',
      data: {
        favorites: this.favorites
      }
    }).afterClosed().subscribe(() => this.loadFavorites());
  }
}

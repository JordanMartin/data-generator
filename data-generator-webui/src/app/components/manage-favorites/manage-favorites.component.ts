import {Component, OnInit} from '@angular/core';
import {OutputConfig} from "../output-config/output-config";
import {Favorite} from "../../services/favorite";
import {StorageService} from "../../services/storage-service";
import {MatSnackBar} from "@angular/material/snack-bar";
import samplesDefinition from './sample-definitions.json';

@Component({
  selector: 'app-manage-favorites',
  templateUrl: './manage-favorites.component.html',
  styleUrls: ['./manage-favorites.component.scss']
})
export class ManageFavoritesComponent implements OnInit {

  favorites: Favorite[] = [];

  constructor(private storage: StorageService,
              private snackBar: MatSnackBar) {
    this.favorites = this.storage.loadFavorites();
  }

  ngOnInit(): void {
  }

  loadExamples() {
    this.storage.saveFavorites(samplesDefinition);
    this.favorites = this.storage.loadFavorites();
  }

  exportFavorites() {
    const now = new Date();
    const format = (n: number) => n < 9 ? '0' + n : n;
    const date = `${now.getFullYear()}-${format(now.getMonth())}-${format(now.getDate())}`;
    const time = `${format(now.getHours())}-${format(now.getMinutes())}-${format(now.getSeconds())}`;
    this.downloadData(`data-generator-favorites-${date}_${time}.json`, this.favorites)
  }

  downloadData(filename: string, data: any) {
    const a = document.createElement('a') as HTMLAnchorElement;
    a.style.display = 'none';
    document.body.appendChild(a);
    const json = JSON.stringify(data, null, 2);
    const blob = new Blob([json], {type: 'octet/stream'});
    const url = window.URL.createObjectURL(blob);
    a.href = url;
    a.download = filename;
    a.click();
    window.URL.revokeObjectURL(url);
  };

  removeFavorite(favorite: Favorite) {
    this.storage.removeFavorite(favorite.name);
    this.favorites = this.storage.loadFavorites();
  }

  async importFavorites(file: File) {
    let favorites: Favorite[];
    try {
      const json = await file.text();
      favorites = JSON.parse(json);
      favorites = favorites.filter(favorite => !!favorite.name);
    } catch (e) {
      this.snackBar.open('❌ Failed to import your saved configuration. The file is not valid', 'Close');
      return;
    }
    this.storage.saveFavorites(favorites);
    this.favorites = this.storage.loadFavorites();
    const plural = (favorites.length > 1 ? 's' : '');
    this.snackBar.open(`✔ ${favorites.length} configuration${plural} imported`, 'Ok', {
      duration: 10000
    });
  }

  loadFavorite(favorite: Favorite) {
      this.storage.loadFavorite(favorite.name);
  }
}

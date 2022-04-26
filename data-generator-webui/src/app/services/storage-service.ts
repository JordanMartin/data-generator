import { BehaviorSubject } from 'rxjs';
import { Injectable } from '@angular/core';
import { OutputConfig } from '../components/output-config/output-config';
import { Favorite } from './favorite';

@Injectable({
  providedIn: 'root'
})
export class StorageService {

  private readonly LOCAL_STORAGE_FAVORITE_KEY = 'favorites';

  public favorite_name = new BehaviorSubject<string>('');
  public dirty_favorite = new BehaviorSubject<boolean>(false);
  public definition = new BehaviorSubject<string>('');
  public output_config = new BehaviorSubject<OutputConfig>({} as OutputConfig);

  public definition_load = new BehaviorSubject<string>('template:\n  id: UUID()');
  public output_config_load = new BehaviorSubject<OutputConfig>({
    count: 5,
    format: 'yaml',
    pretty: true
  } as OutputConfig);

  constructor() {
    this.definition.subscribe(def => this.dirty_favorite.next(true));
    this.output_config.subscribe(def => this.dirty_favorite.next(true));
  }

  loadFavorites(): Favorite[] {
    const favorites = window.localStorage.getItem(this.LOCAL_STORAGE_FAVORITE_KEY);
    if (favorites == null) {
      return [];
    }
    return JSON.parse(favorites);
  }

  loadFavorite(name: string): void {
    const favorite: Favorite = this.loadFavorites()
      .filter(favorite => favorite.name === name)[ 0 ];
    if (!favorite) {
      return;
    }
    this.favorite_name.next(name);
    this.output_config_load.next(favorite.output_config);
    this.definition_load.next(favorite.definition);
    this.dirty_favorite.next(false);
  }

  saveFavorite(favorite: Favorite): Favorite[] {
    return this.saveFavorites([favorite]);
  }

  saveFavorites(favorites: Favorite[]): Favorite[] {
    const newFavoritesNames = favorites.map(f => f.name);
    const newFavorites = this.loadFavorites()
      .filter(f => !newFavoritesNames.includes(f.name))
      .concat(favorites)
      .sort((a, b) => a.name.localeCompare(b.name));
    this.persitFavorites(newFavorites);
    return newFavorites;
  }

  removeFavorite(name: string) {
    const favorites = this.loadFavorites().filter(favorite => favorite.name !== name);
    this.persitFavorites(favorites);
  }

  private persitFavorites(favorites: Favorite[]) {
    window.localStorage.setItem(this.LOCAL_STORAGE_FAVORITE_KEY, JSON.stringify(favorites));
  }

  saveCurrent(name: string) {
    this.saveFavorite({
      name: name,
      definition: this.definition.getValue(),
      output_config: this.output_config.getValue()
    } as Favorite);
    this.dirty_favorite.next(false);
  }
}

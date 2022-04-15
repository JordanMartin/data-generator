import {BehaviorSubject} from 'rxjs';
import {Injectable} from '@angular/core';
import {OutputConfig} from '../components/output-config/output-config';
import {Favorite} from "./favorite";

@Injectable({
  providedIn: 'root'
})
export class StorageService {

  private readonly LOCAL_STORAGE_FAVORITE_KEY = 'favorites';

  public definition = new BehaviorSubject<string>('');
  public output_config = new BehaviorSubject<OutputConfig>({} as OutputConfig);

  public definition_load = new BehaviorSubject<string>('template:\n  id: UUID()');
  public output_config_load = new BehaviorSubject<OutputConfig>({
    count: 5,
    format: 'yaml',
    pretty: true
  } as OutputConfig);

  constructor() {
  }

  loadFavorites(): Favorite[] {
    const favorites = window.localStorage.getItem(this.LOCAL_STORAGE_FAVORITE_KEY);
    if (favorites == null) {
      return [];
    }
    return JSON.parse(favorites);
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
}

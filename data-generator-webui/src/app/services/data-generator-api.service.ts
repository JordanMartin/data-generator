import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {GeneratorDoc} from './generator-doc';
import {OutputConfig} from '../components/output-config/output-config';
import {map} from 'rxjs/operators';
import {VersionInfo} from "./version-info";

@Injectable({
  providedIn: 'root'
})
export class DataGeneratorApiService {

  private base_api = '/api';

  constructor(private http: HttpClient) {
  }

  generateFromTemplate(definition: string, config: OutputConfig) {
    const form = new URLSearchParams();
    Object.entries(config).forEach(([name, value]) => {
      form.set('output.' + name, value);
    });
    form.set('definition', definition);

    return this.http.post(
      this.base_api + '/provider/live',
      form,
      {
        responseType: 'text',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        }
      }
    );
  }

  getAvailableGenerator(): Observable<GeneratorDoc[]> {
    return this.http.get<{ [key: string]: GeneratorDoc }>(this.base_api + '/provider-doc',
      {
        responseType: 'json',
      }
    ).pipe(
      map(response => {
        return Object.values(response).sort(DataGeneratorApiService.sortGenerator);
      })
    );
  }

  getVersion(): Observable<VersionInfo> {
    return this.http.get<VersionInfo>(this.base_api + '/version',
      {
        responseType: 'json',
      }
    );
  }

  private static sortGenerator(a: GeneratorDoc, b: GeneratorDoc): number {
    const compareGroupe = (a.groupe || '').localeCompare(b.groupe || '');
    const compareType = (a.type || '').localeCompare(b.type || '');
    const compareName = (a.name || '').localeCompare(b.name || '');
    if (compareGroupe != 0) {
      return compareGroupe;
    }
    if (compareType != 0) {
      return compareType;
    }
    return compareName;
  }


}

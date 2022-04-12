import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {GeneratorDoc} from './generator-doc';
import {OutputConfig} from '../components/output-config/output-config';
import {map} from 'rxjs/operators';

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
    return this.http.get<{ [ key: string ]: GeneratorDoc }>(this.base_api + '/provider-doc',
      {
        responseType: 'json',
      }
    ).pipe(
      map(response => {
        return Object.values(response);
      })
    );
  }

  getVersion(): Observable<string> {
    return this.http.get<{version: string}>(this.base_api + '/version',
      {
        responseType: 'json',
      }
    ).pipe(
      map(response => {
        return response.version;
      })
    );
  }
}

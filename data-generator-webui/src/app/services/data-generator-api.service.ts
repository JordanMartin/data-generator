import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {GeneratorDoc} from './generator-doc';
import {OutputConfig} from "../components/output-config/output-config";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class DataGeneratorApiService {

  private base_api = '/api';

  constructor(private http: HttpClient) {
  }

  generateFromTemplate(definition: string, config: OutputConfig) {
    const form = new URLSearchParams();
    form.set('output.format', config.format);
    form.set('output.pretty', String(config.pretty));
    form.set('output.template', config.template);
    form.set('output.object_name', config.object_name);
    form.set('output.table_name', config.table_name);
    form.set('output.separator', config.separator);
    form.set('output.count', String(config.count));
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
        return Object.values(response)
          .sort((a, b) => a.name.localeCompare(b.name));
      })
    );
  }
}

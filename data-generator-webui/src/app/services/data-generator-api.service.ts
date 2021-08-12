import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {GeneratorDescription} from './generator-description';
import {GeneratorArg} from './generator-arg';
import {ArgType} from './arg-type';
import {OutputConfig} from "../components/output-config/output-config";

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
    form.set('count', String(config.count));
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

  getAvailableGenerator(): Observable<GeneratorDescription[]> {
    return new Observable((subscriber) => {
      subscriber.next([
        new GeneratorDescription('IntAutoIncrement', 'Incrémente la valeur pour chaque objet', [
          new GeneratorArg("min", "Valeur de départ", ArgType.INTEGER, true),
          new GeneratorArg("step", "Quantité à incrémenter", ArgType.INTEGER, true),
          new GeneratorArg("max", "Valeur maximum", ArgType.INTEGER, true),
        ]),
        new GeneratorDescription('RandomUUID', 'Génère un UUID (java.util.UUID)', []),
      ]);
    });
  }
}

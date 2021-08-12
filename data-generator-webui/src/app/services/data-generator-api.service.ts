import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {GeneratorDescription} from './generator-description';
import {GeneratorArg} from './generator-arg';
import {ArgType} from './arg-type';

@Injectable({
  providedIn: 'root'
})
export class DataGeneratorApiService {

  private base_api = '/api';

  constructor(private http: HttpClient) {
  }

  generateFromTemplate(definition: string, count: number, output: any) {
    const form = new URLSearchParams();
    form.set('output.format', output.format);
    form.set('output.pretty', output.pretty);
    form.set('output.template', output.template);
    form.set('output.object_name', output.object_name);
    form.set('count', count.toString());
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

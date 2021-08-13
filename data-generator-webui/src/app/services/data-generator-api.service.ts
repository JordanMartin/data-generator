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

  getAvailableGenerator(): Observable<GeneratorDescription[]> {
    return new Observable((subscriber) => {
      subscriber.next([
        new GeneratorDescription('IntAutoIncrement', 'Incrémente la valeur pour chaque objet', [
          new GeneratorArg("min", "Valeur de départ", ArgType.INTEGER, true, 0),
          new GeneratorArg("step", "Quantité à incrémenter", ArgType.INTEGER, true, 1),
          new GeneratorArg("max", "Valeur maximum", ArgType.INTEGER, true, 2147483647),
        ]),
        new GeneratorDescription('RandomUUID', 'Génère un UUID (java.util.UUID)'),
        new GeneratorDescription('Constant', 'Valeur constante', [
          new GeneratorArg('value', 'La valeur constante', ArgType.ANY),
        ]),
        new GeneratorDescription('CurrentDate', 'Date actuelle'),
        new GeneratorDescription('FormatDate', 'Formatte une date', [
          new GeneratorArg("generator", 'La date à formatter', ArgType.STRING),
          new GeneratorArg("generator", 'Le format de date (java.text.SimpleDateFormat)', ArgType.STRING),
        ]),
        new GeneratorDescription('RandomDate', 'Date aléatoire entre deux dates', [
          new GeneratorArg("min", "Interval minimum (yyyy-mm-dd)", ArgType.STRING, true, "Y-1"),
          new GeneratorArg("max", "Interval maximum (yyyy-mm-dd)", ArgType.STRING, true, "Y+1"),
        ]),
        new GeneratorDescription('RandomBoolean', 'Booléen aléatoire', [
          new GeneratorArg("percentageOfTrue", "Ratio de génération d'une valeur true entre 0 et 1", ArgType.DOUBLE, true, 0.5),
        ]),
        new GeneratorDescription('RandomFromList', 'TODO', []),
        new GeneratorDescription('RandomFromRegex', 'TODO', []),
        new GeneratorDescription('RandomInt', 'TODO', []),
        new GeneratorDescription('RandomDouble', 'TODO', []),
        new GeneratorDescription('Round', 'TODO', []),
        new GeneratorDescription('SequenceFromList', 'TODO', []),
        new GeneratorDescription('AsString', 'Transforme une valeur en chaine de caractère', [
          new GeneratorArg("generator", 'Le générateur source', ArgType.GENERATOR),
        ]),
        new GeneratorDescription('Idempotent', 'Retourne toujours la même valeur à partir d\'un autre générateur', [
          new GeneratorArg("generator", "Le générateur source", ArgType.GENERATOR),
        ]),
        new GeneratorDescription('ListOf', 'Créer un tableau à partir d\'autre générateurs', [
          new GeneratorArg("generators", "Les générateurs", ArgType.ARRAY),
        ]),
        new GeneratorDescription('ListByRepeat', 'Répète un générateur', [
          new GeneratorArg("generator", "Le générateur à répéter", ArgType.GENERATOR),
          new GeneratorArg("percentageOfTrue", "Nombre de répétition", ArgType.INTEGER),
        ]),
        new GeneratorDescription('Sample', 'TODO', [])
      ]);
    });
  }
}

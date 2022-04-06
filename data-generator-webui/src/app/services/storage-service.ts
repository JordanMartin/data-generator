import {BehaviorSubject} from 'rxjs';
import {Injectable} from '@angular/core';
import {OutputConfig} from '../components/output-config/output-config';

@Injectable({
  providedIn: 'root'
})
export class StorageService {

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
}

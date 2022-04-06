import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { OutputConfig } from './output-config';
import { StorageService } from '../../services/storage-service';

@Component({
  selector: 'app-output-config',
  templateUrl: './output-config.component.html',
  styleUrls: ['./output-config.component.scss']
})
export class OutputConfigComponent implements OnInit {

  @Output() onChange = new EventEmitter<OutputConfig>();

  available_formats = ['json', 'yaml', 'xml', 'sql', 'csv', 'template'];
  csv_default_separator = ['<tab>', ';', ','];
  count_max_warning = 1000;

  config: OutputConfig = {
    include_null: true,
    count: 5,
    format: 'json',
    pretty: true,
    object_name: 'my_object',
    table_name: 'my_table',
    separator: ';',
    gzip: false,
    single_file: false,
    filename_template: '',
    template: [].join('\n'),
  };

  constructor(private storage: StorageService) {
  }

  ngOnInit(): void {
    this.storage.output_config_load.subscribe(conf => {
      for (let param of Object.keys(conf)) {
        // @ts-ignore
        this.config[ param ] = conf[ param ];
      }
      this.onChange.next(this.config);
    });
  }

  updateParam(param: string, value: any) {
    // @ts-ignore
    this.config[ param ] = value;
    this.onChange.next(this.config);
  }
}

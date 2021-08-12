import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {OutputConfig} from "./output-config";

@Component({
  selector: 'app-output-config',
  templateUrl: './output-config.component.html',
  styleUrls: ['./output-config.component.scss']
})
export class OutputConfigComponent implements OnInit {

  @Output() onChange = new EventEmitter<OutputConfig>();

  default_config: OutputConfig = {
    count: 5,
    format: 'json',
    pretty: true,
    object_name: 'my_object',
    table_name: 'my_table',
    separator: ';',
    template: ['##',
      '## Documentation of velocity template here : https://velocity.apache.org/engine/devel/user-guide.html',
      '##',
      '#set ($title = "Random people list")',
      '',
      '$title',
      '===================',
      '#foreach($data in $list)',
      'id=$data.id',
      'name=$data.firstname $data.lastname',
      'age=$data.age',
      '#if($data.age >= 18)',
      'major=yes',
      '#else',
      'major=no',
      '#end',
      '---------',
      '#end'].join('\n'),
  };

  config: OutputConfig = {...this.default_config}
  available_formats = ['json', 'yaml', 'xml', 'sql', 'csv', 'template'];
  csv_default_separator = ['<tab>', ';', ','];

  constructor() {
  }

  ngOnInit(): void {
    this.sendConfig();
  }

  updateParam(param: string, value: any) {
    // @ts-ignore
    this.config[param] = value;
    this.sendConfig();
  }

  private sendConfig() {
    this.onChange.next(this.config);
  }
}

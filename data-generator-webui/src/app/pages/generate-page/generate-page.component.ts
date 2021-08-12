import {Component, OnInit} from '@angular/core';
import {DataGeneratorApiService} from '../../services/data-generator-api.service';
import {Subject} from 'rxjs';
import {debounceTime, map} from 'rxjs/operators';
import {MatDialog} from "@angular/material/dialog";
import {DownloadForm} from "./download-form/download-form.component";

@Component({
  selector: 'app-generate-page',
  templateUrl: './generate-page.component.html',
  styleUrls: ['./generate-page.component.scss']
})
export class GeneratePageComponent implements OnInit {

  private generateSubject = new Subject();
  auto_generate = true;
  definition!: string;
  generated: any;

  defaults = {
    count: 5,
    format: 'json',
    pretty: true,
    object_name: 'object',
    auto_generate_debounce_ms: 200,
    available_formats: ['json', 'yaml', 'xml', 'sql', 'csv', 'template'],
    initial_definition: [
      'template:',
      'id: IntAutoIncrement()',
      'firstname: Sample("Name.firstName")',
      'lastname: Sample("Name.lastName")',
      'age: RandomInt(1, 99)'
    ].join('\n  '),
    initial_template: ['##',
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
      '#end'].join('\n')
  }

  output_count = this.defaults.count;
  output_format = this.defaults.format;
  output_pretty = this.defaults.pretty;
  output_template = this.defaults.initial_template;
  is_request_pending = false;
  output_object_name = this.defaults.object_name;

  constructor(private api: DataGeneratorApiService, private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.generateSubject.pipe(
      debounceTime(this.defaults.auto_generate_debounce_ms),
      map(() => this.generate())
    ).subscribe();
  }

  generateDebounce() {
    this.generateSubject.next();
  }

  generate() {
    this.is_request_pending = true;
    return this.api.generateFromTemplate(this.definition, this.output_count, {
      format: this.output_format,
      pretty: this.output_pretty,
      template: this.output_template,
      object_name: this.output_object_name
    })
      .subscribe(
        result => this.updateGenerated(result),
        error => this.updateGenerated(error.error)
      );
  }

  updateGenerated(generatedData: string) {
    this.generated = {
      content: generatedData,
      format: this.output_format
    };
    this.is_request_pending = false;
  }

  updateDefinition(definition: string) {
    this.definition = definition
    if (this.auto_generate) {
      this.generateDebounce();
    }
  }

  updateCount(count: string) {
    this.output_count = parseInt(count);
    if (this.auto_generate) {
      this.generate();
    }
  }

  updateFormat(format: string) {
    this.output_format = format;
    if (this.auto_generate) {
      this.generate();
    }
  }

  changeAutoGenerate(auto_generate: boolean) {
    this.auto_generate = auto_generate;
    if (auto_generate) {
      this.generate();
    }
  }

  download() {
    const dialogRef = this.dialog.open(DownloadForm, {
      width: '250px',
      data: {
        format: this.output_format,
        definition: this.definition,
        count: this.output_count,
        pretty: this.output_pretty,
        template: this.output_template,
        object_name: this.output_object_name
      }
    });
  }

  updateTemplate(template: string) {
    this.output_template = template;
    this.generateDebounce();
  }

  changeOutputPretty(pretty: boolean) {
    this.output_pretty = pretty;
    this.generate();
  }

  updateObjectName(objectName: string) {
    this.output_object_name = objectName;
    this.generateDebounce();
  }
}


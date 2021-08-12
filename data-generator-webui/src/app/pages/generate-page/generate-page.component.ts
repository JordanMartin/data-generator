import {Component, OnInit} from '@angular/core';
import {DataGeneratorApiService} from '../../services/data-generator-api.service';
import {Subject} from 'rxjs';
import {debounceTime, map} from 'rxjs/operators';
import {MatDialog} from "@angular/material/dialog";
import {DownloadForm} from "./download-form/download-form.component";
import {OutputConfig} from "../../components/output-config/output-config";

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
  output_config!: OutputConfig;

  is_request_pending = false;
  defaults = {
    auto_generate_debounce_ms: 200,
    initial_definition: [
      'template:',
      'id: IntAutoIncrement()',
      'firstname: Sample("Name.firstName")',
      'lastname: Sample("Name.lastName")',
      'age: RandomInt(1, 99)'
    ].join('\n  ')
  }

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
    if (!this.output_config || !this.definition) {
      return;
    }
    this.is_request_pending = true;
    return this.api.generateFromTemplate(this.definition, this.output_config)
      .subscribe(
        result => this.updateGenerated(result),
        error => this.updateGenerated(error.error)
      );
  }

  updateGenerated(generatedData: string) {
    this.generated = {
      content: generatedData,
      format: this.output_config.format
    };
    this.is_request_pending = false;
  }

  updateDefinition(definition: string) {
    this.definition = definition
    if (this.auto_generate) {
      this.generateDebounce();
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
        definition: this.definition,
        config: this.output_config
      }
    });
  }

  updateOutputConfig(config: OutputConfig) {
    this.output_config = config;
    if (this.auto_generate) {
      this.generate();
    }
  }
}


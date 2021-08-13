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

  // TODO initial template
//   references:
//     firstname: Sample("Name.firstName")
//   lastname: Sample("Name.lastName")
//   gen_date: Idempotent(FormatDate(CurrentDate(), "yyyy-MM-dd HH:mm:ss.SSS"))
//   id: RandomUUID()
//   item:
//     parent_id: $$id
//   horodatage: $gen_date
//   name: $("${firstname} $${lastname}")
//
//   template:
//     id: $$id
//   name: $("${firstname} $${lastname}")
//   num: IntAutoIncrement()
//   random: RandomInt(100, 1000)
//   horodatage: $gen_date
//   childs: ListByRepeat($item, 2)
//   active: RandomBoolean()
//   type: RandomFromList(["A", "B", "C"])
//   group: RandomFromList([ItemWeight("A", 10), ItemWeight("B", 90)])
//   array:
// - Round(RandomDouble(0, 10), 2)
// - RandomInt(10, 100)

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
    this.dialog.open(DownloadForm, {
      width: '500px',
      data: {
        definition: this.definition,
        config: this.output_config
      }
    });
  }

  updateOutputConfig({update_now, config}: { update_now: boolean, config: OutputConfig }) {
    this.output_config = config;

    if (this.auto_generate) {
      if (update_now) {
        this.generate();
      } else {
        this.generateDebounce();
      }
    }
  }
}


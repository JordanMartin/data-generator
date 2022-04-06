import { Component, OnInit } from '@angular/core';
import { DataGeneratorApiService } from '../../services/data-generator-api.service';
import { Subject } from 'rxjs';
import { debounceTime, map } from 'rxjs/operators';
import { MatDialog } from '@angular/material/dialog';
import { DownloadForm } from '../../components/download-form/download-form.component';
import { OutputConfig } from '../../components/output-config/output-config';
import { StorageService } from '../../services/storage-service';

@Component({
  selector: 'app-generate-page',
  templateUrl: './generate-page.component.html',
  styleUrls: ['./generate-page.component.scss']
})
export class GeneratePageComponent implements OnInit {

  private generateSubject = new Subject();
  auto_generate = true;
  currentDefinition!: string;
  generated: any;
  output_config!: OutputConfig;

  is_request_pending = false;
  is_error: boolean = false

  defaults = {
    auto_generate_debounce_ms: 200
  }

  constructor(private api: DataGeneratorApiService, public storage: StorageService, private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.generateSubject.pipe(
      debounceTime(this.defaults.auto_generate_debounce_ms),
      map(() => this.generate())
    ).subscribe();

    this.storage.definition.subscribe(definition => this.updateDefinition(definition));
    this.storage.output_config.subscribe(output_config => this.updateOutputConfig({
      update_now: true,
      config: output_config
    }));
  }

  generateDebounce() {
    this.generateSubject.next();
  }

  generate() {
    if (!this.output_config || !this.currentDefinition) {
      return;
    }
    this.is_request_pending = true;
    return this.api.generateFromTemplate(this.currentDefinition, this.output_config)
      .subscribe(
        result => this.updateGenerated(result),
        error => this.updateGenerated(error.error, true)
      );
  }

  updateGenerated(generatedData: string, isError: boolean = false) {
    this.generated = {
      content: generatedData,
      format: this.output_config.format
    };
    this.is_request_pending = false;
    this.is_error = isError;
  }

  updateDefinition(definition: string) {
    this.currentDefinition = definition
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
        definition: this.currentDefinition,
        config: this.output_config
      }
    });
  }

  updateOutputConfig({ update_now, config }: { update_now: boolean, config: OutputConfig }) {
    this.output_config = config;

    if (this.auto_generate) {
      if (update_now) {
        this.generate();
      } else {
        this.generateDebounce();
      }
    }
  }

  drag = false;
  dragEvent!: MouseEvent;
  leftPaneWidth = 'calc(50% - 1px)';
  rightPaneWidth = 'calc(50% - 1px)';
  lastOffset = 0;

  startResize(e: MouseEvent) {
    this.drag = true;
    this.dragEvent = e;
  }

  stopResize(e: MouseEvent) {
    if (!this.drag) return;
    this.drag = false;
    this.resize(e);
    this.lastOffset -= (e.screenX - this.dragEvent.screenX);
  }

  doResize(e: MouseEvent) {
    if (!this.drag) return;
    this.resize(e);
  }

  resize(e: MouseEvent) {
    let newSize = e.screenX - this.dragEvent.screenX - this.lastOffset;
    this.leftPaneWidth = 'calc(50% + ' + newSize + 'px)';
    this.rightPaneWidth = 'calc(50% - ' + (newSize + 2) + 'px)';
  }
}


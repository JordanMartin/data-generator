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
  currentDefinition!: string;
  editorDefinition!: string;
  generated: any;
  output_config!: OutputConfig;

  is_request_pending = false;
  defaults = {
    auto_generate_debounce_ms: 200
  }

  constructor(private api: DataGeneratorApiService, private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.generateSubject.pipe(
      debounceTime(this.defaults.auto_generate_debounce_ms),
      map(() => this.generate())
    ).subscribe();
    this.reloadFromCache();
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
    this.currentDefinition = definition
    if (this.auto_generate) {
      this.generateDebounce();
    }
    this.saveToCache();
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

  reloadFromCache() {
    try {
      const savedDefinition = window.localStorage.getItem('definition') || '';
      const savedOutputConfig = JSON.parse(window.localStorage.getItem('output_config') || '');
      this.editorDefinition = savedDefinition;
      this.output_config = savedOutputConfig;
    } catch (e) {
      console.log('echec de chargement du cache', e);
    }
  }

  saveToCache() {
    window.localStorage.setItem('definition', this.currentDefinition);
    window.localStorage.setItem('output_config', JSON.stringify(this.output_config));
  }

  drag = false;
  dragEvent!: MouseEvent;
  leftPaneWidth = "calc(50% - 1px)";
  rightPaneWidth = "calc(50% - 1px)";
  lastOffset = 0;

  startResize(e: MouseEvent) {
    console.log('start', e);
    this.drag = true;
    this.dragEvent = e;
  }

  stopResize(e: MouseEvent) {
    if (!this.drag) {
      return;
    }
    this.drag = false;
    this.resize(e);
    this.lastOffset -= (e.screenX - this.dragEvent.screenX);
    console.log(this.leftPaneWidth, this.rightPaneWidth, this.lastOffset);
  }

  doResize(e: MouseEvent) {
    if (!this.drag) {
      return;
    }
    this.resize(e);
  }

  resize(e: MouseEvent) {
    let newSize = e.screenX - this.dragEvent.screenX - this.lastOffset;
    this.leftPaneWidth = 'calc(50% + ' + newSize + 'px)';
    this.rightPaneWidth = 'calc(50% - ' + (newSize + 2) + 'px)';
  }
}


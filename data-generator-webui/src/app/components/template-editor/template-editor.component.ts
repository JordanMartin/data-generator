import {AfterViewInit, Component, EventEmitter, Input, NgZone, OnDestroy, Output} from '@angular/core';
import * as monaco from 'monaco-editor';

import {velocity_lang} from './velocity-template-lang';
import IStandaloneCodeEditor = monaco.editor.IStandaloneCodeEditor;
import IStandaloneEditorConstructionOptions = monaco.editor.IStandaloneEditorConstructionOptions;

@Component({
  selector: 'app-template-editor',
  templateUrl: './template-editor.component.html',
  styleUrls: ['./template-editor.component.scss']
})
export class TemplateEditorComponent implements AfterViewInit, OnDestroy {

  @Output() onChange = new EventEmitter<string>();
  @Input() initialValue!: string;

  private editor!: IStandaloneCodeEditor;

  editorOptions: IStandaloneEditorConstructionOptions = {
    theme: 'vs-light',
    language: 'velocity',
    fontSize: 13,
    formatOnPaste: true,
    formatOnType: true,
    automaticLayout: true,
    scrollBeyondLastLine: false,
    tabSize: 2,
  };

  constructor(private _ngZone: NgZone) {
  }

  ngOnDestroy(): void {
    this.editor.dispose();
  }

  ngAfterViewInit() {
    monaco.languages.register({ id: 'velocity' });
    monaco.languages.setMonarchTokensProvider('velocity', velocity_lang);
    this.initMonaco();
  }

  // Will be called once monaco library is available
  initMonaco() {
    const editorDiv = document.getElementById('template-editor')!;
    this.editor = monaco.editor.create(editorDiv, this.editorOptions);
    this.editor.setValue(this.initialValue || '');
    this.editor.getModel()!.onDidChangeContent(() => {
      this.updateCode(this.editor.getValue());
    });
    this.updateCode(this.editor.getValue());
  }

  updateCode(code: string) {
    this._ngZone.run(() => {
      this.onChange.emit(code);
    });
  }
}

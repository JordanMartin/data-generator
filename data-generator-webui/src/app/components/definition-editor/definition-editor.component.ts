import {AfterViewInit, Component, EventEmitter, Input, NgZone, OnDestroy, Output} from '@angular/core';
import * as monaco from 'monaco-editor';
import IStandaloneEditorConstructionOptions = monaco.editor.IStandaloneEditorConstructionOptions;
import IStandaloneCodeEditor = monaco.editor.IStandaloneCodeEditor;

@Component({
  selector: 'app-definition-editor',
  templateUrl: './definition-editor.component.html',
  styleUrls: ['./definition-editor.component.scss']
})
export class DefinitionEditorComponent implements AfterViewInit, OnDestroy {

  @Output() onChange = new EventEmitter<string>();
  @Input() initialValue!: string;

  editorOptions: IStandaloneEditorConstructionOptions = {
    theme: 'vs-light',
    language: 'yaml',
    fontSize: 13,
    formatOnPaste: true,
    formatOnType: true,
    automaticLayout: true,
    scrollBeyondLastLine: false,
    tabSize: 2,
  };
  private editor!: IStandaloneCodeEditor;

  constructor(private _ngZone: NgZone) {
  }

  ngAfterViewInit() {
    this.initMonaco();
  }

  ngOnDestroy(): void {
    this.editor.dispose();
  }

  initMonaco() {
    const editorDiv = document.getElementById('definition-editor')!;
    this.editor = monaco.editor.create(editorDiv, this.editorOptions);
    this.editor.setValue(this.initialValue || '');
    this.editor.getModel()!.onDidChangeContent((event) => {
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

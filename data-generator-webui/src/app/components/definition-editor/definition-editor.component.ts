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

  @Input()
  set value(value: string) {
    console.log('changed', value);
    this._value = value;
    if (this.editor) {
      this.editor.setValue(value);
    }
  }

  private _value!: string;

  editorOptions: IStandaloneEditorConstructionOptions = {
    theme: 'vs-light',
    language: 'yaml',
    fontSize: 13,
    formatOnPaste: true,
    formatOnType: true,
    automaticLayout: true,
    scrollBeyondLastLine: false,
    tabSize: 2,
    scrollbar: {
      alwaysConsumeMouseWheel: false
    }
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
    this.editor.setValue(this._value || '');
    this.editor.getModel()!.onDidChangeContent((event) => {
      this.updateCode(this.editor.getValue());
    });
    if (this.editor.getValue()) {
      this.updateCode(this.editor.getValue());
    }
  }

  updateCode(code: string) {
    this._ngZone.run(() => {
      this.onChange.emit(code);
    });
  }
}

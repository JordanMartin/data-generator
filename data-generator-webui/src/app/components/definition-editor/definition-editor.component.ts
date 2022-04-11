import { AfterViewInit, Component, EventEmitter, Input, NgZone, OnDestroy, Output } from '@angular/core';
import * as monaco from 'monaco-editor';
import IStandaloneEditorConstructionOptions = monaco.editor.IStandaloneEditorConstructionOptions;
import IStandaloneCodeEditor = monaco.editor.IStandaloneCodeEditor;
import { StorageService } from '../../services/storage-service';

@Component({
  selector: 'app-definition-editor',
  templateUrl: './definition-editor.component.html',
  styleUrls: ['./definition-editor.component.scss']
})
export class DefinitionEditorComponent implements AfterViewInit, OnDestroy {

  @Output() onChange = new EventEmitter<string>();

  @Input()
  set definitionError(isError: boolean) {
    this.is_definition_error = isError;
  }

  private _value!: string;
  is_definition_error: boolean = false;

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

  constructor(private _ngZone: NgZone, private storage: StorageService) {
  }

  ngAfterViewInit() {
    this.initMonaco();
    this.storage.definition_load
      .subscribe(value => {
        this._value = value;
        if (this.editor) {
          this.editor.setValue(value);
        }
        this.onChange.emit(value);
      });
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

import {AfterViewInit, Component, Input, NgZone, OnChanges, OnDestroy, SimpleChanges} from '@angular/core';
import * as monaco from "monaco-editor";
import {editor as monacoEditor} from "monaco-editor";
import IStandaloneEditorConstructionOptions = monacoEditor.IStandaloneEditorConstructionOptions;

@Component({
  selector: 'app-data-preview',
  templateUrl: './data-preview.component.html',
  styleUrls: ['./data-preview.component.scss']
})
export class DataPreviewComponent implements AfterViewInit, OnChanges, OnDestroy {

  @Input() data: any;

  private editor!: monacoEditor.IStandaloneCodeEditor;
  editorOptions: IStandaloneEditorConstructionOptions = {
    theme: 'vs-light',
    fontSize: 13,
    formatOnPaste: true,
    formatOnType: true,
    automaticLayout: true,
    readOnly: true,
    scrollBeyondLastLine: false,
    tabSize: 2
  };

  constructor(private _ngZone: NgZone) {
  }

  ngAfterViewInit() {
    this.initMonaco();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (this.editor) {
      this.editor.setValue(this.data.content || '');
      monaco.editor.setModelLanguage(this.editor.getModel()!, this.data.format);
    }
  }

  ngOnDestroy(): void {
    this.editor.dispose();
  }

  initMonaco() {
    const editorDiv = document.getElementById('viewer-editor')!;
    this.editor = monaco.editor.create(editorDiv, this.editorOptions);
  }
}

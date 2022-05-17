import {Component, Inject} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {OutputConfig} from "../output-config/output-config";

@Component({
  templateUrl: './download-form.component.html',
  styleUrls: ['./download-form.component.scss']
})
export class DownloadForm {
  definition: string;
  config: OutputConfig;

  constructor(
    public dialogRef: MatDialogRef<DownloadForm>,
    @Inject(MAT_DIALOG_DATA) data: { definition: string; config: OutputConfig }) {
    this.config = {...data.config};
    this.config.filename_template = '#num.' + this.config.format;
    this.config.count_per_file = 1;
    this.definition = data.definition;
  }

  download() {
    this.dialogRef.close();
  }

  computeNumberOfFile() {
    return Math.ceil(this.config.count / (this.config.count_per_file || 1));
  }
}

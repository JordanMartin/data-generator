import {Component, Inject} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {OutputConfig} from "../../../components/output-config/output-config";

@Component({
  templateUrl: './download-form.component.html'
})
export class DownloadForm {
  count: number;
  gzip = false;
  definition: string;

  constructor(
    public dialogRef: MatDialogRef<DownloadForm>,
    @Inject(MAT_DIALOG_DATA) public data: { definition: string; config: OutputConfig }) {
    this.count = data.config.count;
    this.definition = data.definition;
  }

  download() {
    this.dialogRef.close();
  }
}

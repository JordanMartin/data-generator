import {Component, Inject} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  templateUrl: './download-form.component.html'
})
export class DownloadForm {
  count!: string;
  gzip = false;

  constructor(
    public dialogRef: MatDialogRef<DownloadForm>,
    @Inject(MAT_DIALOG_DATA) public data: any) {
    this.count = data.count;
  }

  download() {
    this.dialogRef.close();
  }
}

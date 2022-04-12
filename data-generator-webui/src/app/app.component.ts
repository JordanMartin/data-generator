import {Component} from '@angular/core';
import {DataGeneratorApiService} from './services/data-generator-api.service';
import {VersionInfo} from "./services/version-info";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'data-generator';
  versionInfo!: VersionInfo;

  constructor(private api: DataGeneratorApiService) {
    api.getVersion().subscribe(versionInfo => this.versionInfo = versionInfo);
  }
}

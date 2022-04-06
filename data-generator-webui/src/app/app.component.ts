import { Component } from '@angular/core';
import { DataGeneratorApiService } from './services/data-generator-api.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'data-generator';
  version!: string;

  constructor(private api: DataGeneratorApiService) {
    api.getVersion().subscribe(version => this.version = version);
  }
}

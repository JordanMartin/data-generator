import {Component, OnInit} from '@angular/core';
import {DataGeneratorApiService} from '../../services/data-generator-api.service';
import {GeneratorDescription} from '../../services/generator-description';

@Component({
  selector: 'app-generator-doc',
  templateUrl: './generator-doc.component.html',
  styleUrls: ['./generator-doc.component.scss']
})
export class GeneratorDocComponent implements OnInit {

  generators!: GeneratorDescription[];

  constructor(private api: DataGeneratorApiService) { }

  ngOnInit(): void {
    this.api.getAvailableGenerator()
      .subscribe(generators => this.generators = generators);
  }

}

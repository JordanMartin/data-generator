import {Component, OnInit} from '@angular/core';
import {DataGeneratorApiService} from '../../services/data-generator-api.service';
import {GeneratorDoc} from '../../services/generator-doc';
import {GeneratorCtor} from "../../services/generator-ctor";
import {GeneratorArg} from "../../services/generator-arg";

@Component({
  selector: 'app-generator-doc',
  templateUrl: './generator-doc.component.html',
  styleUrls: ['./generator-doc.component.scss']
})
export class GeneratorDocComponent implements OnInit {

  generators!: GeneratorDoc[];

  constructor(private api: DataGeneratorApiService) {
  }

  ngOnInit(): void {
    this.api.getAvailableGenerator()
      .subscribe(generators => this.generators = generators);
  }

  getCtorUsage(generator: GeneratorDoc, ctor: GeneratorCtor) {
    let name = generator.name;
    let args = ctor.args.map(arg => arg.name).join(', ');
    return name + '(' + args + ')';
  }

  getArgExamples(arg: GeneratorArg) {
    let examples = arg.examples;
    if (arg.type === 'string') {
      examples = arg.examples
        .map(ex => '"' + ex + '"');
    }
    return examples.join(', ');
  }
}

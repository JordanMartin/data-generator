import { Component, OnInit } from '@angular/core';
import { DataGeneratorApiService } from '../../services/data-generator-api.service';
import { GeneratorDoc } from '../../services/generator-doc';
import { GeneratorCtor } from '../../services/generator-ctor';
import { GeneratorArg } from '../../services/generator-arg';
import { SimplifyJavaTypePipe } from './simplify-java-type.pipe';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-generator-doc',
  templateUrl: './generator-doc.component.html',
  styleUrls: ['./generator-doc.component.scss']
})
export class GeneratorDocComponent implements OnInit {

  generators: GeneratorDoc[] = [];
  generatorsFiltered: GeneratorDoc[] = [];
  private _search: string = '';
  private _sort: string = 'name';

  constructor(private api: DataGeneratorApiService) {
  }

  ngOnInit(): void {
    const simplifyJavaTypePipe = new SimplifyJavaTypePipe();
    this.api.getAvailableGenerator()
      .subscribe(generators => {
        generators.forEach(g => g.type = simplifyJavaTypePipe.transform(g.type));
        this.generators = generators;
        this.search = '';
      });
  }

  set search(value: string) {
    this._search = value;
    this.filterGenerators();
  }

  get search() {
    return this._search;
  }

  get sort(): string {
    return this._sort;
  }

  set sort(value: string) {
    this._sort = value;
    this.filterGenerators();
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

  get preffiledIssueUrl() {
    let title = encodeURIComponent('Ajouter un générateur de ' + this._search);
    let body = encodeURIComponent('Description :  \n\nType retourné : \n\nParamètres : ');
    const labels = 'enhancement';
    return `https://github.com/JordanMartin/data-generator/issues/new?title=${title}&labels=${labels}&body=${body}`;
  }

  private sortGenerator(a: GeneratorDoc, b: GeneratorDoc): number {
    const compareType = (a.type || '').localeCompare(b.type || '');
    const compareName = (a.name || '').localeCompare(b.name || '');
    if (this._sort === 'type') {
      return compareType == 0 ? compareName : compareType;
    }

    return compareName == 0 ? compareType : compareName;
  }

  private filterGenerators() {
    const search = this._search.trim().toLowerCase();
    const filtered = this.generators.filter(g => {
      const nameMatch = g.name.toLowerCase().indexOf(search) !== -1;
      const descriptionMatch = g.description.toLowerCase().indexOf(search) !== -1;
      const returnTypeMatch = g.type.toLowerCase().indexOf(search) !== -1;
      return nameMatch || descriptionMatch || returnTypeMatch;
    });
    this.generatorsFiltered = filtered.sort(this.sortGenerator.bind(this));
  }
}

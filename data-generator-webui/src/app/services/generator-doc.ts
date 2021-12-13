import { GeneratorCtor } from './generator-ctor';

export class GeneratorDoc {

  name: string;
  description!: string;
  type: string;
  constructors: GeneratorCtor[];
  examples: string[] = [];
  groupe: string;

  constructor(name: string, description: string, type: string, constructors: GeneratorCtor[], examples: string[], groupe: string) {
    this.name = name;
    this.description = description;
    this.type = type;
    this.constructors = constructors;
    this.examples = examples;
    this.groupe = groupe;
  }
}

import {GeneratorCtor} from "./generator-ctor";

export class GeneratorDoc {

  name: string;
  description!: string;
  constructors: GeneratorCtor[];
  examples: string[] = [];

  constructor(name: string, description: string, constructors: GeneratorCtor[], examples: string[]) {
    this.name = name;
    this.description = description;
    this.constructors = constructors;
    this.examples = examples;
  }
}

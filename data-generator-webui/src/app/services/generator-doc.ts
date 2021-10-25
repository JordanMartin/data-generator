import {GeneratorCtor} from "./generator-ctor";

export class GeneratorDoc {

  name: string;
  description!: string;
  type: string;
  constructors: GeneratorCtor[];
  examples: string[] = [];

  constructor(name: string, description: string, type: string, constructors: GeneratorCtor[], examples: string[]) {
    this.name = name;
    this.description = description;
    this.type = type;
    this.constructors = constructors;
    this.examples = examples;
  }
}

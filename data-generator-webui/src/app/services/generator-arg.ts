export class GeneratorArg {
  name!: string;
  type!: string;
  description!: string;
  examples: string[] = [];

  constructor(name: string, type: string, description: string, examples: string[]) {
    this.name = name;
    this.type = type;
    this.description = description;
    this.examples = examples;
  }
}

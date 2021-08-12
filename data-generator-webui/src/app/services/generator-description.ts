import { GeneratorArg } from './generator-arg';

export class GeneratorDescription {

  name!: string;
  description!: string;
  args: GeneratorArg[] = [];

  constructor(name: string, description: string, args: GeneratorArg[]) {
    this.name = name;
    this.description = description;
    this.args = args;
  }
}

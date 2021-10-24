import {GeneratorArg} from "./generator-arg";

export class GeneratorCtor {
  description!: string;
  args: GeneratorArg[] = [];

  constructor(description: string, args: GeneratorArg[]) {
    this.description = description;
    this.args = args;
  }
}

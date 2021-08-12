import { ArgType } from './arg-type';

export class GeneratorArg {
  name: string;
  description: string;
  type: ArgType;
  optional: boolean;
  defaultValue: any;

  constructor(name: string, description: string, type: ArgType, optional: boolean = false, defaultValue: any = null) {
    this.name = name;
    this.description = description;
    this.type = type;
    this.optional = optional;
  }
}

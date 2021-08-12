import { FieldDefinition } from './field-definition';

export class ObjectDefinition {
  fields: FieldDefinition[] = [];

  fieldExists(name: string) {
    return this.fields.find(field => field.name == name) !== undefined;
  }

  addField(fieldDefinition: FieldDefinition) {
    this.fields.push(fieldDefinition);
  }

  toYaml() {
    return this.fields.map(field => `  ${field.name}: ${field.definition}`).join('\n');
  }
}

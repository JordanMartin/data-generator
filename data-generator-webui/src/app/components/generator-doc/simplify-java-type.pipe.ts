import {Pipe, PipeTransform} from '@angular/core';

/**
 * Simplifie l'affichage des types Java
 */
@Pipe({name: 'simplifyJavaType'})
export class SimplifyJavaTypePipe implements PipeTransform {
  typesMap: any = {
    'int': 'Integer',
    'long': 'Long',
    'float': 'Float',
    'double': 'Double',
    'boolean': 'Boolean',
    'io.github.jordanmartin.datagenerator.provider.core.ValueProvider': 'Generator'
  }

  transform(value: string): string {
    if (this.typesMap[value]) {
      return this.typesMap[value];
    }
    return this.getClassName(value);
  }

  getClassName(name: string) {
    return name.replace(/.*\.(.*)$/, '$1');
  }
}

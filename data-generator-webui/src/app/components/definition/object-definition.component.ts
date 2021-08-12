import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { ObjectDefinition } from './object-definition';

@Component({
  selector: 'object-definition',
  templateUrl: './object-definition.component.html',
  styleUrls: ['./object-definition.component.scss']
})
export class ObjectDefinitionComponent implements OnInit {
  @Output("onChange") onChange = new EventEmitter<ObjectDefinition>();
  definition: ObjectDefinition;

  constructor() {
    this.definition = new ObjectDefinition();
  }

  ngOnInit(): void {
    this.addField("id", "IntAutoIncrement()");
  }

  addField(name: string, definition: string) {
    if (this.definition.fieldExists(name)) {
      return;
    }
    this.definition.addField({ name, definition })
    this.onChange.emit(this.definition);
  }
}

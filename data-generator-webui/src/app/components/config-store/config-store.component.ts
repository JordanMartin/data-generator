import {Component} from '@angular/core';
import {StorageService} from '../../services/storage-service';
import {ModelConfig} from '../../services/model-config';
import {OutputConfig} from "../output-config/output-config";

@Component({
  selector: 'app-config-store',
  templateUrl: './config-store.component.html',
  styleUrls: ['./config-store.component.scss']
})
export class ConfigStoreComponent {

  save_name: string = '';
  store!: { [key: string]: ModelConfig };

  constructor(private storage: StorageService) {
    this.store = this.getStore();
    // this.storage.definition.subscribe(() => this.save('<non sauvegardé>'));
    // this.storage.output_config.subscribe(() => this.save('<non sauvegardé>'));
  }

  get save_count(): number {
    return Object.keys(this.store).length;
  }

  saveCurrent(name: string) {
    if (!name) {
      return;
    }
    const store = this.getStore();
    store[name] = {
      definition: this.storage.definition.getValue(),
      output_config: this.storage.output_config.getValue()
    };
    window.localStorage.setItem('store', JSON.stringify(store));
    this.store = store;
  }

  getStore(): { [key: string]: ModelConfig } {
    const store = window.localStorage.getItem('store');
    if (store == null) {
      return {};
    }
    return JSON.parse(store);
  }

  delete() {
    if (!this.save_name) {
      return;
    }
    delete this.store[this.save_name];
    window.localStorage.setItem('store', JSON.stringify(this.store));
    this.save_name = '';
  }

  load(name: string) {
    const modelConfig: ModelConfig = this.store[name];
    this.storage.output_config_load.next(modelConfig.output_config);
    this.storage.definition_load.next(modelConfig.definition);
    this.save_name = name;
  }

  loadExamples() {
    const examples = {
      "[Exemple] Template": {
        "definition": "template:\n  id: Increment()\n  firstname: Faker(\"Name.firstName\")\n  lastname: Faker(\"Name.lastName\")\n  age: Integer(0, 100)",
        "output_config": {
          "count": 5,
          "format": "template",
          "template": "#set ($title = \"Liste de personne aléatoire\")\r\n$title\r\n===================\r\n#foreach($data in $list)\r\nid=$data.id\r\nnom=$data.firstname $data.lastname\r\nage=$data.age\r\n#if($data.age >= 18)\r\nmajeur=oui\r\n#else\r\nmajeur=no\r\n#end\r\n---------\r\n#end"
        } as OutputConfig
      },
      "[Exemple] Références": {
        "definition": "references:\n  car:\n    brand: Enum([\"Peugeot\", \"Renault\"])\n    number_plate: Regex(\"[A-Z]{2}-[0-9]{3}-[A-Z]{2}\")\n\ntemplate:\n  id: Increment()\n  firstname: Faker(\"Name.firstName\")\n  lastname: Faker(\"Name.lastName\")\n  age: Integer(0, 100)\n  cars: Repeat($car, Integer(0,2))",
        "output_config": {
          "count": 5,
          "format": "json",
          "pretty": true,
        } as OutputConfig
      },
      "[Exemple] SQL": {
        "definition": "template:\n  id: Increment()\n  uuid: UUID()\n  firstname: Faker(\"Name.firstName\")\n  lastname: Faker(\"Name.lastName\")\n  age: Integer(0, 100)\n  fullname: $(\"${firstname} ${lastname}\")",
        "output_config": {
          "include_null": true,
          "count": 5,
          "format": "sql",
          "table_name": "person",
        } as OutputConfig
      },
      "[Exemple] Dates": {
        "definition": "template:\n  start: FormatDate(Date(\"2000-01-01\", \"2000-12-31\"), \"yyyy-MM-dd\")\n  end: FormatDate(Date(\"2001-01-01\", \"2001-12-31\"), \"yyyy-MM-dd\")\n  generatedAt: FormatDate(Now(), \"yyyy-MM-dd hh:mm:ss\")",
        "output_config": {
          "count": 5,
          "format": "json",
          "pretty": true,
        } as OutputConfig
      },
      "[Exemple] Enumérations": {
        "definition": "template:\n  id: Increment()\n  uuid: UUID()\n  firstname: Faker(\"Name.firstName\")\n  lastname: Faker(\"Name.lastName\")\n  age: Integer(0, 100)\n  fullname: $(\"${firstname} ${lastname}\")\n  blood_group: Enum([EnumWeight(\"O-\", 6), EnumWeight(\"O+\", 36), EnumWeight(\"A-\", 7), EnumWeight(\"A+\", 37), EnumWeight(\"B-\", 1), EnumWeight(\"B+\", 9), EnumWeight(\"AB-\", 1), EnumWeight(\"AB+\", 3)])\n  universal_donor: If(\"blood_group\", \"=\", \"O-\")",
        "output_config": {
          "count": 10,
          "format": "json",
          "pretty": true,
        } as OutputConfig
      },
      "[Exemple] Séquences": {
        "definition": "template:\n  seq: Sequence([\"a\", \"b\", \"c\"])",
        "output_config": {
          "count": 10,
          "format": "json",
          "pretty": true,
        } as OutputConfig
      },
      "[Exemple] Sous objets": {
        "definition": "template:\n  array: List([UUID(), Constant(42), Integer(0, 100)])\n  array2: \n    - UUID()\n    - Constant(42)\n    - Integer(0, 100)\n  array3: Repeat(Round(Double(0, 100), 2), 3)",
        "output_config": {
          "count": 10,
          "format": "json",
          "pretty": true,
        } as OutputConfig
      }
    };

    for (let name in examples) {
      // @ts-ignore
      this.store[name] = examples[name];
    }
    window.localStorage.setItem('store', JSON.stringify(this.store));
  }
}

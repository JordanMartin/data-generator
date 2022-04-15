import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {OutputConfig} from "../output-config/output-config";
import {Favorite} from "../../services/favorite";
import {StorageService} from "../../services/storage-service";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-manage-favorites',
  templateUrl: './manage-favorites.component.html',
  styleUrls: ['./manage-favorites.component.scss']
})
export class ManageFavoritesComponent implements OnInit {

  favorites: Favorite[];

  constructor(private storage: StorageService,
              private snackBar: MatSnackBar,
              @Inject(MAT_DIALOG_DATA) data: { favorites: Favorite[] }) {
    this.favorites = data.favorites;
  }

  ngOnInit(): void {
  }

  loadExamples() {
    const examples: Favorite[] = [
      {
        name: "[Exemple] Template",
        definition: "template:\n  id: Increment()\n  firstname: Faker(\"Name.firstName\")\n  lastname: Faker(\"Name.lastName\")\n  age: Integer(0, 100)",
        output_config: {
          count: 5,
          format: "template",
          template: "#set ($title = \"Liste de personne aléatoire\")\r\n$title\r\n===================\r\n#foreach($data in $list)\r\nid=$data.id\r\nnom=$data.firstname $data.lastname\r\nage=$data.age\r\n#if($data.age >= 18)\r\nmajeur=oui\r\n#else\r\nmajeur=no\r\n#end\r\n---------\r\n#end"
        }
      },
      {
        name: "[Exemple] Références",
        definition: "references:\n  car:\n    brand: Enum([\"Peugeot\", \"Renault\"])\n    number_plate: Regex(\"[A-Z]{2}-[0-9]{3}-[A-Z]{2}\")\n\ntemplate:\n  id: Increment()\n  firstname: Faker(\"Name.firstName\")\n  lastname: Faker(\"Name.lastName\")\n  age: Integer(0, 100)\n  cars: Repeat($car, Integer(0,2))",
        output_config: {
          "count": 5,
          "format": "json",
          "pretty": true,
        }
      },
      {
        name: "[Exemple] SQL",
        definition: "template:\n  id: Increment()\n  uuid: UUID()\n  firstname: Faker(\"Name.firstName\")\n  lastname: Faker(\"Name.lastName\")\n  age: Integer(0, 100)\n  fullname: $(\"${firstname} ${lastname}\")",
        output_config: {
          include_null: true,
          count: 5,
          format: "sql",
          table_name: "person",
        }
      },
      {
        name: "[Exemple] Dates",
        definition: "template:\n  start: FormatDate(Date(\"2000-01-01\", \"2000-12-31\"), \"yyyy-MM-dd\")\n  end: FormatDate(Date(\"2001-01-01\", \"2001-12-31\"), \"yyyy-MM-dd\")\n  generatedAt: FormatDate(Now(), \"yyyy-MM-dd hh:mm:ss\")",
        output_config: {
          count: 5,
          format: "json",
          pretty: true,
        }
      },
      {
        name: "[Exemple] Enumérations",
        definition: "template:\n  id: Increment()\n  uuid: UUID()\n  firstname: Faker(\"Name.firstName\")\n  lastname: Faker(\"Name.lastName\")\n  age: Integer(0, 100)\n  fullname: $(\"${firstname} ${lastname}\")\n  blood_group: Enum([EnumWeight(\"O-\", 6), EnumWeight(\"O+\", 36), EnumWeight(\"A-\", 7), EnumWeight(\"A+\", 37), EnumWeight(\"B-\", 1), EnumWeight(\"B+\", 9), EnumWeight(\"AB-\", 1), EnumWeight(\"AB+\", 3)])\n  universal_donor: If(\"blood_group\", \"=\", \"O-\")",
        output_config: {
          count: 10,
          format: "json",
          pretty: true,
        }
      },
      {
        name: "[Exemple] Séquences",
        definition: "template:\n  seq: Sequence([\"a\", \"b\", \"c\"])",
        output_config: {
          count: 10,
          format: "json",
          pretty: true,
        } as OutputConfig
      },
      {
        name: "[Exemple] Sous objets",
        definition: "template:\n  array: List([UUID(), Constant(42), Integer(0, 100)])\n  array2: \n    - UUID()\n    - Constant(42)\n    - Integer(0, 100)\n  array3: Repeat(Round(Double(0, 100), 2), 3)",
        output_config: {
          count: 10,
          format: "json",
          pretty: true,
        }
      }
    ];

    this.storage.saveFavorites(examples);
    this.favorites = this.storage.loadFavorites();
  }

  exportFavorites() {
    const now = new Date();
    const format = (n: number) => n < 9 ? '0' + n : n;
    const date = `${now.getFullYear()}-${format(now.getMonth())}-${format(now.getDate())}`;
    const time = `${format(now.getHours())}-${format(now.getMinutes())}-${format(now.getSeconds())}`;
    this.downloadData(`data-generator-favoris-${date}_${time}.json`, this.favorites)
  }

  downloadData(filename: string, data: any) {
    const a = document.createElement('a') as HTMLAnchorElement;
    a.style.display = 'none';
    document.body.appendChild(a);
    const json = JSON.stringify(data, null, 2);
    const blob = new Blob([json], {type: 'octet/stream'});
    const url = window.URL.createObjectURL(blob);
    a.href = url;
    a.download = filename;
    a.click();
    window.URL.revokeObjectURL(url);
  };

  removeFavorite(favorite: Favorite) {
    this.storage.removeFavorite(favorite.name);
    this.favorites = this.storage.loadFavorites();
  }

  async importFavorites(file: File) {
    let favorites: Favorite[];
    try {
      const json = await file.text();
      favorites = JSON.parse(json);
      favorites = favorites.filter(favorite => !!favorite.name);
    } catch (e) {
      this.snackBar.open('❌ Favoris non importé. Le fichier de sauvegarde est invalide', 'Fermer');
      return;
    }
    this.storage.saveFavorites(favorites);
    this.favorites = this.storage.loadFavorites();
    this.snackBar.open('✔ ' + favorites.length
      + ' favoris importé' + (favorites.length > 1 ? 's' : ''), 'Ok');
  }
}

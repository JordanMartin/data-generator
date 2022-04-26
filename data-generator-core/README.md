# data-generator-core

> Module core contenant l'ensemble des générateurs et formats de sortie

  * [Fichier de définition YAML](#fichier-de-d-finition-yaml)
  * [Composition de générateur](#composition-de-g-n-rateur)
  * [Références et expressions](#r-f-rences-et-expressions)
- [Utilisation programmatique](#utilisation-programmatique)
  * [Définition d'un générateur](#d-finition-d-un-g-n-rateur)
    + [Par Héritage avec `ObjectBuilder`](#par-h-ritage-avec--objectbuilder-)
  * [Génération](#g-n-ration)
    + [Par instanciation de `ObjectProvider`](#par-instanciation-de--objectprovider-)
  * [Formats de sortie](#formats-de-sortie)

<small><i><a href='http://ecotrust-canada.github.io/markdown-toc/'>Table of contents generated with markdown-toc</a></i></small>


### Fichier de définition YAML

La syntaxe du fichier de définition est la suviante : 

```yaml
references:
  <ref1>: <declaration_generateur>
  <ref2>: <declaration_generateur>
  ...

template:
  <champ1>: <declaration_generation>
  <champ2>: <declaration_generation>
```

La partie `references` permet de déclarer un générateur et de l'utiliser dans la déclaration d'un autre champ. 3 syntaxes sont disponibles : 
- `$<ref>`: Utilise le générateur `ref` pour générer une valeur
- `$$<ref>`: Utilise le générateur `ref` de façon fixe. Lors de la génération d'un objet. Le généraeur `ref` sera alors appelé une et une fois par objet  pour générer une valeur 
- `$("expression avec reference ${<ref>}")`: Retourne une chaine de caractère en substituant ${<ref>} la valeur généré par `ref`.

*Exemple avec references: definition.yml*
```yaml
references:
  num: Integer(0, 10)
template:
  ref: $num
  ref2: $num
  ref_fixe: $$num
  ref_fixe2: $$num
  expression: '$("Numéro: ${num}")'
```

*Resultat pour 3 objets générés*
```json
[
  {
    "ref": 1,
    "ref2": 5,
    "ref_fixe": 8,
    "ref_fixe2": 8,
    "expression": "Numéro: 4"
   },
   {
    "ref": 7,
    "ref2": 1,
    "ref_fixe": 3,
    "ref_fixe2": 3,
    "expression": "Numéro: 4"
   },
   {
    "ref": 4,
    "ref2": 3,
    "ref_fixe": 2,
    "ref_fixe2": 2,
    "expression": "Numéro: 8"
   }
]
```

> Cette syntaxe, permet également de faire référence à un champ définit dans la partie `template`.

*Exemple complet: definition.yml*

```yaml
references:
  firstname: Faker("Name.firstName")
  lastname: Faker("Name.lastName")
  gen_date: Idempotent(FormatDate(Now(), "yyyy-MM-dd HH:mm:ss.SSS"))
  id: UUID()
  item:
    parent_id: $$id
    horodatage: $gen_date
    expression: $("${firstname} ${lastname}")

template:
  id: $$id
  num: Increment()
  random: Integer(100, 1000)
  horodatage: $gen_date
  items: Repeat($item, 2)
  active: Boolean()
  type: Enum(["A", "B", "C"])
  group: Enum([EnumWeight("A", 10), EnumWeight("B", 90)])
  array:
    - Round(Double(0, 10), 2)
    - Integer(10, 100)
```

*Résultat au format JSON*

```json
[
  {
    "id": "5642e11c-d0fb-44f3-a5ad-2eb36b08071a",
    "num": 0,
    "random": 963,
    "horodatage": "2021-07-09 02:59:55.310",
    "items": [
      {
        "parent_id": "5642e11c-d0fb-44f3-a5ad-2eb36b08071a",
        "horodatage": "2021-07-09 02:59:55.310",
        "expression": "Louna Lemaire"
      },
      {
        "parent_id": "5642e11c-d0fb-44f3-a5ad-2eb36b08071a",
        "horodatage": "2021-07-09 02:59:55.310",
        "expression": "Lola Da silva"
      }
    ],
    "active": true,
    "type": "C",
    "group": "B",
    "array": [
      8.3,
      18
    ]
  },
  {
    "id": "02b03213-b67f-4afe-8824-a3d961cf4bdc",
    "num": 1,
    "random": 341,
    "horodatage": "2021-07-09 02:59:55.310",
    "items": [
      {
        "parent_id": "02b03213-b67f-4afe-8824-a3d961cf4bdc",
        "horodatage": "2021-07-09 02:59:55.310",
        "expression": "Maëlys Mercier"
      },
      {
        "parent_id": "02b03213-b67f-4afe-8824-a3d961cf4bdc",
        "horodatage": "2021-07-09 02:59:55.310",
        "expression": "Kylian Robin"
      }
    ],
    "active": false,
    "type": "B",
    "group": "B",
    "array": [
      6.63,
      23
    ]
  },
  {
    "id": "3bb6133a-7114-4bdb-88d0-5144aef94401",
    "num": 2,
    "random": 430,
    "horodatage": "2021-07-09 02:59:55.310",
    "items": [
      {
        "parent_id": "3bb6133a-7114-4bdb-88d0-5144aef94401",
        "horodatage": "2021-07-09 02:59:55.310",
        "expression": "Romane Olivier"
      },
      {
        "parent_id": "3bb6133a-7114-4bdb-88d0-5144aef94401",
        "horodatage": "2021-07-09 02:59:55.310",
        "expression": "Yanis Lefèvre"
      }
    ],
    "active": false,
    "type": "C",
    "group": "B",
    "array": [
      7.31,
      54
    ]
  }
]
```

## Générateurs

La liste et documentation des générateurs sont disponible via la webui.

### Composition de générateur

todo: A commenter

### Références et expressions

todo: A commenter

## Utilisation programmatique
### Définition d'un générateur

#### Par Héritage avec `ObjectBuilder`

La définition peut se faire par héritage de la classe `ObjectBuilder`. 
La classe `ObjectBuilder` contient des méthodes pour créer facilement un générateur :

```java
public class SimpleObject extends ObjectBuilder {
  @Override
  public void configure() {
    providerRef("randomInt", randomInt(0, 10));

    field("randomFromListWithWeight", randomFromList(enumWeight("a", 1), enumWeight("b", 9)));
    field("list", list(randomInt(0, 100), increment()));
    field("randomUUID", randomUUID());
    field("randomFromRegex", randomFromRegex("P[A-Z]{3}[0-9]{5}", 5));
    field("firstname", ctx -> faker.name().firstName());
    field("custom", ctx -> {
      // votre générateur
      return "custom_data";
    });
    field("expression", expression("${firstname} ${lastname}"));
    field("reference", reference("randomInt"));
  }
}
```

### Génération
```java
public class Test {
    public static void main(String[] args) throws IOException {
        SimpleObjectBuilder generator = new SimpleObjectBuilder();
        ObjectOutput.from(generator)
                .toJson().setPretty(true)
                .writeMany(System.out, 2);
    }
}
```

*Résultat au format JSON*

```json
[
  {
    "randomFromList with weight": "b",
    "list": [
      22,
      0
    ],
    "randomUUID": "73b7a52b-1394-4eba-8249-6ab2d90c4616",
    "randomFromRegex": "PHJV99665",
    "firstname": "Valentin",
    "custom": "custom_data",
    "expression": "Valentin Bertrand",
    "reference": 5
  },
  {
    "randomFromList with weight": "b",
    "list": [
      83,
      1
    ],
    "randomUUID": "34d6ad81-a599-4ab3-92e2-4e553e0f64f7",
    "randomFromRegex": "PWAT45684",
    "firstname": "Clément",
    "custom": "custom_data",
    "expression": "Clément Pierre",
    "reference": 2
  }
]
```

#### Par instanciation de `ObjectProvider`

Il est également possible de déclarer dynamiquement un générateur en créant une instance de `ObjectProvider` :

```java
ObjectProvider provider = new ObjectProvider()
      .field("id", new IntAutoIncrement())
      .field("name" () -> "Alice");
```

### Formats de sortie

*Exemple*

```java
ObjectProvider provider = new ObjectProvider()
        .field("id", new IntAutoIncrement())
        .field("name", new Sample("Name.firstName"));
```

**JSON**

```java
ObjectOutput.from(provider)
        .toJson().setPretty(true)
        .writeMany(System.out, 2);
```

```json
[ {
  "id" : 0,
  "name" : "Clara"
}, {
  "id" : 1,
  "name" : "Noa"
} ]
```

**XML**

```java
ObjectOutput.from(provider)
        .toXml().setPretty(true)
        .writeMany(System.out, 2);
```

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<data>
    <object>
        <id>1</id>
        <name>Clara</name>
    </object>
    <object>
        <id>2</id>
        <name>Noa</name>
    </object>
</data>
```

**CSV**

```java     
ObjectOutput.from(provider)
        .toCsv()
        .writeMany(System.out, 2);
```

```csv
id;name
1;Clara
1;Noa
```

**SQL**

```java
ObjectOutput.from(provider)
        .toSQL()
        .writeMany(System.out, 2);
```

```sql
INSERT INTO object(id,name) VALUE(1,'Clara');
INSERT INTO object(id,name) VALUE(2,'Noa');
```

**YAML**

```java
ObjectOutput.from(provider)
        .toYaml().setPretty(true)
        .writeMany(System.out, 2);
```

```yaml
- id: 1
  name: Clara
- id: 2
  name: Noa
```

## Créer un nouveau générateur
Pour définir votre propre générateur, basez vous sur [ceux existants](https://github.com/JordanMartin/data-generator/tree/main/data-generator-core/src/main/java/io/github/jordanmartin/datagenerator/provider)

Par défaut le module core enregistre tous les générateurs présents dans le package `io.github.datagenerator.plugins`.
Ce package peut être modifié en définissant la varialbe d'environnement `PROVIDER_PLUGINS_SEARCH_PACKAGE`.

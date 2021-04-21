# Data generator

Générateur de données à partir de valeurs aléatoires ou personnalisées.

## Générateurs

### `Constant(<valeur>)`

Retourne toujours la même valeur

### `RandomUUID()`

Génère un UUID

### `RandomInt(int min, int max)`

Retourne un entier aléatoire dans un interval. Si les paramètres `min` et `max` ne sont pas spécifié, l'interval
est `[0, Integer.MAX_VALUE]`

### `SequenceFromList(Object element1, Object element2, ...)`

Retourne séquentiellement les éléments spécifiés en paramètre.

*Exemple* : `SequenceFromList("a", "b", "c")`

*Résultat pour 5 éléments*: `["a", "b", "c", "a", "b"]`

### `RandomDouble(double min, double max)`

Retourne un double aléatoire dans un interval. Si les paramètres `min` et `max` ne sont pas spécifié, l'interval
est `[0, Double.MAX_VALUE]`

### `Sample(String expression[, String locale])`

Génère une fausse donnée selon une expression.

- `String expression` : une expression au format [Faker](https://github.com/DiUS/java-faker). La liste des expression
  est [disponible ici](FakerExpression.md) ou via [cette démo](https://java-faker.herokuapp.com/?locale=FR).
  L'expression peut être composée avec la notation suivante.
  `Sample("#{TYPE} #{TYPE}")`. *Exemple: `Sample("#{Name.firstName} #{Name.lastName}")`*
- `String locale (optionnel)` : La locale a utilisé pour les données générés. *Exemple: "fr", "us", "it", "de", ...*

### `Round(<generateur>, <nombre_decimal>)`

TODO

### `RandomDate("<date_min>", "<date_max>")`

- `<date_min> (format: yyyy-MM-dd)` : Borne inférieur de la date générée
- `<date_max> (format: yyyy-MM-dd)` : Borne supérieur de la date générée

Génère une date aléatoire dans un interval.

### `CurrentDate()`

Retourne la date courante

### `RandomFromRegex("<regex>"[, <elem_count>])`

- `<regex>` : Une regex déterminant la valeur à générer
- `<elem_count> (optionnel)` : Nombre de valeurs à générer à partir de la regex

Retourne une valeur aléatoire basée sur une regex. Le second paramètre optionnel permet de calculer `<elem_count>`
éléments à parti de la regex et de retourner un élément aléatoire parmis cette liste.

**Exemple** :

`RandomFromRegex("[A-Z]{2}-[0-9]{3}-[A-Z]{2}", 3)` permet de générer une donnée parmis la liste suivante

```
[ LD-730-KW,  IJ-100-MB,  VM-418-BM ]
```

### `RandomFromList(<element1>, <element2>, ...)`

TODO

### `IntAutoIncrement([<start>, <step>, <max>])`

TODO

### `Idempotent`

TODO

### `ListOf`

TODO

### `AsString`

TODO

### `FormatDate`

TODO

### `ListByRepeat`

TODO

### `Expression`

TODO

### `Reference`

TODO

### `FixedReference`

TODO

### Composition de générateur

TODO

### Références et expressions

TODO

## Définition par programmation

```java
public class SimpleObject extends ObjectBuilder {
    @Override
    public void configure() {
        providerRef("randomInt", randomInt(0, 10));

        field("randomFromListWithWeight", randomFromList(itemWeight("a", 1), itemWeight("b", 9)));
        field("list", list(randomInt(0, 100), increment()));
        field("randomUUID", randomUUID());
        field("randomFromRegex", randomFromRegex("P[A-Z]{3}[0-9]{5}", 5));
        field("firstname", ctx -> faker.name().firstName());
        field("lastname", ctx -> faker.name().lastName());
        field("expression", expression("${firstname} ${lastname}"));
        field("reference", reference("randomInt"));
    }
}
```

```java
public class Test {
    public static void main(String[] args) throws IOException {
        SimpleObjectBuilder generator = new SimpleObjectBuilder();
        new JsonWriter(true).writeMany(System.out, generator.getStream(2));
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
    "lastname": "Bertrand",
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
    "lastname": "Pierre",
    "expression": "Clément Pierre",
    "reference": 2
  }
]
```

## Définition par fichier YAML

*Exemple: definition.yml*

```yaml
references:
  firstname: Sample("Name.firstName")
  lastname: Sample("Name.lastName")
  gen_date: Idempotent(FormatDate(CurrentDate(), "yyyy-MM-dd HH:mm:ss.SSS"))
  id: RandomUUID()
  item:
    parent_id: $$id
    horodatage: $gen_date
    exp: $("${firstname} ${lastname}")

template:
  id: $$id
  num: IntAutoIncrement()
  random: RandomInt(100, 1000)
  horodatage: $gen_date
  items: ListByRepeat($item, 2)
```

*Résultat au format JSON*

```json
[
  {
    "id": "1d712947-6458-41e4-b152-46e458e642d0",
    "num": 0,
    "random": 443,
    "horodatage": "2021-04-20 09:17:27.166",
    "items": [
      {
        "parent_id": "1d712947-6458-41e4-b152-46e458e642d0",
        "horodatage": "2021-04-20 09:17:27.166",
        "exp": "Enzo Thomas"
      },
      {
        "parent_id": "1d712947-6458-41e4-b152-46e458e642d0",
        "horodatage": "2021-04-20 09:17:27.166",
        "exp": "Arthur Pons"
      }
    ]
  },
  {
    "id": "11e52ffb-48e7-4b54-997a-7395344d0ba5",
    "num": 1,
    "random": 143,
    "horodatage": "2021-04-20 09:17:27.166",
    "items": [
      {
        "parent_id": "11e52ffb-48e7-4b54-997a-7395344d0ba5",
        "horodatage": "2021-04-20 09:17:27.166",
        "exp": "Manon Martin"
      },
      {
        "parent_id": "11e52ffb-48e7-4b54-997a-7395344d0ba5",
        "horodatage": "2021-04-20 09:17:27.166",
        "exp": "Nicolas Perrot"
      }
    ]
  },
  {
    "id": "3546832d-9879-4cda-af73-96ebec106829",
    "num": 2,
    "random": 960,
    "horodatage": "2021-04-20 09:17:27.166",
    "items": [
      {
        "parent_id": "3546832d-9879-4cda-af73-96ebec106829",
        "horodatage": "2021-04-20 09:17:27.166",
        "exp": "Chloé Mercier"
      },
      {
        "parent_id": "3546832d-9879-4cda-af73-96ebec106829",
        "horodatage": "2021-04-20 09:17:27.166",
        "exp": "Maeva Ménard"
      }
    ]
  }
]
```

## Génération

*Exemple*

```java
ObjectProvider provider=new ObjectProvider()
        .field("id", new IntAutoIncrement())
        .field("name", new Sample("Name.firstName"));
```

**JSON**

```java
ObjectOuput.from(provider)
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
ObjectOuput.from(provider)
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
ObjectOuput.from(provider)
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
ObjectOuput.from(provider)
        .toSQL()
        .writeMany(System.out, 2);
```

```sql
INSERT INTO object(id,name) VALUE(1,'Clara');
INSERT INTO object(id,name) VALUE(2,'Noa');
```

**YAML**

```java
ObjectOuput.from(provider)
        .toYaml().setPretty(true)
        .writeMany(System.out, 2);
```

```yaml
- id: 1
  name: Clara
- id: 2
  name: Noa
```
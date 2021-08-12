# data-generator-core

> Module core contenant l'ensemble des générateurs et formats de sortie

  * [Fichier de définition YAML](#fichier-de-d-finition-yaml)
- [Générateurs](#g-n-rateurs)
  * [`Constant(<valeur>)`](#-constant--valeur---)
  * [`RandomUUID()`](#-randomuuid---)
  * [`RandomBoolean(double percentageOfTrue)`](#-randomboolean-double-percentageoftrue--)
  * [`RandomInt(int min, int max)`](#-randomint-int-min--int-max--)
  * [`SequenceFromList(Object element1, Object element2, ...)`](#-sequencefromlist-object-element1--object-element2----)
  * [`RandomDouble(double min, double max)`](#-randomdouble-double-min--double-max--)
  * [`Sample(String expression[, String locale])`](#-sample-string-expression---string-locale---)
  * [`Round(<generateur>, <nombre_decimal>, <mode_arrondi>)`](#-round--generateur----nombre-decimal----mode-arrondi---)
  * [`RandomDate("<date_min>", "<date_max>")`](#-randomdate---date-min------date-max----)
  * [`CurrentDate()`](#-currentdate---)
  * [`RandomFromRegex("<regex>"[, <elem_count>])`](#-randomfromregex---regex------elem-count----)
  * [`RandomFromList(<element1>, <element2>, ...)`](#-randomfromlist--element1----element2-----)
  * [`IntAutoIncrement([<start>, <step>, <max>])`](#-intautoincrement---start----step----max----)
  * [`Idempotent(<generateur>)`](#-idempotent--generateur---)
  * [`ListOf`](#-listof-)
  * [`AsString`](#-asstring-)
  * [`FormatDate`](#-formatdate-)
  * [`ListByRepeat`](#-listbyrepeat-)
  * [`Expression`](#-expression-)
  * [`Reference`](#-reference-)
  * [`FixedReference`](#-fixedreference-)
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
  num: RandomInt(0, 10)
template:
  ref: $num
  ref2: $num
  ref_fixe: $$num
  ref_fixe2: $$num
  expression: $("Numéro: ${num}")
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
  firstname: Sample("Name.firstName")
  lastname: Sample("Name.lastName")
  gen_date: Idempotent(FormatDate(CurrentDate(), "yyyy-MM-dd HH:mm:ss.SSS"))
  id: RandomUUID()
  item:
    parent_id: $$id
    horodatage: $gen_date
    expression: $("${firstname} ${lastname}")

template:
  id: $$id
  num: IntAutoIncrement()
  random: RandomInt(100, 1000)
  horodatage: $gen_date
  items: ListByRepeat($item, 2)
  active: RandomBoolean()
  type: RandomFromList(["A", "B", "C"])
  group: RandomFromList([ItemWeight("A", 10), ItemWeight("B", 90)])
  array:
    - Round(RandomDouble(0, 10), 2)
    - RandomInt(10, 100)
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

### `Constant(<valeur>)`

Retourne toujours la même valeur

### `RandomUUID()`

Génère un UUID

**Exemple** :

- `RandomUUID()` => `"fd768fbf-6e86-4e46-a654-12b5e8106d19"`

### `RandomBoolean(double percentageOfTrue)`

Retourne un booléen aléatoire. Si le paramètre `percentageOfTrue` n'est pas précisé il y aura autant de `true` que
de `false`

- `percentageOfTrue`: décimale entre 0 et 1 représentant le pourcentage de valeur `true` généré.
  - Exemples:
  - 0 => que des `false`
  - 0.4 => 40% de `true`
  - 1 => que des `true`

### `RandomInt(int min, int max)`

Retourne un entier aléatoire dans un interval. Si les paramètres `min` et `max` ne sont pas spécifié, l'interval
est `[0, Integer.MAX_VALUE]`

### `SequenceFromList(Object element1, Object element2, ...)`

Retourne séquentiellement les éléments spécifiés en paramètre.

*Exemple* : `SequenceFromList("a", "b", "c")`

*Résultat pour 5 générations successives*: `["a", "b", "c", "a", "b"]`

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

**Exemple**:

- `Sample("Name.fullName")` => "Mme Lina Royer"
- `Sample("Name.firstName")` => "Paul"
- `Sample("Internet.emailAddress")` => "victor.david@hotmail.fr"
- `Sample("#{Name.fullName} (#{Address.cityName})")` => "Mme Lina Royer (Paris)"

### `Round(<generateur>, <nombre_decimal>, <mode_arrondi>)`

Arrondi un nombre décimal

- `<generateur>`: Un généréteur de nombre décimale. Ex: `RandomDouble()`
- `<nombre_decimal>`: Nombre de décimale à garder après la virgule
- `<mode_arrondi> (facultatif: défaut = "UP") `: "UP", "DOWN", "CEILING", "FLOOR", "HALF_UP", "HALF_DOWN", "HALF_EVEN"

**Exemple**:

- `Round(Constant(1.123), 2, "UP")` => 1.13

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

Retourne une valeur aléatoire parmis la liste d'élément. Si l'élément est un `ItemWeight`, alors le point permet de
moduler le taux d'apparition d'un élément.

**Exemple**:

- `RandomFromList(["A", "B", "C"])` : Retourne A, B ou C équitablement
- `RandomFromList([ItemWeight("A", 10), ItemWeight("B", 70), ItemWeight("C", 20)])` : Retounre 10% de A, 70% de B et 20%
  de C

### `IntAutoIncrement([<start>, <step>, <max>])`

Retourne un nombre entier incrémenté à chaque génération

- `<start>`: Valeur initiale
- `<step>`: Quantité incrémenté à chaque génération
- `<max>`: Valeur maximum. Si la valeur courante dépasse le max, l'increment retourne à `<start>`

### `Idempotent(<generateur>)`

Permet de renvoyer toujours la même valeur.
**Exemple**:

- `Idempotent(FormatDate(RandomDate(), "yyy-MM-dd"))`: Génère une date random et retourne toujours la même date

### `ListOf`

todo: A commenter

### `AsString`

todo: A commenter

### `FormatDate`

todo: A commenter

### `ListByRepeat`

todo: A commenter

### `Expression`

todo: A commenter

### `Reference`

todo: A commenter

### `FixedReference`

todo: A commenter

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

        field("randomFromListWithWeight", randomFromList(itemWeight("a", 1), itemWeight("b", 9)));
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

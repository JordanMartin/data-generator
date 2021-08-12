# data-generator-cli

> Invite de commande pour le générateur de données

## Prérequis

- La dernière version compilée est [disponible ici](https://github.com/JordanMartin/data-generator/releases/latest)
- Requière Java >= 11

### Usage

```
usage: java -jar data-generator-x.y.z.jar
-c,--count <arg>        Nombre d'objet à générer
-d,--definition <arg>   Fichier de defintion
-f,--format <arg>       Format de sortie : yaml, json, csv, sql, xml
--gzip               Compresse la sortie en GZIP
-o,--out <arg>          Fichier de sortie
--pretty             Active le mode pretty pour la sortie JSON ou YAML
--separator <arg>    Séparateur à utiliser pour le format CSV
--stdout             Utilise la sortie standard
--table-name <arg>   Nom de la table SQL à utilier pour les requêtes insert
```

*Exemple*
```bash
java -jar data-generator-1.0.0-alpha.jar --definition def.yml --count 10 --format json --pretty --stdout
```

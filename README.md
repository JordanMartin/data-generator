# data-generator

> Générateur de données aléatoire au format  json, yaml, xml, sql, csv ou personalisé via template

La génération de données peut se faire via 4 méthodes : 
- Invite de commande (module cli)
- Requête HTTP (endpoints exposé par le module api)
- Interface Web (exposée par le module api)
- Programmatique en java/groovy/kotlin (module core)

# Data generator
[![Build & Tests](https://github.com/JordanMartin/data-generator/actions/workflows/build.yml/badge.svg)](https://github.com/JordanMartin/data-generator/actions/workflows/build.yml)
[![](https://img.shields.io/github/v/release/JordanMartin/data-generator?sort=semver&style=flat&label=Latest%20release&logo=github&color=green)](https://github.com/JordanMartin/data-generator/releases/latest)

## Modules
- [`data-generator-core`](data-generator-core) : module core contenant les générateurs et formats de sortie
- [`data-generator-api`](data-generator-api) : Serveur http
- [`data-generator-cli`](data-generator-cli) : Invite de commande
- [`data-generator-webui`](data-generator-webui) : Interface Web

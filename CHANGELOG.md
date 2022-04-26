# 1.9.0

## webui:

- Gestionnaire de favoris amélioré
- Import/export des favoris
- Affichage si nouvelle version disponible

## core

- Fix le port HTTP => 8050 pour conserver le localstorage
- Customisation du package de recherche pour les plugins provider

# 1.8.0

## webui:

- Gestionnaire de sauvegardes
- Affichage du numéro de version
- Icone dans la zone de notification pour ouvrir ou fermer l'app

## core

- Nouveaux générateurs: Or, And, If

# 1.7.2

## webui:

- FIX : ouverture automatique du navigateur + port random par défaut

# 1.7.1

## core

- ObjectBuilder: possibilité de ne pas appeler directement la méthode `configure` lors de la construction d'un objet fi

# 1.7.0

- Nouveau générateur: Not
- Générateur RandomFromRegex: possibilité de générer un cache avec des données random ou séquentielles
- Ouverture du navigateur par defaut au démarrage de l'api
- Définition d'un port random disponible au démarrage de l'api (peut être forcé par -Dquarkus.http.port)
- Gestion des booléens dans les fichiers de définition YAML

# 1.6.0

- Génération de documentation automatique via la WebUI
- Suppression du module cli

# 1.5.0

* Création de la WebUI 

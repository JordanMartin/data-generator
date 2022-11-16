# Data generator

[![Build & Tests](https://github.com/JordanMartin/data-generator/actions/workflows/build.yml/badge.svg)](https://github.com/JordanMartin/data-generator/actions/workflows/build.yml)
[![](https://img.shields.io/github/v/release/JordanMartin/data-generator?sort=semver&style=flat&label=Latest%20release&logo=github&color=green)](https://github.com/JordanMartin/data-generator/releases/latest)

> Random Data generator in json, yaml, xml, sql, csv format or customized by template


## Getting started

### Generate data using WebUI (module web-ui exposed by the api module)
- Download the latest release
- Execute `data-generator-api-x.x.x-runner.jar`
- Try the UI and load samples to start faster

![screenshot](https://user-images.githubusercontent.com/5437374/202072005-48555664-cb1f-4f3c-a8bf-992deb25f1a9.png)


### Generate data programmatically (module core)
- Include the latest `data-generator-core-x.x.x.jar` module
- Use the module in your app

```java
// Define your model
ObjectProvider provider = new ObjectProvider()
        .field("id", new IntAutoIncrement())
        .field("name", new Sample("Name.firstName"));

// Output JSON to stdout
ObjectOutput.from(provider)
        .toJson().setPretty(true)
        .writeMany(System.out, 2);
```

See [`data-generator-core`](data-generator-core)

## Modules

- [`data-generator-core`](data-generator-core) : core module containing all data providers and output format generator
- [`data-generator-api`](data-generator-api) : http server powered by quarkus
- [`data-generator-webui`](data-generator-webui) : User friendly web ui powered by Angular


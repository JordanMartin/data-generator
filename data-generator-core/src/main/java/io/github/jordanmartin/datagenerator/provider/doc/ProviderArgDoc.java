package io.github.jordanmartin.datagenerator.provider.doc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProviderArgDoc {
    String name;
    String type;
    String description;
    String[] examples;
}

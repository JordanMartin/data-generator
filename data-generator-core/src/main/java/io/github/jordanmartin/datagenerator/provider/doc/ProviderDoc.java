package io.github.jordanmartin.datagenerator.provider.doc;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProviderDoc {
    String name;
    String description;
    String type;
    List<ProviderCtorDoc> constructors = new ArrayList<>();
    String[] examples;
}

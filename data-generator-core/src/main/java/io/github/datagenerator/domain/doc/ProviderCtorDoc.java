package io.github.datagenerator.domain.doc;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProviderCtorDoc {
    String description;
    List<ProviderArgDoc> args = new ArrayList<>();
}

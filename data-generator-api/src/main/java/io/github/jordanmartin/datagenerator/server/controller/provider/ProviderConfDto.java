package io.github.jordanmartin.datagenerator.server.controller.provider;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class ProviderConfDto {
    private String href;
    private String name;
    private String template;
    private String format;
}

package io.github.datagenerator.server.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProviderConfDto {
    private String href;
    private String name;
    private String template;
    private String format;
}

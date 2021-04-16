package fr.jordanmartin.datagenerator.definition;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Definition {

    private Map<String, Object> template;
    private Map<String, Object> references;

}

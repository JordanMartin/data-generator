package io.github.datagenerator.domain.core;

public class FieldNotFoundInContext extends RuntimeException {
    private final String fieldName;

    public FieldNotFoundInContext(String fieldName, ObjectContext ctx) {
        super();
        this.fieldName = fieldName;
    }

    @Override
    public String getMessage() {
        return "The field '" + fieldName + "' doesn't exists in context";
    }
}

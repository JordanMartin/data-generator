package io.github.datagenerator.domain.providers.object;

import io.github.datagenerator.domain.annotation.Provider;
import io.github.datagenerator.domain.annotation.ProviderArg;
import io.github.datagenerator.domain.annotation.ProviderCtor;
import io.github.datagenerator.domain.core.ObjectContext;
import io.github.datagenerator.domain.core.ValueProvider;
import lombok.AllArgsConstructor;

import java.util.Comparator;
import java.util.List;

@Provider(
        name = "If",
        description = "Returns true of false by comparing the value of a field. Note: the fields must have the same data type",
        examples = {
                "If(\"age\", \">=\", 18) => returns true if the field \"age\" is >= 18 otherwise false"
        },
        returns = Boolean.class,
        group = "boolean"
)
public class If implements ValueProvider<Boolean> {

    private final String fieldA;
    private final String operator;
    private final ValueProvider<?> provider;
    public static final List<String> AVAILABLE_OPERATORS = List.of("<", "<=", "=", "!=", ">",  ">=");

    @ProviderCtor
    public If(
            @ProviderArg(description = "Name of the field to compare") String field,
            @ProviderArg(description = "Comparison operator : <, <=, =, !=, >, >=") String operator,
            @ProviderArg(description = "Valeur à comparer") Object value) {
        this(field, operator, ctx -> value);
    }

    @ProviderCtor
    public If(
            @ProviderArg(description = "Name of the field to compare") String field,
            @ProviderArg(description = "Comparison operator : <, <=, =, !=, >, >=") String operator,
            @ProviderArg(description = "Value to compare with") ValueProvider<?> provider) {
        this.fieldA = field;
        this.operator = operator;
        this.provider = provider;

        if (!AVAILABLE_OPERATORS.contains(operator)) {
            throw new InvalidConditionOperator();
        }
    }

    @Override
    public Boolean get(ObjectContext ctx) {
        Object a = getFieldValue(ctx);
        Object b = provider.get(ctx);
        return compare(a, b);
    }

    private Boolean compare(Object a, Object b) {
        Comparator<Object> comparator = getComparator(a, b);
        int compare = comparator.compare(a, b);

        switch (operator) {
            case "=":
                return compare == 0;
            case "!=":
                return compare != 0;
            case ">":
                return compare > 0;
            case ">=":
                return compare >= 0;
            case "<":
                return compare < 0;
            case "<=":
                return compare <= 0;
            default:
                throw new InvalidConditionOperator();
        }
    }

    private Comparator<Object> getComparator(Object a, Object b) {
        if (a == null && b == null) {
            return ((o1, o2) -> 0);
        }
        if (a == null || b == null) {
            return (o1, o2) -> -1;
        }
        if (a instanceof String && b instanceof String) {
            return Comparator.comparing(o -> (String) o);
        }
        if (a instanceof Integer && b instanceof Integer) {
            return Comparator.comparing(o -> (Integer) o);
        }
        if (a instanceof Double && b instanceof Double) {
            return Comparator.comparing(o -> (Double) o);
        }
        if (a instanceof Long && b instanceof Long) {
            return Comparator.comparing(o -> (Long) o);
        }
        if (a instanceof Boolean && b instanceof Boolean) {
            return Comparator.comparing(o -> (Boolean) o);
        }

        throw new InvalidCondition(a, b);
    }

    private Object getFieldValue(ObjectContext ctx) {
        Object value = ctx.getFieldValue(fieldA, Object.class);
        if (value == null) {
            value = ctx.getRefValue(fieldA, Object.class);
        }
        return value;
    }

    @AllArgsConstructor
    public class InvalidCondition extends RuntimeException {
        private final Object a;
        private final Object b;

        @Override
        public String getMessage() {
            return String.format("Invalid condition : '%s' %s %s.\nThe value <%s> (%s) is incompatible <%s> (%s)",
                    fieldA, operator, b, a, a.getClass().getSimpleName(), b, b.getClass().getSimpleName());
        }
    }

    public class InvalidConditionOperator extends RuntimeException {
        @Override
        public String getMessage() {
            return String.format("The condition on the field \"%s\" is not valid. \"%s\" is not a valid operator." +
                    "\nAvailable operators are : %s", fieldA, operator, AVAILABLE_OPERATORS);
        }
    }
}

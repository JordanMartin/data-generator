package io.github.jordanmartin.datagenerator.provider.object;

import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderArg;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderCtor;
import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;
import lombok.AllArgsConstructor;

import java.util.Comparator;
import java.util.List;

@Provider(
        name = "If",
        description = "Retourne true ou false selon une condition sur un champ. A noter: les type des champs doivent être identiques",
        examples = {
                "If(\"age\", \">=\", 18) => retourne true si le champ age est >= 18 sinon false"
        },
        returns = Boolean.class,
        group = "reference"
)
public class If implements ValueProvider<Boolean> {

    private final String field;
    private final String operator;
    private final ValueProvider<?> provider;
    public static final List<String> AVAILABLE_OPERATORS = List.of("<", "<=", "=", "!=", ">",  ">=");

    @ProviderCtor
    public If(
            @ProviderArg(description = "Nom du champ sur lequel porte la condition") String field,
            @ProviderArg(description = "Opérateur : <, <=, =, !=, >, >=") String operator,
            @ProviderArg(description = "Valeur à comparer") Object value) {
        this(field, operator, ctx -> value);
    }

    @ProviderCtor
    public If(
            @ProviderArg(description = "Nom du champ sur lequel porte la condition") String field,
            @ProviderArg(description = "Opérateur : <, <=, =, !=, >, >=") String operator,
            @ProviderArg(description = "Valeur à comparer") ValueProvider<?> provider) {
        this.field = field;
        this.operator = operator;
        this.provider = provider;

        if (!AVAILABLE_OPERATORS.contains(operator)) {
            throw new InvalidConditionOperator();
        }
    }

    @Override
    public Boolean getOneWithContext(IObjectProviderContext ctx) {
        Object a = getFieldValue(ctx);
        Object b = provider.getOneWithContext(ctx);
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

    private Object getFieldValue(IObjectProviderContext ctx) {
        Object value = ctx.getFieldValue(field);
        if (value == null) {
            value = ctx.getRefValue(field);
        }
        return value;
    }

    @AllArgsConstructor
    public class InvalidCondition extends RuntimeException {
        private final Object a;
        private final Object b;

        @Override
        public String getMessage() {
            return String.format("Condition invalide : '%s' %s %s.\nLa valeur <%s> (%s) est incompatible avec <%s> (%s)",
                    field, operator, b, a, a.getClass().getSimpleName(), b, b.getClass().getSimpleName());
        }
    }

    public class InvalidConditionOperator extends RuntimeException {
        @Override
        public String getMessage() {
            return String.format("La condition du champ \"%s\" est invalide. \"%s\" n'est pas un opérateur valide." +
                    "\nLes opérateurs valides sont : %s", field, operator, AVAILABLE_OPERATORS);
        }
    }
}

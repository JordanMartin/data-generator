package io.github.datagenerator.domain.providers.object;


import io.github.datagenerator.domain.core.DataDefinition;
import io.github.datagenerator.domain.core.MapProvider;
import io.github.datagenerator.domain.providers.base.Constant;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IfTest {

    @Test
    void test() {
        var definition = new DataDefinition();
        definition.field("ageA", () -> 1);
        definition.field("ageB", () -> 20);
        definition.field("isAMajor", new If("ageA", ">=", 18));
        definition.field("isBMajor", new If("ageB", ">=", 18));

        MapProvider provider = new MapProvider(definition);
        Map<String, ?> object = provider.get();
        assertEquals(false, object.get("isAMajor"));
        assertEquals(true, object.get("isBMajor"));
    }

    @Test
    void invalidOperator() {
        assertThrows(If.InvalidConditionOperator.class, () -> new If("ageB", "&&", 18));
    }

    @Test
    void invalidCondition() {
        var definition = new DataDefinition();
        definition.field("age", new Constant<>("1"));
        definition.field("isMajor", new If("age", ">=", 18));
        MapProvider provider = new MapProvider(definition);

        assertThrows(If.InvalidCondition.class, provider::get);
    }
}

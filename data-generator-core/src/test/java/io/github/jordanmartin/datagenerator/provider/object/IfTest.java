package io.github.jordanmartin.datagenerator.provider.object;

import io.github.jordanmartin.datagenerator.provider.base.Constant;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IfTest {

    @Test
    void test() {
        var provider = new ObjectProvider()
                .field("ageA", new Constant<>(1))
                .field("ageB", new Constant<>(20))
                .field("isAMajor", new If("ageA", ">=", 18))
                .field("isBMajor", new If("ageB", ">=", 18));

        Map<String, ?> object = provider.getOne();
        assertEquals(false, object.get("isAMajor"));
        assertEquals(true, object.get("isBMajor"));
    }

    @Test
    void invalidOperator() {
        assertThrows(If.InvalidConditionOperator.class, () -> new If("ageB", "&&", 18));
    }

    @Test
    void invalidCondition() {
        var provider = new ObjectProvider()
                .field("age", new Constant<>("1"))
                .field("isMajor", new If("age", ">=", 18));

        assertThrows(If.InvalidCondition.class, provider::getOne);
    }
}

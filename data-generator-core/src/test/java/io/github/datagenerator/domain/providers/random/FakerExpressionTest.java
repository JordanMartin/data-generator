package io.github.datagenerator.domain.providers.random;

import io.github.datagenerator.domain.core.ValueProviderException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FakerExpressionTest {

    @Test
    void shortcut() {
        FakerExpression fakerExpression = new FakerExpression("Name.firstName");
        String name = fakerExpression.get();
        assertFalse(name.isBlank());
        assertNotEquals("Name.firstname", name);
    }

    @Test
    void get() {
        FakerExpression fakerExpression = new FakerExpression("#{Name.firstName}");
        Assertions.assertFalse(fakerExpression.get().isBlank());
    }

    @Test
    void multiple() {
        FakerExpression fakerExpression = new FakerExpression("#{Name.fullName} (#{Address.cityName})");
        Assertions.assertTrue(fakerExpression.get().matches("[^()]+ \\([^()]+\\)"));
    }

    @Test
    void badExpression() {
        assertThrows(ValueProviderException.class, () -> {
            new FakerExpression("badexpression").get();
        });

        assertThrows(ValueProviderException.class, () -> {
            new FakerExpression("#{badexpression}").get();
        });
    }

    @Test
    void defaultLocalIsFrenchH() {
        String sample = new FakerExpression("Name.name", "fr").get();
        assertFalse(sample.isBlank());
    }
}
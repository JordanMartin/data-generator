package io.github.jordanmartin.datagenerator.provider.random;

import io.github.jordanmartin.datagenerator.provider.core.ValueProviderException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FakerExpressionTest {

    @Test
    void shortcut() {
        FakerExpression fakerExpression = new FakerExpression("Name.firstName");
        String name = fakerExpression.getOne();
        assertFalse(name.isBlank());
        assertNotEquals("Name.firstname", name);
    }

    @Test
    void getOne() {
        FakerExpression fakerExpression = new FakerExpression("#{Name.firstName}");
        assertFalse(fakerExpression.getOne().isBlank());
    }

    @Test
    void multiple() {
        FakerExpression fakerExpression = new FakerExpression("#{Name.fullName} (#{Address.cityName})");
        assertTrue(fakerExpression.getOne().matches("[^()]+ \\([^()]+\\)"));
    }

    @Test
    void badExpression() {
        assertThrows(ValueProviderException.class, () -> {
            new FakerExpression("badexpression").getOne();
        });

        assertThrows(ValueProviderException.class, () -> {
            new FakerExpression("#{badexpression}").getOne();
        });
    }

    @Test
    void defaultLocalIsFrenchH() {
        String sample = new FakerExpression("Name.name", "fr").getOne();
        assertFalse(sample.isBlank());
    }
}
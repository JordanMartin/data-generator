package fr.jordanmartin.datagenerator.provider.random;

import fr.jordanmartin.datagenerator.provider.core.ValueProviderException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SampleTest {

    @Test
    void shortcut() {
        Sample sample = new Sample("Name.firstName");
        String name = sample.getOne();
        assertFalse(name.isBlank());
        assertNotEquals("Name.firstname", name);
    }

    @Test
    void getOne() {
        Sample sample = new Sample("#{Name.firstName}");
        assertFalse(sample.getOne().isBlank());
    }

    @Test
    void multiple() {
        Sample sample = new Sample("#{Name.fullName} (#{Address.cityName})");
        assertTrue(sample.getOne().matches("[^()]+ \\([^()]+\\)"));
    }

    @Test
    void badExpression() {
        assertThrows(ValueProviderException.class, () -> {
            new Sample("badexpression").getOne();
        });

        assertThrows(ValueProviderException.class, () -> {
            new Sample("#{badexpression}").getOne();
        });
    }

    @Test
    void defaultLocalIsFrenchH() {
        String sample = new Sample("Name.name", "fr").getOne();
        assertFalse(sample.isBlank());
    }
}
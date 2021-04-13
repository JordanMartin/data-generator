package fr.jordanmartin.datagenerator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObjectBuilderTest {

    @Test
    void useBuilder() {
        SampleObjectBuilder generator = new SampleObjectBuilder();
        assertEquals(8, generator.getOne().size());
    }

    static class SampleObjectBuilder extends ObjectBuilder {
        @Override
        public void configure() {
            providerRef("randomInt", randomInt(0, 10));

            field("randomFromList with weight", randomFromList(itemWeight("a", 1), itemWeight("b", 9)));
            field("list", list(randomInt(0, 100), increment()));
            field("randomUUID", randomUUID());
            field("randomFromRegex", randomFromRegex("P[A-Z]{3}[0-9]{5}", 5));
            field("firstname", () -> faker.name().firstName());
            field("lastname", () -> faker.name().lastName());
            field("expression", expression("${firstname} ${lastname}"));
            field("reference", reference("randomInt"));
        }
    }
}
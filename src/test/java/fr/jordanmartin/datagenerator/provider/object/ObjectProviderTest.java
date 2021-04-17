package fr.jordanmartin.datagenerator.provider.object;

import com.github.javafaker.Faker;
import fr.jordanmartin.datagenerator.provider.base.Constant;
import fr.jordanmartin.datagenerator.provider.random.RandomInt;
import fr.jordanmartin.datagenerator.provider.random.Sample;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ObjectProviderTest {

    @Test
    void nestFieldObject() {
        Faker faker = new Faker();
        var personProvider = new ObjectProvider()
                .providerRef("firstname", () -> faker.name().firstName())
                .providerRef("lastname", () -> faker.name().lastName())
                .field("age", new RandomInt(20, 50))
                .field("fullname", (ContextAwareProvider<?>) new Expression("${firstname} ${lastname}"))
                .field("children", ctx -> new ObjectProvider()
                        .providerRef("firstname", () -> faker.name().firstName())
                        .providerRef("lastname", new Constant<>(ctx.getRefProviderValue("lastname")))
                        .field("age", new RandomInt(1, 20))
                        .field("fullname", (ContextAwareProvider<?>) new Expression("${firstname} ${lastname}"))
                        .repeat(new RandomInt(0, 3))
                        .getOne())
                .field("number_child", ctx -> ctx.getFieldValue("children", List.class).size());

        personProvider.getStream(10).forEach(System.out::println);
    }

    @Test
    void throwExceptionOnMissingRef() {
        assertThrows(IllegalArgumentException.class, () -> new ObjectProvider()
                .field("field", (ContextAwareProvider<?>) new Expression("${missingReference}"))
                .getOne());

        assertThrows(IllegalArgumentException.class, () -> new ObjectProvider()
                .field("field", (ContextAwareProvider<?>) new Reference<>("missingReference"))
                .getOne());
    }

    @Test
    void fieldOrder() {
        List<String> fields = List.of("one", "two", "three", "four");

        ObjectProvider provider = new ObjectProvider(true);
        for (String field : fields) {
            provider.field(field, () -> field);
        }
        int idx = 0;
        for (Map.Entry<String, ?> entry : provider.getOne().entrySet()) {
            assertEquals(fields.get(idx++), entry.getKey());
        }
    }

    @Test
    void simpleFields() {
        ObjectProvider provider = new ObjectProvider()
                .field("a", () -> "a")
                .field("b", () -> "b");

        Map<String, ?> object = provider.getOne();
        assertEquals(2, provider.getOne().size());
        assertEquals("a", object.get("a"));
        assertEquals("b", object.get("b"));
    }

    @Test
    void sampleField() {
        ObjectProvider provider = new ObjectProvider()
                .field("a", () -> "a")
                .field("firstname", new Sample("Name.firstName"))
                .field("lastname", new Sample("#{Name.lastName}"));

        Map<String, ?> object = provider.getOne();
        assertEquals(3, provider.getOne().size());
        assertFalse(((String)object.get("firstname")).isBlank());
        assertFalse(((String)object.get("lastname")).isBlank());
    }

    @Test
    void nullField() {
        ObjectProvider provider = new ObjectProvider()
                .field("a", () -> null);
        Map<String, ?> object = provider.getOne();
        assertEquals(1, provider.getOne().size());
        assertNull(object.get("a"));
    }

    @Test
    void emptyObject() {
        ObjectProvider provider = new ObjectProvider();
        assertTrue(provider.getOne().isEmpty());
    }

    @Test
    void providerRef() {
        String REF_VALUE = "refvalue";
        ObjectProvider provider = new ObjectProvider()
                .providerRef("theRef", () -> REF_VALUE)

                .field("ref", (ContextAwareProvider<?>) new Reference<>("theRef"))
                .field("expression", (ContextAwareProvider<?>) new Expression("${theRef}"));

        Map<String, ?> object = provider.getOne();
        assertEquals(2, object.size());
        assertEquals(REF_VALUE, object.get("ref"));
        assertEquals(REF_VALUE, object.get("expression"));
    }

    @Test
    void testExpression() {
        ObjectProvider provider = new ObjectProvider()
                .field("firstname", () -> "John")
                .field("lastname", () -> "Doe")
                .field("fullname", (ContextAwareProvider<?>) new Expression("${firstname} ${lastname}"));
        Map<String, ?> object = provider.getOne();
        assertEquals(3, object.size());
        assertEquals("John Doe", object.get("fullname"));
    }
}
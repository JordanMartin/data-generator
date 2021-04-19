package fr.jordanmartin.datagenerator.provider.object;

import fr.jordanmartin.datagenerator.provider.base.Constant;
import fr.jordanmartin.datagenerator.provider.base.ValueProviderException;
import fr.jordanmartin.datagenerator.provider.random.RandomInt;
import fr.jordanmartin.datagenerator.provider.random.Sample;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ObjectProviderTest {

    @Test
    @SuppressWarnings("unchecked")
    void nestedFieldObject() {
        var childProvider = new ObjectProvider()
                .providerRef("child_ref", (objectContext) -> "child_ref_value")
                .field("child_field", (objectContext) -> "child")
                .field("child_ref_field", new Reference<>("child_ref"));

        var rootProvider = new ObjectProvider()
                .providerRef("root_ref", (objectContext) -> "root_ref_value")
                .field("root_field", (objectContext) -> "root")
                .field("child", childProvider)
                .field("root_ref_field", new Reference<>("root_ref"));

        Map<String, ?> root = rootProvider.getOne();
        assertEquals(3, root.size());
        assertEquals("root", root.get("root_field"));
        assertEquals("root_ref_value", root.get("root_ref_field"));

        Map<String, ?> child = (Map<String, ?>) root.get("child");
        assertEquals(2, child.size());
        assertEquals("child", child.get("child_field"));
        assertEquals("child_ref_value", child.get("child_ref_field"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void nestObjectAccessParentReferencesWithoutOverride() {
        ObjectProvider childProvider = new ObjectProvider()
                .providerRef("ref", (objectContext) -> "ref_value_of_child")
                .field("root_ref", new Reference<>("root_ref"))
                .field("ref", new Reference<>("ref"));

        var rootProvider = new ObjectProvider()
                .providerRef("root_ref", (objectContext) -> "root_ref_value")
                .providerRef("ref", (objectContext) -> "ref_value_of_root")
                .field("root_ref", new Reference<>("root_ref"))
                .field("ref", new Reference<>("ref"))
                .field("child", childProvider);

        Map<String, ?> root = rootProvider.getOne();
        assertEquals("root_ref_value", root.get("root_ref"));
        assertEquals("ref_value_of_root", root.get("ref"));

        Map<String, ?> child = (Map<String, ?>) root.get("child");
        assertEquals("root_ref_value", child.get("root_ref"));
        assertEquals("ref_value_of_child", child.get("ref"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void nestObjectWithContextAccessParentReferencesWithoutOverride() {
        ObjectProvider childProvider = new ObjectProvider()
                .providerRef("ref", (objectContext) -> "ref_value_of_child")
                .field("root_ref", new Reference<>("root_ref"))
                .field("ref", new Reference<>("ref"));

        var rootProvider = new ObjectProvider()
                .providerRef("root_ref", (objectContext) -> "root_ref_value")
                .providerRef("ref", (objectContext) -> "ref_value_of_root")
                .field("root_ref", new Reference<>("root_ref"))
                .field("ref", new Reference<>("ref"))
                .field("child", ctx -> childProvider);

        Map<String, ?> root = rootProvider.getOne();
        assertEquals("root_ref_value", root.get("root_ref"));
        assertEquals("ref_value_of_root", root.get("ref"));

        Map<String, ?> child = (Map<String, ?>) root.get("child");
        assertEquals("root_ref_value", child.get("root_ref"));
        assertEquals("ref_value_of_child", child.get("ref"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void nestedFieldWithContextObject() {
        var rootProvider = new ObjectProvider()
                .providerRef("firstname", new Constant<>("root_firstname"))
                .providerRef("lastname", new Constant<>("root_lastname"))
                .field("age", new RandomInt(20, 50))
                .field("fullname", new Expression("${firstname} ${lastname}"))
                .field("children", ctx -> new ObjectProvider()
                        .providerRef("child_firstname", new Constant<>("child_firstname"))
                        .providerRef("child_lastname", new Constant<>(ctx.getRefProviderValue("lastname")))
                        .field("age", new RandomInt(1, 19))
                        .field("child_fullname", new Expression("${child_firstname} ${child_lastname}"))
                        .repeat(2))
                .field("number_child", ctx -> ctx.getFieldValue("children", List.class).size());

        Map<String, ?> root = rootProvider.getOne();
        assertEquals(4, root.size());
        assertEquals("root_firstname root_lastname", root.get("fullname"));
        int rootAge = (int) root.get("age");
        assertTrue(rootAge >= 20 && rootAge <= 50);


        List<Map<String, ?>> children = (List<Map<String, ?>>) root.get("children");
        assertEquals(2, children.size());
        for (Map<String, ?> child : children) {
            assertEquals(2, child.size());
            int childAge = (int) child.get("age");
            assertTrue(childAge >= 1 && childAge <= 19);
            assertEquals("child_firstname root_lastname", child.get("child_fullname"));
        }
    }

    @Test
    void throwExceptionOnMissingRef() {
        assertThrows(ValueProviderException.class, () -> new ObjectProvider()
                .field("field", new Expression("${missingReference}"))
                .getOne());

        assertThrows(ValueProviderException.class, () -> new ObjectProvider()
                .field("field", new Reference<>("missingReference"))
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
        assertFalse(((String) object.get("firstname")).isBlank());
        assertFalse(((String) object.get("lastname")).isBlank());
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

                .field("ref", new Reference<>("theRef"))
                .field("expression", new Expression("${theRef}"));

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
                .field("fullname", new Expression("${firstname} ${lastname}"));
        Map<String, ?> object = provider.getOne();
        assertEquals(3, object.size());
        assertEquals("John Doe", object.get("fullname"));
    }
}
package io.github.datagenerator.domain.providers.object;


import io.github.datagenerator.domain.core.FieldNotFoundInContext;
import io.github.datagenerator.domain.providers.MapProviderBuilder;
import io.github.datagenerator.domain.providers.base.Constant;
import io.github.datagenerator.domain.providers.random.FakerExpression;
import io.github.datagenerator.domain.providers.random.RandomInt;
import io.github.datagenerator.domain.providers.transform.Repeat;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unchecked")
class MapProviderBuilderTest {

    @Test
    void nestedFieldObject() {
        var childProvider = new MapProviderBuilder()
                .providerRef("child_ref", (objectContext) -> "child_ref_value")
                .field("child_field", (objectContext) -> "child")
                .field("child_ref_field", new Reference<>("child_ref"))
                .build();

        var rootProvider = new MapProviderBuilder()
                .providerRef("root_ref", (objectContext) -> "root_ref_value")
                .field("root_field", (objectContext) -> "root")
                .field("child", childProvider)
                .field("root_ref_field", new Reference<>("root_ref"))
                .build();

        Map<String, ?> root = rootProvider.get();
        assertEquals(3, root.size());
        assertEquals("root", root.get("root_field"));
        assertEquals("root_ref_value", root.get("root_ref_field"));

        Map<String, ?> child = (Map<String, ?>) root.get("child");
        assertEquals(2, child.size());
        assertEquals("child", child.get("child_field"));
        assertEquals("child_ref_value", child.get("child_ref_field"));
    }

    @Test
    void nestObjectAccessParentReferencesWithoutOverride() {
        var childProvider = new MapProviderBuilder()
                .providerRef("ref", (objectContext) -> "ref_value_of_child")
                .field("root_ref", new Reference<>("root_ref"))
                .field("ref", new Reference<>("ref"))
                .build();

        var rootProvider = new MapProviderBuilder()
                .providerRef("root_ref", (objectContext) -> "root_ref_value")
                .providerRef("ref", (objectContext) -> "ref_value_of_root")
                .field("root_ref", new Reference<>("root_ref"))
                .field("ref", new Reference<>("ref"))
                .field("child", childProvider)
                .build();

        Map<String, ?> root = rootProvider.get();
        assertEquals("root_ref_value", root.get("root_ref"));
        assertEquals("ref_value_of_root", root.get("ref"));

        Map<String, ?> child = (Map<String, ?>) root.get("child");
        assertEquals("root_ref_value", child.get("root_ref"));
        assertEquals("ref_value_of_child", child.get("ref"));
    }

    @Test
    void nestObjectWithContextAccessParentReferencesWithoutOverride() {
        var childProvider = new MapProviderBuilder()
                .providerRef("ref", (objectContext) -> "ref_value_of_child")
                .field("root_ref", new Reference<>("root_ref"))
                .field("ref", new Reference<>("ref"))
                .build();

        var rootProvider = new MapProviderBuilder()
                .providerRef("root_ref", (objectContext) -> "root_ref_value")
                .providerRef("ref", (objectContext) -> "ref_value_of_root")
                .field("root_ref", new Reference<>("root_ref"))
                .field("ref", new Reference<>("ref"))
                .field("child", ctx -> childProvider)
                .build();

        Map<String, ?> root = rootProvider.get();
        assertEquals("root_ref_value", root.get("root_ref"));
        assertEquals("ref_value_of_root", root.get("ref"));

        Map<String, ?> child = (Map<String, ?>) root.get("child");
        assertEquals("root_ref_value", child.get("root_ref"));
        assertEquals("ref_value_of_child", child.get("ref"));
    }

    @Test
    void nestedFieldWithContextObject() {
        var rootProvider = new MapProviderBuilder()
                .providerRef("firstname", new Constant<>("root_firstname"))
                .providerRef("lastname", new Constant<>("root_lastname"))
                .field("age", new RandomInt(20, 50))
                .field("fullname", new Expression("${firstname} ${lastname}"))
                .field("children", ctx -> new Repeat<>(new MapProviderBuilder()
                        .providerRef("child_firstname", new Constant<>("child_firstname"))
                        .providerRef("child_lastname", new Reference<>("lastname"))
                        .field("age", new RandomInt(1, 19))
                        .field("child_fullname", new Expression("${child_firstname} ${child_lastname}"))
                        .build(), 2))
                .field("number_child", ctx -> ctx.getFieldValue("children", List.class).size())
                .build();

        Map<String, ?> root = rootProvider.get();
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
        assertThrows(FieldNotFoundInContext.class, () -> new MapProviderBuilder()
                .field("field", new Expression("${missingReference}"))
                .build()
                .get());

        assertThrows(FieldNotFoundInContext.class, () -> new MapProviderBuilder()
                .field("field", new Reference<>("missingReference"))
                .build()
                .get());
    }

    @Test
    void fieldOrder() {
        List<String> fields = List.of("one", "two", "three", "four");

        var provider = new MapProviderBuilder();
        for (String field : fields) {
            provider.field(field, () -> field);
        }
        int idx = 0;
        for (Map.Entry<String, ?> entry : provider.build().get().entrySet()) {
            assertEquals(fields.get(idx++), entry.getKey());
        }
    }

    @Test
    void simpleFields() {
        var provider = new MapProviderBuilder()
                .field("a", () -> "a")
                .field("b", () -> "b")
                .build();

        Map<String, ?> object = provider.get();
        assertEquals(2, provider.get().size());
        assertEquals("a", object.get("a"));
        assertEquals("b", object.get("b"));
    }

    @Test
    void sampleField() {
        var provider = new MapProviderBuilder()
                .field("a", () -> "a")
                .field("firstname", new FakerExpression("Name.firstName"))
                .field("lastname", new FakerExpression("#{Name.lastName}"))
                .build();

        Map<String, ?> object = provider.get();
        assertEquals(3, provider.get().size());
        assertFalse(((String) object.get("firstname")).isBlank());
        assertFalse(((String) object.get("lastname")).isBlank());
    }

    @Test
    void nullField() {
        var provider = new MapProviderBuilder()
                .field("a", () -> null)
                .build();
        Map<String, ?> object = provider.get();
        assertEquals(1, provider.get().size());
        assertNull(object.get("a"));
    }

    @Test
    void emptyObject() {
        var provider = new MapProviderBuilder().build();
        assertTrue(provider.get().isEmpty());
    }

    @Test
    void providerRef() {
        String REF_VALUE = "refvalue";
        var provider = new MapProviderBuilder()
                .providerRef("theRef", () -> REF_VALUE)
                .field("ref", new Reference<>("theRef"))
                .field("expression", new Expression("${theRef}"))
                .build();

        Map<String, ?> object = provider.get();
        assertEquals(2, object.size());
        assertEquals(REF_VALUE, object.get("ref"));
        assertEquals(REF_VALUE, object.get("expression"));
    }

    @Test
    void testExpression() {
        var provider = new MapProviderBuilder()
                .field("firstname", () -> "John")
                .field("lastname", () -> "Doe")
                .field("fullname", new Expression("${firstname} ${lastname}"))
                .build();

        Map<String, ?> object = provider.get();
        assertEquals(3, object.size());
        assertEquals("John Doe", object.get("fullname"));
    }

    @Test
    void referencesAreEvaluatedEachTime() {
        var provider = new MapProviderBuilder()
                .providerRef("nanotime", System::nanoTime)
                .field("a", new Reference<>("nanotime"))
                .field("b", new Reference<>("nanotime"))
                .field("c", new Reference<>("nanotime"))
                .field("child", new MapProviderBuilder()
                        .field("d", new Reference<>("nanotime"))
                        .build()
                )
                .build();

        Map<String, ?> object = provider.get();
        Map<String, Object> child = (Map<String, Object>) object.get("child");

        assertEquals(4, object.size());
        assertNotEquals(object.get("a"), object.get("b"));
        assertNotEquals(object.get("b"), object.get("c"));
        assertNotEquals(child.get("d"), object.get("c"));
    }

    @Test
    void fixedReferenceAreEqualsInsideObject() {
        var provider = new MapProviderBuilder()
                .providerRef("nanotime", System::nanoTime)
                .field("a", new FixedReference<>("nanotime"))
                .field("b", new FixedReference<>("nanotime"))
                .field("c", new FixedReference<>("nanotime"))
                .field("child", new MapProviderBuilder()
                        .field("d", new FixedReference<>("nanotime"))
                        .build()
                )
                .build();

        Map<String, ?> object = provider.get();
        Map<String, Object> child = (Map<String, Object>) object.get("child");

        assertEquals(4, object.size());
        assertEquals(object.get("a"), object.get("b"));
        assertEquals(object.get("b"), object.get("c"));
        assertEquals(object.get("c"), child.get("d"));
    }

}
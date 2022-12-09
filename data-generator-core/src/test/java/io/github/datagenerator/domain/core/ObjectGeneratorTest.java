package io.github.datagenerator.domain.core;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
public class ObjectGeneratorTest {

    @Test
    void should_generate_map() {
        // Given
        String field = "field";
        String value = "value";

        DataDefinition definition = new DataDefinition();
        definition.field(field, ctx -> value);
        MapProvider gen = new MapProvider(definition);

        // When
        Map<String, Object> object = gen.get();

        // Then
        assertTrue(object.containsKey(field));
        assertEquals(value, object.get(field));
    }

    @Test
    void should_generate_map_with_related_field() {
        // Given
        DataDefinition definition = new DataDefinition();
        definition.field("a", () -> "a");
        definition.field("b", () -> "b");
        definition.field("c", (ctx) -> ctx.getFieldValue("a", String.class) + ctx.getFieldValue("b", String.class));
        MapProvider gen = new MapProvider(definition);

        // When
        Map<String, Object> object = gen.get();

        // Then
        assertEquals("ab", object.get("c"));
    }

    @Test
    void should_returns_null_when_missing_related_field() {
        // Given
        DataDefinition definition = new DataDefinition();
        definition.field("c", (ctx) -> ctx.getFieldValue("a", String.class));
        MapProvider gen = new MapProvider(definition);

        // When / Then
        assertNull(gen.get().get("c"));
    }

    @Test
    void should_evaluate_if_field_return_provider() {
        // Given
        DataDefinition definition = new DataDefinition();
        ValueProvider<String> sayHelloProvider = ctx -> "Hello " + ctx.getFieldValue("name", String.class);
        definition.field("name", () -> "World");
        definition.field("message", () -> sayHelloProvider);
        MapProvider gen = new MapProvider(definition);

        // When
        Map<String, Object> object = gen.get();

        // Then
        assertEquals("Hello World", object.get("message"));
    }

    @Test
    void should_evaluate_provider_with_context_from_a_provider() {
        // Given
        DataDefinition definition = new DataDefinition();
        ValueProvider<String> sayHelloProvider = ctx -> "Hello " + ctx.getFieldValue("name", String.class);
        definition.field("name", () -> "World");
        definition.field("message", ctx -> sayHelloProvider.get(ctx));
        MapProvider gen = new MapProvider(definition);

        // When
        Map<String, Object> object = gen.get();

        // Then
        assertEquals("Hello World", object.get("message"));
    }

    @Test
    void should_compute_field_number() {
        // Given
        DataDefinition definition = new DataDefinition();
        definition.field("first", ObjectContext::getCurrentFieldNumber);
        definition.field("second", ObjectContext::getCurrentFieldNumber);
        MapProvider gen = new MapProvider(definition);

        // When
        Map<String, Object> object = gen.get();

        // Then
        assertEquals(0, object.get("first"));
        assertEquals(1, object.get("second"));
    }

    @Test
    void should_generate_multiple_map() {
        // Given
        DataDefinition definition = new DataDefinition();
        definition.field("num", ObjectContext::getObjectIndex);
        MapProvider gen = new MapProvider(definition);

        // When
        List<Map<String, ?>> objects = gen.getMany(2);

        // Then
        assertEquals(0L, objects.get(0).get("num"));
        assertEquals(1L, objects.get(1).get("num"));
    }

    @Test
    void should_generate_sub_map() {
        // Given
        DataDefinition childDefinition = new DataDefinition();
        childDefinition.field("nameChild", () -> "child");
        childDefinition.field("nameOfParent", ctx -> ctx.getFieldValue("nameParent", String.class));
        MapProvider child = new MapProvider(childDefinition);

        DataDefinition parentDefinition = new DataDefinition();
        parentDefinition.field("nameParent", () -> "parent");
        parentDefinition.field("child", child);
        MapProvider gen = new MapProvider(parentDefinition);

        // When
        Map<String, Object> object = gen.get();

        // Then
        assertEquals("parent", object.get("nameParent"));
        assertEquals("child", ((Map<?, ?>)object.get("child")).get("nameChild"));
        assertEquals("parent", ((Map<?, ?>)object.get("child")).get("nameOfParent"));
    }
}

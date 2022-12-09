package io.github.datagenerator.domain.providers.object;

import io.github.datagenerator.domain.core.DataDefinition;
import io.github.datagenerator.domain.core.MapProvider;
import io.github.datagenerator.domain.providers.base.IntIncrement;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExpressionTest {

    @Test
    void testExpression() {

        DataDefinition definition = new DataDefinition();
        definition.refField("ref", new IntIncrement());
        definition.field("a", new Expression("${ref}"));
        definition.field("a'", new Expression("${ref}"));
        definition.field("b", new Expression("$${ref}"));
        definition.field("b'", new Expression("$${ref}"));

        MapProvider provider = new MapProvider(definition);

        Map<String, ?> first = provider.get();
        assertEquals("1", first.get("a"));
        assertEquals("2", first.get("a'"));
        assertEquals("0", first.get("b"));
        assertEquals("0", first.get("b'"));

        Map<String, ?> second = provider.get();
        assertEquals("4", second.get("a"));
        assertEquals("5", second.get("a'"));
        assertEquals("3", second.get("b"));
        assertEquals("3", second.get("b'"));
    }

    @Test
    void testComplexExpression() {
        DataDefinition definition = new DataDefinition();
        definition.refField("id", new IntIncrement());
        definition.refField("prefix", () -> ">");
        definition.field("groupId", new Expression("group-$${id}"));
        definition.field("expr", new Expression("${prefix}group-$${id}"));

        MapProvider provider = new MapProvider(definition);

        Map<String, ?> first = provider.get();
        assertEquals("group-0", first.get("groupId"));
        assertEquals(">group-0", first.get("expr"));

        Map<String, ?> second = provider.get();
        assertEquals("group-1", second.get("groupId"));
        assertEquals(">group-1", second.get("expr"));
    }
}
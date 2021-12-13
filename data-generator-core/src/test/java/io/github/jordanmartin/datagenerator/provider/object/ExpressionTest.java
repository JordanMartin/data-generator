package io.github.jordanmartin.datagenerator.provider.object;

import io.github.jordanmartin.datagenerator.provider.sequence.IntIncrement;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExpressionTest {

    @Test
    void testExpression() {
        ObjectProvider provider = new ObjectProvider()
                .providerRef("ref", new IntIncrement())
                .field("a", new Expression("${ref}"))
                .field("a'", new Expression("${ref}"))
                .field("b", new Expression("$${ref}"))
                .field("b'", new Expression("$${ref}"));

        Map<String, ?> first = provider.getOne();
        assertEquals("1", first.get("a"));
        assertEquals("2", first.get("a'"));
        assertEquals("0", first.get("b"));
        assertEquals("0", first.get("b'"));

        Map<String, ?> second = provider.getOne();
        assertEquals("4", second.get("a"));
        assertEquals("5", second.get("a'"));
        assertEquals("3", second.get("b"));
        assertEquals("3", second.get("b'"));
    }

    @Test
    void testComplexExpression() {
        ObjectProvider provider = new ObjectProvider()
                .providerRef("id", new IntIncrement())
                .providerRef("prefix", () -> ">")
                .field("groupId", new Expression("group-$${id}"))
                .field("expr", new Expression("${prefix}group-$${id}"));

        Map<String, ?> first = provider.getOne();
        assertEquals("group-0", first.get("groupId"));
        assertEquals(">group-0", first.get("expr"));

        Map<String, ?> second = provider.getOne();
        assertEquals("group-1", second.get("groupId"));
        assertEquals(">group-1", second.get("expr"));
    }
}
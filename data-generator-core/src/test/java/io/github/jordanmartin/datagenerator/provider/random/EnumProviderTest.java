package io.github.jordanmartin.datagenerator.provider.random;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EnumProviderTest {

    @Test
    void getOneConstructorCollection() {
        List<String> set = List.of("a", "b", "c");
        EnumProvider<String> provider = new EnumProvider<>(set);

        for (int i = 0; i < 10; i++) {
            Object item = provider.getOne();
            assertTrue(set.contains(item));
        }
    }

    @Test
    void getOneConstructorArray() {
        Set<String> set = Set.of("a", "b", "c");
        EnumProvider<String> provider = new EnumProvider<>("a", "b", "c");

        for (int i = 0; i < 10; i++) {
            Object item = provider.getOne();
            assertTrue(set.contains(item));
        }
    }

    @Test
    void getOneConstructorProvider() {
        var set = Set.of("a", "b", "c");
        var provider = new EnumProvider<String>("a", "b", "c");

        for (int i = 0; i < 10; i++) {
            Object item = provider.getOne();
            assertTrue(set.contains(item));
        }
    }

    @Test
    void getOneWithNullWeight() {
        var a = new EnumWeight("a10%", 1);
        var b = new EnumWeight("b90%", 9);
        var c = new EnumWeight("c0%", 0);
        var provider = new EnumProvider<String>(a, b, c);

        var set = Set.of("a10%", "b90%");
        for (int i = 0; i < 10; i++) {
            String item = provider.getOne();
            assertTrue(set.contains(item));
        }
    }

    @Test
    void getOneWithWeight() {
        var a = new EnumWeight("a50%", 5);
        var b = new EnumWeight("b30%", 3);
        var c = new EnumWeight("c20%", 2);
        var d = new EnumWeight("d10%", 1);
        var provider = new EnumProvider<String>(a, b, c, d);

        Map<String, Long> collect = provider.getStream(1000)
                .collect(groupingBy(identity(), counting()));

        System.out.println(collect);
        assertTrue(collect.get("a50%") > collect.get("b30%"));
        assertTrue(collect.get("b30%") > collect.get("c20%"));
        assertTrue(collect.get("c20%") > collect.get("d10%"));
    }
}
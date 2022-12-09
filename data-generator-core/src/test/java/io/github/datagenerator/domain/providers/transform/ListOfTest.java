package io.github.datagenerator.domain.providers.transform;

import io.github.datagenerator.domain.core.ValueProvider;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ListOfTest {

    @Test
    void get() {
        ValueProvider<String> providerA = ctx -> "a";
        ValueProvider<Integer> providerB = ctx -> 1;
        ListOf listProvider = new ListOf(providerA, providerB);
        List<?> list = listProvider.get();
        assertEquals(2, list.size());
        assertEquals(providerA.get(), list.get(0));
        assertEquals(providerB.get(), list.get(1));
    }
}
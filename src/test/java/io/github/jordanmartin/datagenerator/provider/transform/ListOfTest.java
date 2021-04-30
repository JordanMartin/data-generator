package io.github.jordanmartin.datagenerator.provider.transform;

import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ListOfTest {

    @Test
    void getOne() {
        ValueProvider<String> providerA = (obectContext) -> "a";
        ValueProvider<Integer> providerB = (obectContext) -> 1;
        ListOf listProvider = new ListOf(providerA, providerB);
        List<?> list = listProvider.getOne();
        assertEquals(2, list.size());
        assertEquals(providerA.getOne(), list.get(0));
        assertEquals(providerB.getOne(), list.get(1));
    }
}
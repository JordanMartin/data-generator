package fr.jordanmartin.datagenerator.provider;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListOfTest {

    @Test
    void getOne() {
        ValueProvider<String> providerA = () -> "a";
        ValueProvider<Integer> providerB = () -> 1;
        ListOf listProvider = new ListOf(providerA, providerB);
        List<Object> list = listProvider.getOne();
        assertEquals(2, list.size());
        assertEquals(providerA.getOne(), list.get(0));
        assertEquals(providerB.getOne(), list.get(1));
    }
}
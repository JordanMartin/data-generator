package io.github.datagenerator.generation.writer;

import io.github.datagenerator.domain.core.MapProvider;
import io.github.datagenerator.domain.providers.MapProviderBuilder;
import io.github.datagenerator.domain.providers.base.IntIncrement;
import io.github.datagenerator.generation.output.PojoOutput;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PojoOutputTest {

    @Data
    public static class Person {
        long id;
        String firstname;
        String lastname;
        Address address;
    }

    @Data
    public static class Address {
        int streetNum;
        String streetName;
        String city;
    }

    @Test
    void toPojo() {
        MapProvider addressProvider = new MapProviderBuilder()
                .field("streetNum", ctx -> 1)
                .field("streetName", ctx -> "rue Victor Hugo")
                .field("city", ctx -> "Lyon")
                .build();

        MapProvider personProvider = new MapProviderBuilder()
                .field("id", new IntIncrement())
                .field("firstname", ctx -> "Firstname")
                .field("lastname", ctx -> "Lastname")
                .field("address", addressProvider)
                .build();

        List<Person> people = new PojoOutput<>(personProvider, Person.class).getMany(2);
        Person p1 = people.get(0);
        Person p2 = people.get(1);
        assertEquals(0, p1.getId());
        assertEquals("Lyon", p1.getAddress().getCity());
        assertEquals(1, p2.getId());
        assertEquals("rue Victor Hugo", p2.getAddress().getStreetName());
    }
}

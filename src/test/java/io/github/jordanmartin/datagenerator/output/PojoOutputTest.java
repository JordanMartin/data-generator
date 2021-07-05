package io.github.jordanmartin.datagenerator.output;

import io.github.jordanmartin.datagenerator.provider.object.ObjectProvider;
import io.github.jordanmartin.datagenerator.provider.sequence.IntAutoIncrement;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        ObjectProvider addressProvider = new ObjectProvider()
                .field("streetNum", ctx -> 1)
                .field("streetName", ctx -> "rue Victor Hugo")
                .field("city", ctx -> "Lyon");

        ObjectProvider personProvider = new ObjectProvider()
                .field("id", new IntAutoIncrement())
                .field("firstname", ctx -> "Firstname")
                .field("lastname", ctx -> "Lastname")
                .field("address", addressProvider);

        List<Person> people = new PojoOutput<>(personProvider, Person.class).getMany(2);
        Person p1 = people.get(0);
        Person p2 = people.get(1);
        assertEquals(0, p1.getId());
        assertEquals("Lyon", p1.getAddress().getCity());
        assertEquals(1, p2.getId());
        assertEquals("rue Victor Hugo", p2.getAddress().getStreetName());
    }
}

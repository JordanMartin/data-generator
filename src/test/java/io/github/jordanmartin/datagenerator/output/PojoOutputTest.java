package io.github.jordanmartin.datagenerator.output;

import io.github.jordanmartin.datagenerator.provider.object.ObjectProvider;
import io.github.jordanmartin.datagenerator.provider.sequence.IntAutoIncrement;
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
        ObjectProvider addressProvider = new ObjectProvider()
                .field("streetNum", ctx -> 1)
                .field("streetName", ctx -> "rue Victor Hugo")
                .field("city", ctx -> "Lyon");

        ObjectProvider personProvider = new ObjectProvider()
                .field("id", new IntAutoIncrement())
                .field("firstname", ctx -> "Firstname")
                .field("lastname", ctx -> "Lastname")
                .field("address", addressProvider);

        List<Person> p = new PojoOutput<>(personProvider, Person.class).getMany(10);
        System.out.println(p);
    }
}

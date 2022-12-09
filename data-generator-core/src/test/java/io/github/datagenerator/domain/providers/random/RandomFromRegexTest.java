package io.github.datagenerator.domain.providers.random;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomFromRegexTest {

    private final String euLicensePlateRegex = " [A-Z]{2}-[0-9]{3}-[A-Z]{2}";

    @Test
    void get() {
        RandomFromRegex euLicensePlateProvider = new RandomFromRegex(euLicensePlateRegex);
        for (int i = 0; i < 10; i++) {
            String plate = euLicensePlateProvider.get();
            assertTrue(plate.matches(euLicensePlateRegex));
        }
    }

    @Test
    void getSizeLimited() {
        RandomFromRegex euLicensePlateProvider = new RandomFromRegex(euLicensePlateRegex, 10);
        long distinctCount = IntStream.range(0, 100).mapToObj(i -> euLicensePlateProvider.get())
                .distinct()
                .count();
        assertEquals(10, distinctCount);
    }
}
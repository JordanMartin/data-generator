package io.github.jordanmartin.datagenerator.provider.random;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomFromRegexTest {

    private final String euLicensePlateRegex = " [A-Z]{2}[-][0-9]{3}[-][A-Z]{2}";

    @Test
    void getOne() {
        RandomFromRegex euLicensePlateProvider = new RandomFromRegex(euLicensePlateRegex);
        for (int i = 0; i < 10; i++) {
            String plate = euLicensePlateProvider.getOne();
            assertTrue(plate.matches(euLicensePlateRegex));
        }
    }

    @Test
    void getOneSizeLimited() {
        RandomFromRegex euLicensePlateProvider = new RandomFromRegex(euLicensePlateRegex, 10);
        long distinctCount = euLicensePlateProvider.getStream(100)
                .distinct()
                .count();
        assertEquals(10, distinctCount);
    }
}
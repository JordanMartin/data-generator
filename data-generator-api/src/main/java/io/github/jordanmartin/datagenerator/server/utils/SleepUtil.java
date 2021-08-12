package io.github.jordanmartin.datagenerator.server.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SleepUtil {

    /**
     * Génère une pause avec un Thread.sleep
     *
     * @param expression Expression pour le délai.
     *                   Ex: "500" => 500ms
     *                   "1000,2000" => entre 1000 et 2000ms
     */
    @SneakyThrows
    public static void sleep(String expression) {
        if (expression == null) {
            return;
        }

        int delayMs;
        if (expression.contains(",")) {
            final String[] values = expression.split(",");
            int min = Integer.parseInt(values[0].trim());
            int max = Integer.parseInt(values[1].trim());
            delayMs = (int) (min + Math.random() * (max - min));
            log.debug("Random delay from {}ms and {}ms => {}ms", min, max, delayMs);
        } else {
            delayMs = Integer.parseInt(expression);
            log.debug("Fixed delay => {}ms", delayMs);
        }

        if (delayMs > 0) {
            Thread.sleep(delayMs);
        }
    }

}

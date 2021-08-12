package io.github.jordanmartin.datagenerator.provider.core;

import io.github.jordanmartin.datagenerator.provider.transform.AsString;
import io.github.jordanmartin.datagenerator.provider.transform.Round;

import java.math.RoundingMode;

public interface DoubleProvider extends ValueProvider<Double> {

    default AsString<Double> asString() {
        return new AsString<>(this);
    }

    default Round roundUp(int precision) {
        return new Round(this, precision, RoundingMode.UP);
    }

    default Round roundDown(int precision) {
        return new Round(this, precision, RoundingMode.DOWN);
    }

}

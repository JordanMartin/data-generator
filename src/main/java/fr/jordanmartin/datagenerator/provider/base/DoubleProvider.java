package fr.jordanmartin.datagenerator.provider.base;

import fr.jordanmartin.datagenerator.provider.transform.AsString;
import fr.jordanmartin.datagenerator.provider.transform.Round;

import java.math.RoundingMode;

public abstract class DoubleProvider implements ValueProvider<Double> {

    public AsString<Double> asString() {
        return new AsString<>(this);
    }

    public Round roundUp(int precision) {
        return new Round(this, precision, RoundingMode.UP);
    }

    public Round roundDown(int precision) {
        return new Round(this, precision, RoundingMode.DOWN);
    }

}

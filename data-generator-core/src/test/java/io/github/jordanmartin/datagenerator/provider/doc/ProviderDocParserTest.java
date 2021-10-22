package io.github.jordanmartin.datagenerator.provider.doc;

import io.github.jordanmartin.datagenerator.provider.core.StatelessValueProvider;
import io.github.jordanmartin.datagenerator.provider.doc.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.doc.annotation.ProviderArg;
import io.github.jordanmartin.datagenerator.provider.doc.annotation.ProviderCtor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProviderDocParserTest {

    private ProviderDoc doc;

    @BeforeEach
    void setUp() {
        doc = ProviderDocParser.getProviderDoc(Plus.class);
    }

    @Test
    void shouldGetProviderDoc() {
        assertEquals("Un test", doc.getDescription());
        assertEquals("Plus", doc.getKey());
    }

    @Test
    void shouldGetConstructorDoc() {
        assertEquals(3, doc.getConstructors().size());

        ProviderCtorDoc ctor1 = doc.getConstructors().get(0);
        assertEquals(0, ctor1.getArgs().size());

        ProviderCtorDoc ctor2 = doc.getConstructors().get(1);
        assertEquals(1, ctor2.getArgs().size());

        ProviderCtorDoc ctor3 = doc.getConstructors().get(2);
        assertEquals(2, ctor3.getArgs().size());
    }

    @Test
    void badClassShouldFail() {
        assertThrows(IllegalArgumentException.class, () -> {
            ProviderDocParser.getProviderDoc(Object.class);
        });
    }

    @Provider(key = "Plus", description = "Un test")
    private static class Plus implements StatelessValueProvider<Integer> {
        int a;
        int b;

        public Plus(int a, int b, int c) {
        }

        @ProviderCtor("Additionne deux nombre")
        public Plus(@ProviderArg(name = "A", description = "Valeur A") int a,
                    @ProviderArg(name = "B", description = "Valeur B") int b) {
            this.a = a;
            this.b = b;
        }

        @ProviderCtor("Additionne un nombre avec 10")
        public Plus(@ProviderArg(name = "A", description = "Valeur A") int a) {
            this(a, 10);
        }

        @ProviderCtor("Aditionne 10 et 10")
        public Plus() {
            this(10, 10);
        }

        @Override
        public Integer getOne() {
            return a + b;
        }
    }
}

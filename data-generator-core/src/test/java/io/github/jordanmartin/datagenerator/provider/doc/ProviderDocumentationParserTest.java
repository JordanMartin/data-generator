package io.github.jordanmartin.datagenerator.provider.doc;

import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderArg;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderCtor;
import io.github.jordanmartin.datagenerator.provider.core.StatelessValueProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProviderDocumentationParserTest {

    private ProviderDoc doc;

    @BeforeEach
    void setUp() {
        doc = ProviderDocumentationParser.parse(Plus.class).orElseThrow();
    }

    @Test
    void should_get_provider_doc() {
        assertEquals("Un test", doc.getDescription());
        assertEquals("Plus", doc.getName());
    }

    @Test
    void should_get_3_constructors() {
        assertEquals(3, doc.getConstructors().size());
    }

    @Test
    void sould_get_first_constructors() {
        ProviderCtorDoc ctor1 = doc.getConstructors().get(0);
        assertEquals("Additionne 10 et 10", ctor1.description);
        assertEquals(0, ctor1.getArgs().size());
    }

    @Test
    void should_get_second_constructors() {
        ProviderCtorDoc ctor2 = doc.getConstructors().get(1);
        assertEquals("Additionne un nombre avec 10", ctor2.description);
        assertEquals(1, ctor2.getArgs().size());
    }

    @Test
    void should_get_third_constructors() {
        ProviderCtorDoc ctor3 = doc.getConstructors().get(2);
        assertEquals("Additionne deux nombre", ctor3.description);
        assertEquals(2, ctor3.getArgs().size());

        ProviderArgDoc ctor3Arg1 = ctor3.getArgs().get(0);
        assertEquals("A", ctor3Arg1.getName());
        assertEquals("Valeur A", ctor3Arg1.getDescription());
        assertEquals("int", ctor3Arg1.getType());
        assertArrayEquals(new String[]{}, ctor3Arg1.getExamples());

        ProviderArgDoc ctor3Arg2 = ctor3.getArgs().get(1);
        assertEquals("B", ctor3Arg2.getName());
        assertEquals("Valeur B", ctor3Arg2.getDescription());
        assertEquals("int", ctor3Arg2.getType());
        assertArrayEquals(new String[]{"1"}, ctor3Arg2.getExamples());
    }

    @Test
    void should_fail_with_not_annotated_class() {
        assertThrows(IllegalArgumentException.class, () -> ProviderDocumentationParser.parse(Object.class));
    }

    @Provider(name = "Plus", description = "Un test")
    private static class Plus implements StatelessValueProvider<Integer> {
        int a;
        int b;

        public Plus(int a, int b, int c) {
        }

        @ProviderCtor("Additionne deux nombre")
        public Plus(@ProviderArg(name = "A", description = "Valeur A") int a,
                    @ProviderArg(name = "B", description = "Valeur B", examples = "1") int b) {
            this.a = a;
            this.b = b;
        }

        @ProviderCtor("Additionne un nombre avec 10")
        public Plus(@ProviderArg(name = "A", description = "Valeur A") int a) {
            this(a, 10);
        }

        @ProviderCtor("Additionne 10 et 10")
        public Plus() {
            this(10, 10);
        }

        @Override
        public Integer getOne() {
            return a + b;
        }
    }
}

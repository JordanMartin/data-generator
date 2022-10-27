package io.github.jordanmartin.datagenerator.provider.random;

import com.mifmif.common.regex.Generex;
import com.mifmif.common.regex.util.Iterator;
import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderArg;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderCtor;
import io.github.jordanmartin.datagenerator.provider.core.StatelessValueProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Génère une chaine de caractère aléatoire basé sur une regex.
 * À noter: il peut y avoir des doublons
 */
@Slf4j
@Provider(
        name = "Regex",
        description = "Generate a string based on a regex expression",
        examples = {
                "Regex(\"[A-Z]{2}[-][0-9]{3}[-][A-Z]{2}\") => OY-230-WL",
                "Regex(\"[A-ZaZ0-9]{16}\") => 0218NaN7XaNaaAE7",
                "Documentation of regular expression : https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html"
        }
)
public class RandomFromRegex implements StatelessValueProvider<String> {

    /**
     * Générateur de String à partir d'une regex
     */
    private final Generex generex;

    /**
     * Liste de donnée pré-généré
     */
    private List<String> cache = null;

    private final Random random = new Random();


    @ProviderCtor("Random string from a regex (this can generate duplicate values)")
    public RandomFromRegex(
            @ProviderArg(description = "The regular expression") String regex) {
        this.generex = new Generex(regex);
    }

    @ProviderCtor("Generate <count> random string from a regex. Returns one of these values on each use")
    public RandomFromRegex(
            @ProviderArg(description = "The regular expression") String regex,
            @ProviderArg(description = "Number of distinct values to generate") int count) {
        this(regex, count, false);
    }

    @ProviderCtor("Generate <count> string from a regex. Returns one of these values on each use")
    public RandomFromRegex(
            @ProviderArg(description = "The regular expression") String regex,
            @ProviderArg(description = "Number of distinct values to generate") int count,
            @ProviderArg(description = "true for a sequential generation (eg. [a-z]{2} => a, b, c, ...). False for a random one. Use true if <count> is big") boolean sequential) {
        this(regex);
        generateUniqueCache(count, sequential);
    }

    @Override
    public String getOne() {
        if (cache == null) {
            return generex.random();
        } else {
            int idx = random.nextInt(cache.size());
            return cache.get(idx);
        }
    }

    /**
     * Génère un cache de {@code count} éléments
     *
     * @param count      Nombre d'éléments à générer
     * @param sequential S'ils doivent être séquentiels ou non
     */
    private void generateUniqueCache(int count, boolean sequential) {
        this.cache = new ArrayList<>();
        if (sequential) {
            fillCacheWithSequential(count);
        } else {
            fillCacheWithNonSequential(count);
        }
    }

    /**
     * Alimente le cache avec des données aléatoires à partir de la regex.
     * Méthode plus lente que la génération séquentielle
     */
    private void fillCacheWithNonSequential(int count) {
        for (int i = 0; i < count; i++) {
            this.cache.add(generex.random());
        }
    }

    /**
     * Alimente le cache avec des données séquentielles à partir de la regex.
     * Méthode plus rapide que la génération aléatoire. À privilégier pour un grand nombre d'objet à mettre en cache
     */
    private void fillCacheWithSequential(int count) {
        Iterator it = generex.iterator();
        for (int i = 0; i < count && it.hasNext(); i++) {
            this.cache.add(it.next());
        }
    }
}

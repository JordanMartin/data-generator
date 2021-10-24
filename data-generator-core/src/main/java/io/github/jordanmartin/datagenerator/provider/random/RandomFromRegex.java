package io.github.jordanmartin.datagenerator.provider.random;

import com.mifmif.common.regex.Generex;
import com.mifmif.common.regex.util.Iterator;
import io.github.jordanmartin.datagenerator.provider.core.StatelessValueProvider;
import io.github.jordanmartin.datagenerator.provider.doc.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.doc.annotation.ProviderCtor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Génère une chaine de caractère aléatoire basé sur une regex.
 * A noter: il peut y avoir des doublons
 */
@Slf4j
@Provider(
        name = "Regex",
        description = "Retourne une chaine de caractères basée sur une expression régulière",
        examples = {
                "Regex(\"[A-Z]{2}[-][0-9]{3}[-][A-Z]{2}\") => OY-230-WL",
                "Regex(\"[A-ZaZ0-9]{16}\") => 0218NaN7XaNaaAE7"
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


    @ProviderCtor("Retourne une valeur aléatoire à partir de l'expression régulière (doublon possible)")
    public RandomFromRegex(String regex) {
        this.generex = new Generex(regex);
    }

    @ProviderCtor("Permet la mise en cache d'un nombre spécifié d'élément unique. " +
            "Ce générateur retourne ensuite aléatoirement une valeur depuis le cache.")
    public RandomFromRegex(String regex, int count) {
        this(regex);
        generateUniqueCache(count);
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
     */
    private void generateUniqueCache(int count) {
        this.cache = new ArrayList<>();
        Iterator it = generex.iterator();
        for (int i = 0; i < count && it.hasNext(); i++) {
            this.cache.add(it.next());
        }
    }
}

package fr.jordanmartin.datagenerator.provider.random;

import com.github.javafaker.Faker;
import fr.jordanmartin.datagenerator.provider.base.ValueProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Génère une chaine de caractère aléatoire basé sur une regex.
 * A noter: il peut y avoir des doublons
 */
@Slf4j
public class RandomFromRegex implements ValueProvider<String> {

    private final Faker faker = new Faker();
    /**
     * La regex utilisé pour la génération d'une valeur
     */
    private final String regex;
    /**
     * Liste de donnée pré-généré. utilisé si {@link #count} est spécifié
     */
    private List<String> source = null;
    /**
     * Nombre d'éléments à générer à partir de la regex
     */
    private int count;

    /**
     * Index du prochain élements à retourner dans la source
     */
    private int nextIdx = 0;

    public RandomFromRegex(String regex) {
        this.regex = regex;
    }

    public RandomFromRegex(String regex, int count) {
        this.regex = regex;
        this.count = count;

        Stream<String> stream = IntStream.range(0, count)
                .mapToObj(idx -> faker.regexify(regex));

        if (log.isInfoEnabled()) {
            AtomicInteger current = new AtomicInteger();
            stream = stream.peek(o -> {
                int percent = (int) (((double) current.incrementAndGet() / count) * 100);
                System.err.printf(
                        "\r%d%% Génération de %d/%d éléments à partir de la regex \"%s\"",
                        percent, current.get(), count, regex
                );
            });
        }
        this.source = stream.collect(Collectors.toList());

        if (log.isInfoEnabled()) {
            System.err.println();
        }
    }

    @Override
    public String getOne() {
        if (source == null) {
            return faker.regexify(regex);
        } else {
            int currentIdx = this.nextIdx;
            this.nextIdx = (this.nextIdx + 1) % count;
            return source.get(currentIdx);
        }
    }
}

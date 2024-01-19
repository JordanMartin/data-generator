package io.github.datagenerator.domain.core;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MapProvider implements ValueProvider<Map<String, ?>> {

    private final DataDefinition definition;

    public MapProvider(DataDefinition dataDefinition) {
        this.definition = dataDefinition;
    }

    public Stream<Map<String, ?>> getStream(int count) {
        DefaultObjectContext context = new DefaultObjectContext();
        AtomicLong currentIndex = new AtomicLong(0);
        return IntStream.range(0, count)
                .peek(mapStream -> context.setObjectIndex(currentIndex.getAndIncrement()))
                .mapToObj(i -> get(context));
    }

    public List<Map<String, ?>> getMany(int count) {
        return getStream(count).collect(Collectors.toList());
    }

    private Object evaluateProvider(ObjectContext context, ValueProvider<?> provider) {
        Object value = provider.get(context);
        if (value instanceof ValueProvider) {
            return ((ValueProvider<?>) value).get(context);
        }
        return value;
    }

    private Map<String, Object> get(DefaultObjectContext context) {
        Map<String, Object> object = new LinkedHashMap<>();
        Map<String, Object> refsSnapshots = new LinkedHashMap<>();
        context.setObject(object);
        context.setRefs(definition.refFields);
        context.setRefsSnapshot(refsSnapshots);

        for (var refField : definition.refFields.entrySet()) {
            String name = refField.getKey();
            ValueProvider<?> provider = refField.getValue();
            Object value = evaluateProvider(context, provider);
            refsSnapshots.put(name, value);
        }

        int fieldNumber = 0;
        for (var field : definition.fields.entrySet()) {
            context.setCurrentFieldNumber(fieldNumber++);
            String name = field.getKey();
            ValueProvider<?> provider = field.getValue();
            Object value = evaluateProvider(context, provider);
            object.put(name, value);
        }

        return object;
    }

    @Override
    public Map<String, Object> get(ObjectContext parentContext) {
        ObjectContext context = new DefaultObjectContext((DefaultObjectContext) parentContext);
        return get((DefaultObjectContext) context);
    }

    public Map<String, Object> get() {
        return get((ObjectContext) null);
    }

}

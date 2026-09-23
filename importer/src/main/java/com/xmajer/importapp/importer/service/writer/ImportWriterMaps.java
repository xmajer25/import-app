package com.xmajer.importapp.importer.service.writer;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

final class ImportWriterMaps {

    private ImportWriterMaps() {
    }

    static <T> Map<String, T> lastRecordByCode(
            Collection<T> items,
            Function<T, String> codeExtractor
    ) {
        Map<String, T> records = new LinkedHashMap<>();

        for (T item : items) {
            records.put(codeExtractor.apply(item), item);
        }

        return records;
    }

    static <T> Map<String, T> toMap(
            Iterable<T> items,
            Function<T, String> codeExtractor
    ) {
        return StreamSupport.stream(items.spliterator(), false)
                .collect(Collectors.toMap(
                        codeExtractor,
                        Function.identity(),
                        (first, second) -> first,
                        LinkedHashMap::new
                ));
    }
}

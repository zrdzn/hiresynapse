package dev.zrdzn.hiresynapse.hiresynapsebackend.shared;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CollectionHelper {

    public static <K, V> Map<K, V> emptyMapIfNull(Map<K, V> map) {
        return map == null ? Collections.emptyMap() : map;
    }

    public static <T> List<T> emptyListIfNull(List<T> list) {
        return list == null ? Collections.emptyList() : list;
    }

}

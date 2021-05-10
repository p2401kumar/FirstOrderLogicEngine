package pk.usc.ai.ValueMap;

import java.util.HashMap;
import java.util.Map;

public class PredicateMap {
    public static HashMap<String, Integer> _predicateMap = new HashMap<>();
    static int _count = 1;

    public static int predicateValue(String predicate) {
        if (_predicateMap.containsKey(predicate)) return _predicateMap.get(predicate);
        _predicateMap.put(predicate, _count++);
        return _predicateMap.get(predicate);
    }

    public static String findString(int val) {
        for (Map.Entry<String, Integer> entry : _predicateMap.entrySet()) {
            if (entry.getValue() == val) return entry.getKey();
        }
        return "";
    }
}

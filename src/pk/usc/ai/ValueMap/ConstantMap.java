package pk.usc.ai.ValueMap;

import java.util.HashMap;
import java.util.Map;

public class ConstantMap {
    public static HashMap<String, Integer> _constantMap = new HashMap<>();
    static int _count = 1000;

    public static int constantValue(String predicate) {
        if (_constantMap.containsKey(predicate)) return _constantMap.get(predicate);
        _constantMap.put(predicate, _count++);
        return _constantMap.get(predicate);
    }

    public static String findString(int val) {
        for (Map.Entry<String, Integer> entry : _constantMap.entrySet()) {
            if (entry.getValue() == val) return entry.getKey();
        }
        return "";
    }
}

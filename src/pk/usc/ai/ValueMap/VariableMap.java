package pk.usc.ai.ValueMap;

import java.util.HashMap;
import java.util.Map;

public class VariableMap {
    public HashMap<String, Integer> _variableMap = new HashMap<>();
    static int _count = 1000000;

    public int variableValue(String var) {
        if (_variableMap.containsKey(var)) return _variableMap.get(var);
        _variableMap.put(var, _count++);
        return _variableMap.get(var);
    }

    public String findString(int val) {
        for (Map.Entry<String, Integer> entry : _variableMap.entrySet()) {
            if (entry.getValue() == val) return entry.getKey();
        }
        return "";
    }

    public boolean hasVariable() {
        return !_variableMap.isEmpty();
    }

    public static int getNewVariable() {
        int r = _count;
        _count++;
        return r;
    }
}

package pk.usc.ai.diff;

import pk.usc.ai.Unit.Parameter;
import pk.usc.ai.ValueMap.VariableMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class DiffSubstitute {
    public HashMap<Parameter, Parameter> var2Constant = new HashMap<>();
    public ArrayList<HashSet<Parameter>> var2Var = new ArrayList<>();
    public HashMap<Parameter, Integer> varName = new HashMap<>();
    boolean isFeasible = true;

    public void addVar2ConstantMapping(HashMap<Parameter, Parameter> mapping) {
        if (!isFeasible)return;
        mapping.forEach((parameter, parameter2) -> {
            var2Constant.put(parameter, parameter2);
        });
    }

    public void addVar2VarMapping(HashSet<Parameter> mapping) {
        if (!isFeasible)return;
        int x = VariableMap.getNewVariable();
        for (Parameter e: mapping) {
            varName.put(e, x);
        }
        var2Var.add(mapping);
    }

    public void notFeasible() {
        isFeasible = false;
        var2Var.clear();
        var2Constant.clear();
    }

    public void process() {
        if (var2Constant.isEmpty() && !var2Var.isEmpty()) {
            notFeasible();
        }
    }
}

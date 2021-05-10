package pk.usc.ai.diff;

import pk.usc.ai.Unit.Parameter;
import pk.usc.ai.Unit.Predicate;

import java.util.*;

public class PredicateDiffStat {
    Predicate _leftPredicate, _rightPredicate;
    boolean isFeasible = true;

    UnionFind<Parameter> unionFind;
    public PredicateDiffStat(Predicate p1, Predicate p2) {
        _leftPredicate = p1;
        _rightPredicate = p2;
        unionFind = new UnionFind<>(new HashSet<>());
    }

    public PredicateDiffStat getPredicate() {
        return this;
    }

    public DiffSubstitute getSubstitute() {
        DiffSubstitute ds = new DiffSubstitute();

        for (ArrayList<Parameter> e : unionFind.collect()) {
            HashSet<Parameter> l = new HashSet<>();
            Parameter constant = null;
            Set<Integer> constantCount = new HashSet<>();
            for (Parameter ee : e) {
                if (ee._isConstant) {
                    constantCount.add(ee._value);
                    constant = ee;
                } else {
                    l.add(ee);
                }
            }

            HashMap<Parameter, Parameter> hm = new HashMap<>();
            Parameter finalConstant = constant;
            if (constant != null) {
                l.forEach(parameter -> {
                    hm.put(parameter, finalConstant);
                });
            }

            if (constantCount.size() == 1) {
                ds.addVar2ConstantMapping(hm);
            } else if (constantCount.size() == 0) {
                ds.addVar2VarMapping(l);
            } else {
                ds.notFeasible();
            }
        }
        //ds.process();
        if (!isFeasible) ds.notFeasible();
        return ds;
    }

    public void printDiff() {
        System.out.println("#################################");
        System.out.println("-----------DIFFS:----------------");
        for (Map.Entry<Parameter, Parameter> ee : getSubstitute().var2Constant.entrySet()) {
            System.out.println(ee.getKey().toString() + " => " + ee.getValue().toString());
        }
        System.out.println("        ----Are Same:-----        ");
        for (HashSet<Parameter> e : getSubstitute().var2Var) {
            System.out.print(e.toString() + " ");
        }
        System.out.println();
        System.out.println("#################################");
    }

    public void param2paramMapping(Parameter left, Parameter right) {
        left = left.clone();
        right = right.clone();

        unionFind.addElement(left);
        unionFind.addElement(right);

        unionFind.union(left, right);
        if (left._isConstant && right._isConstant) {
            if (left._value != right._value)
                isFeasible = false;
        }
    }
}

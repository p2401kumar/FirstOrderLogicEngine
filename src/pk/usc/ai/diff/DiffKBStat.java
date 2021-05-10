package pk.usc.ai.diff;

import pk.usc.ai.Unit.KBSentence;
import pk.usc.ai.Unit.Parameter;
import pk.usc.ai.Unit.Predicate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class DiffKBStat {
    KBSentence _kb1, _kb2;
    PredicateDiffStat _pds;

    public DiffKBStat(KBSentence kb1, KBSentence kb2, PredicateDiffStat pds) {
        _kb1 = kb1;
        _kb2 = kb2;
        _pds = pds;
    }

    public KBSentence getKBAfterUnification(Predicate p1, Predicate p2) {
        DiffSubstitute substitute = _pds.getSubstitute();
        if (!substitute.isFeasible) return null;

        KBSentence newKb = new KBSentence("");
        Set<String> set = new HashSet<>();
        ArrayList<Predicate> arr = new ArrayList<>();
        arr.addAll(_kb1._ORRule);
        arr.addAll(_kb2._ORRule);
        arr.stream().filter(predicate ->
                !((predicate._kbSentence._index == p1._kbSentence._index && p1.toString().equals(predicate.toString()))
                        || (predicate._kbSentence._index == p2._kbSentence._index && p2.toString().equals(predicate.toString())))
        ).forEach(predicate -> {
            Predicate clone = predicate.clone();
            clone._params.clear();
            clone._kbSentence = newKb;
            for (int i = 0; i < predicate._params.size(); i++) {
                Parameter parameter;
                if (predicate._params.get(i)._isConstant) {
                    parameter = predicate._params.get(i).clone();
                } else if (substitute.var2Constant.containsKey(predicate._params.get(i))) {
                    parameter = substitute.var2Constant.get(predicate._params.get(i)).clone();
                    parameter._isConstant = true;
                } else if (substitute.varName.containsKey(predicate._params.get(i))) {
                    String paramName = "(" + newKb._index + "_" + substitute.varName.get(predicate._params.get(i)) + ")";
                    parameter = new Parameter(paramName, newKb, clone, i);
                } else {
                    String paramName = "(" + newKb._index + "_" + predicate._params.get(i)._value + ")";
                    parameter = new Parameter(paramName, newKb, clone, i);
                }
                parameter._KBSentence = newKb;
                parameter._predicate = clone;
                parameter._parameterString = parameter.toString();
                clone._params.add(parameter);
            }
            clone._predicateString = clone.toString();
            if (!set.contains(clone.toString())) {
                newKb._ORRule.add(clone);
            }
            set.add(clone.toString());
        });
        newKb._KBString = newKb.toString();
        return newKb;
    }
}

package pk.usc.ai.Unit;

import pk.usc.ai.ValueMap.VariableMap;

import java.util.*;

public class KBSentence {
    public VariableMap VariableMap = new VariableMap();
    public SortedSet<Predicate> _ORRule = new TreeSet<>();
    public static int _indexCounter = 1;
    public int _index = 0;
    public String _KBString;
/*
    public KBSentence(String kbSentence) {
        _KBString = kbSentence.replace(" ", "");
        _index = _indexCounter;
        _indexCounter++;

        if (kbSentence.contains("|")) {
            //String[] _1 = kbSentence.replace(" ", "").split("=>");
            String[] _2 = kbSentence.replace(" ", "").split("\\|");

            for (int i = 0; i < _2.length; i++) {
                String e = _2[i];
                _ORRule.add(new Predicate(e, this, false, i));
            }
        } else if (!kbSentence.isEmpty()) {
            _ORRule.add(new Predicate(kbSentence.replaceAll(" ", ""), this, false, 0));
        }
    }
*/

    public KBSentence(String kbSentence) {
        _KBString = kbSentence.replace(" ", "");
        _index = _indexCounter;
        _indexCounter++;

        if (kbSentence.contains("=>")) {
            String[] _1 = kbSentence.replace(" ", "").split("=>");
            String[] _2 = _1[0].split("&");

            for (int i = 0; i < _2.length; i++) {
                String e = _2[i];
                _ORRule.add(new Predicate(e, this, true, i));
            }
            _ORRule.add(new Predicate(_1[1], this, false, _2.length));
        } else if (!kbSentence.isEmpty()) {
            _ORRule.add(new Predicate(kbSentence.replaceAll(" ", ""), this, false, 0));
        }
    }


    public int getIndex() {
        return _index;
    }

    public boolean isSimilar(KBSentence kbSentence) {
        if (kbSentence._ORRule.size() != _ORRule.size()) return false;
        HashMap<String, String> param2param = new HashMap<>();

        Iterator<Predicate> it1 = kbSentence._ORRule.iterator();
        Iterator<Predicate> it2 = _ORRule.iterator();

        while (it1.hasNext() && it2.hasNext()){
            Predicate p1 = it1.next();
            Predicate p2 = it2.next();

            if (!p1.isSimilar(p2, param2param)) return false;
        }

        return true;
    }

    @Override
    public String toString() {
        final String[] pString = {""};
        _ORRule.forEach(predicate -> {
            if (pString[0].equals("")) {
                pString[0] += predicate.toString();
            } else {
                pString[0] += " V " + predicate.toString();
            }
        });

        return pString[0];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KBSentence that = (KBSentence) o;
        return _index == that._index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_index);
    }

    public boolean isTrue() {
        HashMap<String, Boolean> map = new HashMap<>();
        for (Predicate e: _ORRule) {
            String s1 = e.toString2();
            if (map.containsKey(s1) && e._isNegative != map.get(s1)) return true;
            map.put(s1, e._isNegative);
        }
        return false;
    }

    public boolean isRule() {
        return VariableMap.hasVariable();
    }
}

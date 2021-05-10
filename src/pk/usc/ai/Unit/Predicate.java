package pk.usc.ai.Unit;

import pk.usc.ai.ValueMap.PredicateMap;
import pk.usc.ai.diff.PredicateDiffStat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class Predicate implements Comparable<Predicate> {
    public boolean _isNegative = false;
    public int _index;
    public int _predicateName;
    public KBSentence _kbSentence;
    public String _predicateString;
    public ArrayList<Parameter> _params = new ArrayList<>();
    boolean _reverseSign;

    public Predicate() {
    }

    public Predicate(String predicate, KBSentence kBSentence, boolean reverseSign, int index) {
        _predicateString = predicate;
        _kbSentence = kBSentence;
        _index = index;
        _reverseSign = reverseSign;

        String[] _1 = predicate.split("\\(");

        if (_1[0].charAt(0) == '~') {
            _isNegative = true;
            _predicateName = PredicateMap.predicateValue(_1[0].split("~")[1]);
        } else {
            _predicateName = PredicateMap.predicateValue(_1[0]);
        }

        String[] _2 = _1[1].split("\\)")[0].split(",");

        for (int i = 0; i < _2.length; i++) {
            _params.add(new Parameter(_2[i], kBSentence, this, i));
        }
        if (reverseSign) reverseSign();
    }

    public void reverseSign() {
        _isNegative = !_isNegative;
    }

    public static boolean isOpposite(Predicate p1, Predicate p2) {
        return (p1._isNegative == !p2._isNegative)
                && (p1._predicateName == p2._predicateName)
                && (p1._params.size() == p2._params.size());
    }

    public static PredicateDiffStat getDiff(Predicate p1, Predicate p2) {
        PredicateDiffStat pds = new PredicateDiffStat(p1, p2);
        if (p1._isNegative == !p2._isNegative && p1._predicateName == p2._predicateName && p1._params.size() == p2._params.size()) {

            for (int i = 0; i < p1._params.size(); i++) {
                pds.param2paramMapping(p1._params.get(i), p2._params.get(i));
            }
        }
        return pds.getPredicate();
    }

    public boolean isSimilar(Predicate predicate, HashMap<String, String> p2p) {
        if (predicate._predicateName != _predicateName
                || predicate._isNegative != _isNegative
                || predicate._params.size() != _params.size()) return false;

        for (int i = 0; i < _params.size(); i++) {
            if (predicate._params.get(i)._isConstant ^ _params.get(i)._isConstant) return false;
            if (predicate._params.get(i)._isConstant
                    && _params.get(i)._isConstant
                    && predicate._params.get(i)._value != _params.get(i)._value) return false;

            String l = predicate._kbSentence._index + predicate._params.get(i).toString();
            String r = _kbSentence._index + _params.get(i).toString();

            if ((p2p.containsKey(l) && !p2p.get(l).equals(r)) || (p2p.containsKey(r) && !p2p.get(r).equals(l))) {
                return false;
            }
            p2p.put(l, r);
            p2p.put(r, l);
        }
        return true;
    }

    @Override
    public String toString() {
        final String[] pString = {""};
        _params.forEach(parameter -> {
            if (pString[0].equals("")) {
                pString[0] += parameter.toString();
            } else {
                pString[0] += "," + parameter.toString();
            }
        });

        //return (_isNegative ? "~" : "") + _predicateName + "(" + pString[0] + ")";
        return (_isNegative ? "~" : "") + PredicateMap.findString(_predicateName) + "(" + pString[0] + ")";
    }

    public String toString2() {
        final String[] pString = {""};
        _params.forEach(parameter -> {
            if (pString[0].equals("")) {
                pString[0] += parameter.toString2();
            } else {
                pString[0] += "," + parameter.toString2();
            }
        });

        return _predicateName + "(" + pString[0] + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Predicate predicate = (Predicate) o;
        return _isNegative == predicate._isNegative
                && _index == predicate._index
                && _predicateName == predicate._predicateName
                && Objects.equals(_kbSentence, predicate._kbSentence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_isNegative, _index, _predicateName, _kbSentence, _reverseSign);
    }

    public Predicate clone() {
        Predicate predicate = new Predicate();
        predicate._isNegative = _isNegative;
        predicate._index = _index;
        predicate._predicateName = _predicateName;
        predicate._kbSentence = _kbSentence;
        predicate._predicateString = _predicateString;
        predicate._reverseSign = _reverseSign;
        return predicate;
    }

    @Override
    public int compareTo(Predicate p) {
        if (_predicateName == p._predicateName) {
            if (_isNegative && !p._isNegative) return -1;
            else if (!_isNegative && p._isNegative) return 1;

            Iterator<Parameter> it1 = _params.iterator();
            Iterator<Parameter> it2 = p._params.iterator();
            while (it1.hasNext() && it2.hasNext()) {
                Parameter p1 = it1.next();
                Parameter p2 = it2.next();

                if (p1._isConstant && p2._isConstant) {
                    return (p1._value - p2._value) < 0 ? -1 : 1;
                } else if (!p1._isConstant && p2._isConstant) {
                    return 1;
                } else {
                    return -1;
                }
            }
        }
        return (_predicateName - p._predicateName) < 0 ? -1 : 1;
    }
}

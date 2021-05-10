package pk.usc.ai.Unit;

import pk.usc.ai.ValueMap.ConstantMap;

import java.util.Objects;

public class Parameter {
    public boolean _isConstant;
    public int _value;
    public KBSentence _KBSentence;
    public Predicate _predicate;
    public int _index;
    public String _parameterString;

    public int _extra = -1;

    public Parameter(){}
    public Parameter(String parameter, KBSentence KBSentence, Predicate predicate, int index) {
        _parameterString = parameter;
        _KBSentence = KBSentence;
        _predicate = predicate;
        _index = index;

        _isConstant = ('A' <= parameter.charAt(0) && parameter.charAt(0) <= 'Z');
        if (_isConstant) {
            _value = ConstantMap.constantValue(parameter);
        } else {
            _value = KBSentence.VariableMap.variableValue(parameter);
        }
    }

    @Override
    public String toString() {
        if (_isConstant) {
            return ConstantMap.findString(_value);
        } else {
            return _KBSentence.VariableMap.findString(_value);
        }
    }

    public String toString2() {
        if (_isConstant) {
            return "" + _value;
        } else {
            return "_" + _value;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parameter parameter = (Parameter) o;
        return _isConstant == parameter._isConstant
                && _value == parameter._value
                && Objects.equals(_KBSentence, parameter._KBSentence);
                //&& Objects.equals(_predicate, parameter._predicate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_isConstant, _value, _KBSentence);
    }

    @Override
    public Parameter clone() {
        Parameter p = new Parameter();
        p._isConstant = _isConstant;
        p._value = _value;
        p._KBSentence = _KBSentence;
        p._predicate = _predicate;
        p._index = _index;
        p._parameterString = _parameterString;
        return p;
    }
}

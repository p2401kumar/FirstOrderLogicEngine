# FirstOrderLogicEngine

Given Knowledge Base all in specific format, this engine can answer Queries given in same specified format.
<br/>

<h3>Query format</h3> Each query will be a single literal of the form Predicate(Constant_Arguments) or
~Predicate(Constant_Arguments) and will not contain any variables. Each predicate will have
between 1 and 25 constant arguments. Two or more arguments will be separated by commas.

<h3>KB format</h3> Each sentence in the knowledge base is written in one of the following forms:
<br/>1) An implication of the form p1 ∧ p2 ∧ ... ∧ pm ⇒ q, where its premise is a conjunction of
literals and its conclusion is a single literal. Remember that a literal is an atomic sentence
or a negated atomic sentence.
<br/>2) A single literal: q or ~q

<br/><br/>
<h2>Example</h2>
<h3>Given KB</h3><br/>
&emsp; Ready(x) => Train(Come,x)<br/>
&emsp; Healthy(x) & Train(y,x) => Learn(y,x)<br/>
&emsp; Learn(Come,x) => Train(Sit,x)<br/>
&emsp; Learn(Come,x) & Learn(Sit,x) => Train(Down,x)<br/>
&emsp; Learn(Down,x) => Graduate(x)<br/>
&emsp; Ready(Hayley)<br/>
&emsp; Ready(Ares)<br/>
&emsp; Healthy(Ares)<br/>


<h3>Queries</h3><br/>
&emsp; Learn(Sit,Ares)<br/>
&emsp; Graduate(Hayley)

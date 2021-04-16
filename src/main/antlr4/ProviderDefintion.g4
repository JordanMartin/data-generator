grammar ProviderDefintion;

@header {
package fr.jordanmartin.datagenerator.definition;
}

definition: func;

func: func_name LPAREN func_params RPAREN;
func_params: ( | func_param (COMMA func_param)*);
func_name: Ident;
func_param: func | string | number;
number: Integer | Double;
string: StringLiteral;

StringLiteral: '"' StringCharacter* '"';
fragment StringCharacter    : ~["\\\r\n] |	EscapeSequence;
fragment EscapeSequence     : '\\' [btnfr"'\\];

Ident : [a-zA-Z][a-zA-Z_0-9]*;
Integer: Sign? [0-9]+;
Double: Sign? [0-9]+ ('.'[0-9]+);
fragment Sign : ('+' | '-');
COMMA : ',';
LPAREN : '(';
RPAREN : ')';
WS : [ \r\n\t] + -> skip;
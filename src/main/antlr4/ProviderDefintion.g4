grammar ProviderDefintion;

@header {
package fr.jordanmartin.datagenerator.definition;
}

definition: func;

func: func_name LPAREN func_params RPAREN;
func_params: ( | func_param (COMMA func_param)*);
func_name: Ident;
func_param: func | string | number | list;
number: Integer | Double;
string: StringLiteral;

list: LBRACK ( | list_element (COMMA list_element)*) RBRACK;
list_element:  func | string | number;

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
LBRACK : '[';
RBRACK : ']';
WS : [ \r\n\t] + -> skip;
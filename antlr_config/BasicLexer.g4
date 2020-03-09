lexer grammar BasicLexer;
@header {
package antlr;
}
// program
BEGIN: 'begin';
END: 'end';
IS: 'is';
ASKIP: 'skip';

// type
INT: 'int';
STRING: 'string' ;
CHAR: 'char';
BOOL: 'bool';
TRUE: 'true';
FALSE: 'false';

fragment DIGIT: '0'..'9' ;
fragment HEX: '0'..'9'|'A'..'F'|'a'..'f' ;

// unary operators
NOT: '!' ;
LEN: 'len' ;
ORD: 'ord' ;
CHR: 'chr' ;

ALLINT: (DIGIT)+ ;
BINARY: '0b'('0'|'1')+ ;
HEXADECIMAL: '0x'(HEX)+ ;

// binary operators
PLUS: '+' ;
MINUS: '-' ;
TIME: '*' ;
DIVIDE: '/' ;
MOD: '%' ;

//binary bool operator
GREATER_E: '>=' ;
SMALLER_E: '<=' ;
GREATER: '>' ;
SMALLER: '<' ;
EQUAL: '==' ;
NOT_EQUAL: '!=' ;
B_AND: '&&' ;
B_OR: '||' ;



// stat
IMPORT: 'import';
WACCSUFFIX: '.wacc';
READ: 'read';
FREE: 'free';
RETURN: 'return';
EXIT: 'exit';
PRINT: 'print';
PRINTLN: 'println';
IF: 'if';
THEN: 'then';
ELSE: 'else';
FI: 'fi';
WHILE: 'while';
DO: 'do';
DONE: 'done';

//brackets
OPEN_PARENTHESES: '(' ;
CLOSE_PARENTHESES: ')' ;
OPEN_SQUARE: '[' ;
CLOSE_SQUARE: ']' ;

// pair
NEWPAIR: 'newpair' ;
PAIR: 'pair' ;
FST: 'fst';
SND: 'snd';

COLON: ';';
COMMA: ',';
CALL: 'call';
ASSIGN: '=';
NULL: 'null';
COMMENT: '#' .*? '\n' -> skip;

// escaped char
ST: '"';
AP: '\'';


IGNOR: [ \t\r\n] -> skip;

fragment CHARACTER: ANY_ASCII
| ESCAPED_CHAR;

CHAR_LITER: AP CHARACTER AP;

STR_LITER: ST (CHARACTER)* ST ;

IDENT: ('_' | 'a'..'z' | 'A'..'Z') ('_' | 'a'..'z' | 'A'..'Z' | DIGIT)*;
ANY_ASCII: ~[\\'"];
ESCAPED_CHAR: '\\' [0btnfr"'\\];
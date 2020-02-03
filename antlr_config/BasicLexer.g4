lexer grammar BasicLexer;
//@header {
//package antlr;
//}

// program
BEGIN: 'begin';
END: 'end';
IS: 'is';
ASKIP: 'skip';

// type
INT: 'int';
DIGIT: '0'..'9' ;
STRING: 'string' ;
CHAR: 'char';
BOOL: 'bool';
TRUE: 'true';
FALSE: 'false';


// unary operators
NOT: '!' ;
NEGATIVE: '-' ;
LEN: 'len' ;
ORD: 'ord' ;
CHR: 'chr' ;

// binary operators
PLUS: '+' ;
MINUS: '-' ;
EQUAL: '==' ;
NOT_EQUAL: '!=' ;
B_AND: '&&' ;
B_OR: '||' ;
TIME: '*' ;
DIVIDE: '/' ;
GREATER: '>' ;
SMALLER: '<' ;
MOD: '%' ;
GREATER_E: '>=' ;
SMALLER_E: '<=' ;

// stat
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
lexer grammar BasicLexer;
// program
BEGIN: 'begin';
END: 'end';
IS: 'is';
ASKIP: 'skip';

// type
INT: 'int';
DIGIT: '0'..'9' ;
INTEGER: DIGIT+ ;
CHAR: 'a'..'z' | 'A'..'Z' ;    //any-ASCII-character-except-`\'-`''-`"'

UNDERSCORE: '_';
BOOL: 'bool';

// unary operators
NOT: '!' ;
NEGATIVE: '-' ;
LEN: 'len' ;
ORD: 'ord' ;
CHR: 'char' ;

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


// state
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

TRUE: 'true';
FALSE: 'false';



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
// EOL: '\n';

// escaped char
ESCAPED_CHAR:[0btnfr"'\\];     //not sure
BACKSLASH: '\\';    //not sure
AP: '\'';        //not sure
ST: '"';


CHAR_LITER: [^\\'"];     //not sure!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
STRING: CHAR+ ;
IGNOR: [ \t\r\n] -> skip;

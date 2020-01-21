lexer grammar BasicLexer;
//numbers
fragment DIGIT: '0'..'9' ;
INTEGER: DIGIT+ ;
WS : [ \t\r\n]+ -> skip ;

BEN: 'begin' ;
END: 'end' ;
MYSKIP: 'skip' ;

//null
NULL: 'null' ;

//uniary operators
NOT: '!' ;
LENGTH: 'len' ;
ORD: 'ord' ;
CHR: 'chr' ;

//boolean
TRUE: 'true';
FALSE: 'false';

//binary operators
PLUS: '+' ;
MINUS: '-' ;
GREATER: '>' ;
GREATEREQUAL: '>=' ;
SMALLER: '<' ;
SMALLEREQUAL: '<=' ;
EQUAL: '==' ;
NOTEQUAL: '!=' ;
MULTIPLICATION: '*' ;
DIVISION: '/' ;
MOD: '%' ;
BITWISEOR: '||' ;
BITWISEAND: '&&' ;

//brackets
OPEN_PARENTHESES: '(' ;
CLOSE_PARENTHESES: ')' ;
OPEN_BRACKETS: '[' ;
CLOSE_BRACKETS: ']' ;

//semi-colon
SEMI_COLON: ';' ;

//comma
COMMA: ',' ;



//strings
UNDERSCORE: '_' ;
SINGLE_QUOTE: '\'' ;
DOUBLE_QUOTE: '"' ;
EOL: '\n' ;
/*fragment LOWERCASE_ALPHA: 'a'..'z' ;
fragment UPPERCASE_ALPHA: 'A'..'Z' ;*/


//comments
START_COMMENTS: '#' ;


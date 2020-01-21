parser grammar BasicParser;

options {
  tokenVocab=BasicLexer;
}

binary_oper: TIME | DIVIDE | MOD | PLUS | MINUS | GREATER
| SMALLER | GREATER_E | SMALLER_E | EQUAL | NOT_EQUAL | B_AND | B_OR ;

assign_lhs: ident
| array_elem
| pair_elem ;

assign_rhs: expr
| array_liter
| NEWPAIR OPEN_PARENTHESES expr COMMA expr CLOSE_PARENTHESES
| pair_elem
| CALL ident OPEN_PARENTHESES (arg_list)? CLOSE_PARENTHESES ;

arg_list: expr (COMMA expr)* ;

pair_elem: FST expr
| SND expr;

base_type: INT
| BOOL
| CHAR
| STRING ;

// array_type: type OPEN_SQUARE CLOSE_SQUARE ; //type
array_type: OPEN_SQUARE CLOSE_SQUARE ; //type
pair_type: PAIR OPEN_PARENTHESES pair_elem_type COMMA pair_elem_type CLOSE_PARENTHESES ;

type: base_type
| array_type
| pair_type ;

pair_elem_type: base_type
| array_type
| PAIR ;

ident: (UNDERSCORE | CHAR) (UNDERSCORE | CHAR | DIGIT)*;

array_elem: ident (OPEN_SQUARE expr CLOSE_SQUARE)+ ;

int_liter: (int_sign)? (DIGIT)+ ;

int_sign: PLUS | MINUS ;

bool_liter: TRUE | FALSE ;

char_liter: AP CHAR_LITER AP
| BACKSLASH ESCAPED_CHAR;

str_liter: ST (CHAR_LITER)* ST ;

array_liter: OPEN_SQUARE ( expr (COMMA expr)* )? CLOSE_SQUARE ;

pair_liter: NULL;

param_list: param (COMMA param)*;

param: type ident;

expr: int_liter
| bool_liter
| char_liter
| str_liter
| pair_liter
| ident
| array_elem
| unary_oper expr
| expr binary_oper expr
| OPEN_PARENTHESES expr CLOSE_PARENTHESES ;

unary_oper: NOT | NEGATIVE | LEN | ORD | CHR ;

stat: ASKIP
| type ident ASSIGN assign_rhs
| assign_lhs ASSIGN assign_rhs
| READ assign_lhs
| FREE expr
| RETURN expr
| EXIT expr
| PRINT expr
| PRINTLN expr
| IF expr THEN stat ELSE stat FI
| WHILE expr DO stat DONE
| BEGIN stat END
| stat COLON stat;

func: type ident OPEN_PARENTHESES (param_list)? CLOSE_PARENTHESES IS stat END ;

// EOF indicates that the program must consume to the end of the input.
prog: BEGIN (func)* stat END EOF ;
